package com.heroku.api.parser;

import java.lang.reflect.Type;

public interface Parser {

    <T> T parse(byte[] data, Type type);

    String encode(Object object);
}
