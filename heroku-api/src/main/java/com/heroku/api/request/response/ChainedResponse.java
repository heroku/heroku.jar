package com.heroku.api.request.response;


import com.heroku.api.request.Request;

/**
 * A chained response is a response that returns a request that should subsequently be executed.
 * <p/>
 * For intstance to get access to logs, an initial LogsRequest is sent which sets up the logplex session
 * and returns a LogResponse  (which is a ChainedResponse<TextRequest>).
 * Executing the request returned from the LogResponse will
 * actually be the request that gets log data for you.
 *
 * @param <T> The type of the 'next' request that should be executed.
 */
public class ChainedResponse<T extends Request> {

    T nextCommand;
    byte[] data;

    public ChainedResponse(T next, byte[] raw) {
        nextCommand = next;
        data = raw;
    }

    public T getNextCommand() {
        return nextCommand;
    }
}
