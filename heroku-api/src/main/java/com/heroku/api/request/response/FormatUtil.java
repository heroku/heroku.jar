package com.heroku.api.request.response;

import com.heroku.api.Heroku;
import com.heroku.api.exception.ResponseFailedException;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class FormatUtil {
    public static int asInt(Object val) {
        return Integer.valueOf(asString(val));
    }
    
    public static Date asDate(Object val) {
        try {
            return DateFormat.getInstance().parse(asString(val));
        } catch (ParseException e) {
            throw new ResponseFailedException(String.format("Unable to parse %s into a date.", val), e);
        }
    }

    public static Heroku.Stack asStack(Object val) {
        return Heroku.Stack.fromString(asString(val));
    }

    public static String asString(Object val) {
        return String.valueOf(val);
    }
}
