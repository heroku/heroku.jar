package com.heroku.api.parser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Provides a java.lang.reflect.Type for generically-typed arguments.
 *
 * Source is based on Neal Gafter's TypeReference class
 * http://gafter.blogspot.com/2006/12/super-type-tokens.html
 *
 * @author Naaman Newbold
 */
public abstract class TypeReference<T> {
    private final Type t;
    
    protected TypeReference() {
        Type superclass = getClass().getGenericSuperclass();

        if (superclass instanceof Class)
            throw new RuntimeException("No type parameter supplied.");

        t = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }
    
    public Type getType() {
        return t;
    }

}