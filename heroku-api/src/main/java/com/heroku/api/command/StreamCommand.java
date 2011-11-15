package com.heroku.api.command;


import com.heroku.api.command.response.StreamResponse;
import com.heroku.api.exception.RequestFailedException;

import java.io.InputStream;
import java.net.URL;

public class StreamCommand extends GetURLCommand<StreamResponse> {


    public StreamCommand(URL toStream) {
        super(toStream);
    }

    @Override
    public StreamResponse getResponse(InputStream inputStream, int status) {
        if (status == 200)
            return new StreamResponse(inputStream);
        else
            throw new RequestFailedException("Unable to open stream", status, inputStream);
    }
}
