package com.heroku.api.command.response;


import com.heroku.api.http.HttpUtil;

public class TextResponse {

    String text;

    public TextResponse(byte[] in) {
        text = HttpUtil.getUTF8String(in);
    }

    public String getText() {
        return text;
    }
}
