package com.heroku.api.parser;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author Ryan Brainard
 */
public class JerseyClientJsonParserTest {

    @Test
    public void testParse() throws Exception {
        Parser p = new JerseyClientJsonParser();
        Something expected = new Something("coffee");
        Something actual = p.parse("{\"name\":\"coffee\"}".getBytes(), Something.class);
        assertEquals(actual, expected);
    }
}
