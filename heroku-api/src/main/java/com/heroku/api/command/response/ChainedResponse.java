package com.heroku.api.command.response;


import com.heroku.api.command.Command;

/**
 * A chained response is a response that returns a command that should subsequently be executed.
 * <p/>
 * For intstance to get access to logs, an initial LogsRequest is sent which sets up the logplex session
 * and returns a LogResponse  (which is a ChainedResponse<TextCommand>).
 * Executing the command returned from the LogResponse will
 * actually be the command that gets log data for you.
 *
 * @param <T> The type of the 'next' command that should be executed.
 */
public class ChainedResponse<T extends Command> {

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
