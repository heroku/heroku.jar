package com.heroku.api.request;

import com.heroku.api.Heroku;
import com.heroku.api.request.log.LogStreamResponse;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class LogStreamResponseTest {

    @Test
    public void testLogStream() throws IOException {
        LogStreamResponse response = new LogStreamResponse(new URL(Heroku.Config.ENDPOINT.value));
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.openStream()));
        String line = "";
        while (line != null) {
            System.out.println(line);
            line = reader.readLine();
        }

    }
}
