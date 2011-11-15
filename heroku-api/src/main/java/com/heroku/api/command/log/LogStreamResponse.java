package com.heroku.api.command.log;


import com.heroku.api.command.response.ChainedResponse;
import com.heroku.api.command.StreamCommand;

public class LogStreamResponse extends ChainedResponse<StreamCommand> {
    public LogStreamResponse(StreamCommand next, byte[] raw) {
        super(next, raw);
    }
}
