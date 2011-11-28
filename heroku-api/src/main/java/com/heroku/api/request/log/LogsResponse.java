package com.heroku.api.request.log;


import com.heroku.api.request.TextRequest;
import com.heroku.api.request.response.ChainedResponse;

public class LogsResponse extends ChainedResponse<TextRequest> {

    public LogsResponse(TextRequest next, byte[] raw) {
        super(next, raw);
    }
}
