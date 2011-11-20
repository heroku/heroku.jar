package com.heroku.api.command.log;


import com.heroku.api.command.TextCommand;
import com.heroku.api.command.response.ChainedResponse;

public class LogsResponse extends ChainedResponse<TextCommand> {

    public LogsResponse(TextCommand next, byte[] raw) {
        super(next, raw);
    }
}
