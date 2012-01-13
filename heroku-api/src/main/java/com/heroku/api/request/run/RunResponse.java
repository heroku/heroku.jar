package com.heroku.api.request.run;


import com.heroku.api.exception.HerokuAPIException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.net.URI;

public class RunResponse {

    String slug;
    String command;
    String upid;
    String process;
    String action;
    URI rendezvous_url;
    String type;
    int elapsed;
    boolean attached;
    String transitioned_at;
    String state;

    public String getSlug() {
        return slug;
    }

    private void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCommand() {
        return command;
    }

    private void setCommand(String command) {
        this.command = command;
    }

    public String getUpid() {
        return upid;
    }

    private void setUpid(String upid) {
        this.upid = upid;
    }

    public String getProcess() {
        return process;
    }

    private void setProcess(String process) {
        this.process = process;
    }

    public String getAction() {
        return action;
    }

    private void setAction(String action) {
        this.action = action;
    }

    public URI getRendezvous_url() {
        return rendezvous_url;
    }

    private void setRendezvous_url(URI rendezvous_url) {
        this.rendezvous_url = rendezvous_url;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    public int getElapsed() {
        return elapsed;
    }

    private void setElapsed(int elapsed) {
        this.elapsed = elapsed;
    }

    public boolean isAttached() {
        return attached;
    }

    private void setAttached(boolean attached) {
        this.attached = attached;
    }

    public String getTransitioned_at() {
        return transitioned_at;
    }

    private void setTransitioned_at(String transitioned_at) {
        this.transitioned_at = transitioned_at;
    }

    public String getState() {
        return state;
    }

    private void setState(String state) {
        this.state = state;
    }

    /**
     * Should only be called if you started the process with attach enabled
     *
     * @return an InputStream that contains the output of your process.
     */
    public InputStream attach() {
        if(attached == false){
            throw new IllegalStateException("The process was not started as attached, and has probably already executed. Check your app logs");
        }
        try {
            String host = rendezvous_url.getHost();
            int port = rendezvous_url.getPort();
            String secret = rendezvous_url.getPath().substring(1);
            SocketFactory socketFactory = SSLSocketFactory.getDefault();
            final Socket socket = socketFactory.createSocket(host, port);
            // Create streams to securely send and receive data to the server
            final InputStream in = socket.getInputStream();
            final OutputStream out = socket.getOutputStream();
            /*Write the secret to the server*/
            OutputStreamWriter owriter = new OutputStreamWriter(out);
            final BufferedWriter writer = new BufferedWriter(owriter);
            writer.write(secret);
            writer.flush();
            InputStreamReader ireader = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(ireader);
            /*this line reads the string "rendezvous" */
            reader.readLine();
            return in;
        } catch (IOException e) {
            throw new HerokuAPIException("IOException while running process", e);
        }
    }


}
