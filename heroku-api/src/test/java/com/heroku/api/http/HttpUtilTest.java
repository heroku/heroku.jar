package com.heroku.api.http;

import org.testng.annotations.Test;

import static com.heroku.api.http.HttpUtil.encodeIncludingSpecialCharacters;
import static org.testng.Assert.assertEquals;

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

    @Test
    public void encodeSpecialCharsInEmailAddressShouldNotLeaveAPeriodOrAtsign() {
        assertEquals("j%40heroku%2djar%2ecom", encodeIncludingSpecialCharacters("j@heroku-jar.com"));
    }
}
