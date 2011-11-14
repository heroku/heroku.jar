package com.heroku.api.command;


public class LogStreamResponse extends ChainedResponse<StreamCommand> {
    public LogStreamResponse(StreamCommand next, byte[] raw) {
        super(next, raw);
    }
}
