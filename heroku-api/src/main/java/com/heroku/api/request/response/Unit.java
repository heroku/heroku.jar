package com.heroku.api.request.response;

/**
 * This class represents the functional equivalent of scala's Unit type.
 * <p/>
 * Commands that dont return anything can implement Response<Unit> and return Unit.unit on completion.
 */
public enum Unit {
    unit;
}
