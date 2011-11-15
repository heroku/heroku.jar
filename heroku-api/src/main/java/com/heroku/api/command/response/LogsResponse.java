package com.heroku.api.command.response;


import com.heroku.api.command.response.ChainedResponse;
import com.heroku.api.command.TextCommand;

public class LogsResponse extends ChainedResponse<TextCommand> {

    public LogsResponse(TextCommand next, byte[] raw) {
        super(next, raw);
    }
}
