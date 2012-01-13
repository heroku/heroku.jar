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

    private RequestConfig config;


    public Run(String app, String command) {
        config = new RequestConfig().app(app).with(Heroku.RequestKey.Command, command);
    }


    public Run(String app, String command, boolean attach) {
       this(app,command);
       if(attach){
           config = config.with(Heroku.RequestKey.Attach, "true");
       }
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
        if (config.has(Heroku.RequestKey.Attach)) {
            return HttpUtil.encodeParameters(config, Heroku.RequestKey.AppName, Heroku.RequestKey.Command, Heroku.RequestKey.Attach);
        } else {
            return HttpUtil.encodeParameters(config, Heroku.RequestKey.AppName, Heroku.RequestKey.Command);
        }
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> map = Http.Header.Util.setHeaders(Http.ContentType.FORM_URLENCODED);
        //workaround should be removed when core is updated to not require this
        map.put("User-Agent", "heroku-gem/2.4");
        return map;
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
                                                   