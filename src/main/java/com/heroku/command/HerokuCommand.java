package com.heroku.command;

import com.heroku.connection.HerokuAPIException;
import com.heroku.connection.HerokuConnection;

import java.io.IOException;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public interface HerokuCommand {
    HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException;
}
