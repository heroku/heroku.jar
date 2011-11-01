package com.heroku.api.command;

import com.heroku.api.connection.HerokuAPIException;
import com.heroku.api.connection.HerokuConnection;

import java.io.IOException;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public interface HerokuCommand {
    HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException;
}
