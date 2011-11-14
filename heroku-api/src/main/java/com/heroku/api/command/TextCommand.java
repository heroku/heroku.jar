package com.heroku.api.command;

import com.heroku.api.exception.RequestFailedException;

import java.io.InputStream;
import java.net.URL;


public class TextCommand extends GetURLCommand<TextResponse> {
    public TextCommand(URL toGet) {
        super(toGet);
    }

    @Override
    public TextResponse getResponse(InputStream inputStream, int status) {
        if (status == 200)
            return new TextResponse(inputStream);
        else
            throw new RequestFailedException("Unable to open stream", status, inputStream);
    }
}
