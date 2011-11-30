package com.heroku.api.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface JsonParser {

    <T> T parse(byte[] data, Type type);

    Map<String, String> parseMap(byte[] raw);

    List<Map<String, String>> parseArray(byte[] raw);

}
