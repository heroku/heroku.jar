package com.heroku.api.request;

import com.heroku.api.http.Http;

import java.util.Map;

/**
 * Transforms a {@link Request}<code>&lt;A&gt;</code> into a {@link Request}<code>&lt;B&gt;</code>
 * to allow parsing of the raw response to create a <code>B</code>.
 *
 * This is useful for extending standard request classes with a different response type.
 *
 * @author Ryan Brainard
 */
public abstract class RequestTransformation<A,B> implements Request<B> {

    private final Request<A> a;

    public RequestTransformation(Request<A> a) {
        this.a = a;
    }

    @Override
    public Http.Method getHttpMethod() {
        return a.getHttpMethod();
    }

    @Override
    public String getEndpoint() {
        return a.getEndpoint();
    }

    @Override
    public boolean hasBody() {
        return a.hasBody();
    }

    @Override
    public String getBody() {
        return a.getBody();
    }

    @Override
    public Map<String,?> getBodyAsMap() {
        return a.getBodyAsMap();
    }

    @Override
    public Http.Accept getResponseType() {
        return a.getResponseType();
    }

    @Override
    public Map<String, String> getHeaders() {
        return a.getHeaders();
    }

    @Override
    public abstract B getResponse(byte[] bytes, int status, Map<String,String> responseHeaders);
}
