package com.heroku.api.parser;


public class JsonSelector {

    public static void selectParser(Parser parser) {
        Json.Holder.parser = parser;
    }

}
