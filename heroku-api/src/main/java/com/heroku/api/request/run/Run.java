package com.heroku.api.request.run;


import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.parser.Json;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Map;

public class Run implements Request<RunResponse> {

    private final RequestConfig config;

    public Run(String app, String command) {
        config = new RequestConfig().app(app).with(Heroku.RequestKey.Command, command).with(Heroku.RequestKey.Attach, "false");
    }


    public Run(String app, String command, boolean attach) {
        config = new RequestConfig().app(app).with(Heroku.RequestKey.Command, command).with(Heroku.RequestKey.Attach, Boolean.toString(attach));
    }


    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }


    @Override
    public String getEndpoint() {
        return Heroku.Resource.Process.format(config.get(Heroku.RequestKey.AppName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, Heroku.RequestKey.AppName, Heroku.RequestKey.Command, Heroku.RequestKey.Attach);
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Http.Header.Util.setHeaders(Http.ContentType.FORM_URLENCODED);
    }

    @Override
    public RunResponse getResponse(byte[] bytes, int status) {
        if (status == 200) {
            return Json.parse(bytes, this.getClass());
        } else {
            throw new RequestFailedException("Run Request Failed,", status, bytes);
        }
    }
}
