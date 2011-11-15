package com.heroku.api.json;


import java.util.List;
import java.util.Map;

public interface JsonParser {

    Map<String, String> parseMap(byte[] raw);

    List<Map<String, String>> parseArray(byte[] raw);

}
