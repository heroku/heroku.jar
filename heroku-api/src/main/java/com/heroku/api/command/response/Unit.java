package com.heroku.api.command.response;

/**
 * This class represents the functional equivalent of scala's Unit type.
 * <p/>
 * Commands that dont return anything can implement CommandResponse<Unit> and return Unit.unit on completion.
 */
public enum Unit {
    unit;
}
