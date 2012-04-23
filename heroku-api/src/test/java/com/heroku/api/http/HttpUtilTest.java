package com.heroku.api.http;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static com.heroku.api.http.HttpUtil.*;

/**
 * Tests for http related utility methods.
 *
 * @author Naaman Newbold
 */
public class HttpUtilTest {

    @Test
    public void encodeParametersIncludingSpecialCharactersShouldEncodePeriodDashAsteriskAndUnderscore() {
        assertEquals("%2e%2d%2a%5f", encodeIncludingSpecialCharacters(".-*_"));
    }
}
