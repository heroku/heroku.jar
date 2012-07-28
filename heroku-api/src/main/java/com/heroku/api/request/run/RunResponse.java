package com.heroku.api.request.run;


import com.heroku.api.Proc;
import com.heroku.api.exception.HerokuAPIException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;

public class RunResponse {

    private final Proc proc;

    public RunResponse(Proc proc) {
        this.proc = proc;
    }

    public Proc getProc() {
        return proc;
    }

    /**
     * Should only be called if you started the process with attach enabled
     *
     * @return an InputStream that contains the output of your process.
     */
    public InputStream attach() {
        if(!proc.isAttached()) {
            throw new IllegalStateException("The process was not started as attached, and has probably already executed. Check your app logs");
        }
        try {
            String host = proc.getRendezvousUrl().getHost();
            int port = proc.getRendezvousUrl().getPort();
            String secret = proc.getRendezvousUrl().getPath().substring(1);
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
