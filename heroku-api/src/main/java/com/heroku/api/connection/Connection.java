package com.heroku.api.connection;

import com.heroku.api.command.Command;

/**
 * Connection manager for connecting to the Heroku API.
 *
 * @author Naaman Newbold
 */
public interface Connection<F> {


    <T> T executeCommand(Command<T> command);

    <T> F executeCommandAsync(Command<T> command);

    void close();

}
