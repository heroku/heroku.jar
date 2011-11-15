package com.heroku.api.command;


import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.http.HttpUtil;

import java.io.*;
import java.net.URL;
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

    public static String getString(InputStream in) {
        return new String(getBytes(in));
    }

    public static void closeQuietly(Closeable c) {
        try {
            c.close();
        } catch (IOException e) {
            //ignored
        }
    }

    public static Reader bytesReader(byte[] bytes) {
        return new InputStreamReader(bytesInputStream(bytes));
    }

    public static InputStream bytesInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }




}
