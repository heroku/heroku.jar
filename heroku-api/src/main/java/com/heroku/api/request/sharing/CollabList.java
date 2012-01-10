package com.heroku.api.request.sharing;

import com.heroku.api.Collaborator;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.parser.XmlParser;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class CollabList implements Request<List<Collaborator>> {

    private final RequestConfig config;

    public CollabList(String appName) {
        config = new RequestConfig().app(appName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Collaborators.format(config.get(Heroku.RequestKey.AppName));
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        throw HttpUtil.noBody();
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.XML;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public List<Collaborator> getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            Collaborators collaborators = new XmlParser().parse(bytes, Collaborators.class);
            return collaborators.getCollaborator();
        } else {
            throw new RequestFailedException("List collaborators failed.", status, bytes);
        }
    }

    @XmlRootElement
    static class Collaborators {
        List<Collaborator> collaborators;

        public List<Collaborator> getCollaborator() {
            return collaborators;
        }

        public void setCollaborator(List<Collaborator> collablist) {
            this.collaborators = collablist;
        }
    }

}
