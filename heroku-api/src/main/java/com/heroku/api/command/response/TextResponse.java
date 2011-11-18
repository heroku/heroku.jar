package com.heroku.api.command.response;


import com.heroku.api.command.CommandUtil;

public class TextResponse {

    String text;

    public TextResponse(byte[] in) {
        text = CommandUtil.getUTF8String(in);
    }

    public String getText() {
        return text;
    }
}
