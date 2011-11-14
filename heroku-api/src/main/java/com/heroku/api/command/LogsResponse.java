package com.heroku.api.command;


public class LogsResponse extends ChainedResponse<TextCommand> {

    public LogsResponse(TextCommand next, byte[] raw) {
        super(next, raw);
    }
}
