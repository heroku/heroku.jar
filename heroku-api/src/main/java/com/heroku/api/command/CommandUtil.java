package com.heroku.api.command;


import com.heroku.api.exception.HerokuAPIException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class CommandUtil {

    public static byte[] getBytes(InputStream in) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        WritableByteChannel wbc = Channels.newChannel(os);
        ReadableByteChannel rbc = Channels.newChannel(in);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            while (rbc.read(byteBuffer) != -1) {
                byteBuffer.flip();
                wbc.write(byteBuffer);
                byteBuffer.clear();
            }
            wbc.close();
            rbc.close();
            return os.toByteArray();
        } catch (IOException e) {
            throw new HerokuAPIException("IOException while reading response");
        }
    }



    public static String getUTF8String(byte[] in) {
        try {
            return new String(in, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new HerokuAPIException("Somehow UTF-8 is unsupported");
        }
    }

}
