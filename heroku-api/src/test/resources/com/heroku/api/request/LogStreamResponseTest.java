package com.heroku.api.request;

import com.heroku.api.Heroku;
import com.heroku.api.request.log.LogStreamResponse;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;

public class LogStreamResponseTest {

    @Test
    public void testLogStream() throws IOException {
        LogStreamResponse response = new LogStreamResponse(new URL(Heroku.Config.ENDPOINT.value));
        response.openStream().close();
    }
}
