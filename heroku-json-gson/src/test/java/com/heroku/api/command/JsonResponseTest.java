package com.heroku.api.command;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.heroku.api.command.response.JsonArrayResponse;
import com.heroku.api.command.response.JsonMapResponse;
import com.heroku.api.command.response.XmlMapResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class JsonResponseTest {
    @Test
    public void testArrayOfJsonElementsMapsToHerokuCommandMap() {
        String jsonArray = "[{\"name\":\"warm-frost-3139\",\"stack\":\"cedar\"},{\"name\":\"vivid-summer-2426\",\"stack\":\"cedar\"}]";
        JsonArrayResponse res = new JsonArrayResponse(jsonArray.getBytes());
        Assert.assertNotNull(res.get("warm-frost-3139"));
    }

    @Test
    public void testSingleJsonElementMapsToHerokuCommandMap() {
        String json = "{\"name\":\"warm-frost-3139\",\"stack\":\"cedar\"}";
        JsonMapResponse res = new JsonMapResponse(json.getBytes());
        Assert.assertEquals(res.get("name"), "warm-frost-3139");
    }

    @Test(enabled = false)
    public void testLongAssJsonResponse() {
        String json = "\n" +
                "[\n" +
                "  {\n" +
                "    \"name\": \"devcenter\",\n" +
                "    \"git_url\": \"git@heroku.com:devcenter.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 37822464,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/02/24 12:10:18 -0800\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 453931,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 49696768,\n" +
                "    \"web_url\": \"http://devcenter.heroku.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"ion-mhale\",\n" +
                "    \"git_url\": \"git@heroku.com:ion-mhale.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 34377728,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/04/11 08:39:50 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 497985,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 37670912,\n" +
                "    \"web_url\": \"http://ion-mhale.heroku.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"codon-production-cedar\",\n" +
                "    \"git_url\": \"git@heroku.com:codon-production-cedar.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 44822528,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/06/29 19:20:37 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 584093,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 15093760,\n" +
                "    \"web_url\": \"http://codon-production-cedar.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"shouter\",\n" +
                "    \"git_url\": \"git@heroku.com:shouter.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 12197888,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/07/01 06:52:14 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 585663,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 7491584,\n" +
                "    \"web_url\": \"http://shouter.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"andvari-production\",\n" +
                "    \"git_url\": \"git@heroku.com:andvari-production.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 9605120,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/07/08 02:05:54 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 593338,\n" +
                "    \"domain_name\": {\n" +
                "      \"created_at\": \"2011/09/22 12:40:06 -0700\",\n" +
                "      \"updated_at\": \"2011/09/22 12:40:06 -0700\",\n" +
                "      \"default\": null,\n" +
                "      \"domain\": \"shogun-wat.heroku.com\",\n" +
                "      \"id\": 180317,\n" +
                "      \"app_id\": 593338,\n" +
                "      \"base_domain\": \"heroku.com\"\n" +
                "    },\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 7548928,\n" +
                "    \"web_url\": \"http://andvari-production.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"codon-staging-cedar\",\n" +
                "    \"git_url\": \"git@heroku.com:codon-staging-cedar.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 44822528,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/07/14 14:07:00 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 607975,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 15093760,\n" +
                "    \"web_url\": \"http://codon-staging-cedar.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"toolbelt\",\n" +
                "    \"git_url\": \"git@heroku.com:toolbelt.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 7389184,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/08/17 15:39:17 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 780465,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 8204288,\n" +
                "    \"web_url\": \"http://toolbelt.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"leech-production\",\n" +
                "    \"git_url\": \"git@heroku.com:leech-production.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 3289088,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/09/11 11:57:41 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 976122,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 11116544,\n" +
                "    \"web_url\": \"http://leech-production.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"clojars\",\n" +
                "    \"git_url\": \"git@heroku.com:clojars.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 19722240,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/10/07 12:10:53 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 1381369,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 24768512,\n" +
                "    \"web_url\": \"http://clojars.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"shadout-mapes\",\n" +
                "    \"git_url\": \"git@heroku.com:shadout-mapes.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 31490048,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/10/10 14:14:36 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 1419668,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 28061696,\n" +
                "    \"web_url\": \"http://shadout-mapes.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"codon-pnh\",\n" +
                "    \"git_url\": \"git@heroku.com:codon-pnh.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 46882816,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/10/11 15:55:30 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 1433221,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 15728640,\n" +
                "    \"web_url\": \"http://codon-pnh.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"camper-van\",\n" +
                "    \"git_url\": \"git@heroku.com:camper-van.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 7684096,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/10/12 13:12:56 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 1444304,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 1671168,\n" +
                "    \"web_url\": \"http://camper-van.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"pnh-znc\",\n" +
                "    \"git_url\": \"git@heroku.com:pnh-znc.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 2568192,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/10/18 11:51:01 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 1513252,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 1179648,\n" +
                "    \"web_url\": \"http://pnh-znc.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"swank\",\n" +
                "    \"git_url\": \"git@heroku.com:swank.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 1671168,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/10/20 17:53:20 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 1540782,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 1585152,\n" +
                "    \"web_url\": \"http://swank.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"commodore-night-vision\",\n" +
                "    \"git_url\": \"git@heroku.com:commodore-night-vision.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 45916160,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/10/29 13:44:15 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 1640085,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 258048,\n" +
                "    \"web_url\": \"http://commodore-night-vision.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"melange-pnh\",\n" +
                "    \"git_url\": \"git@heroku.com:melange-pnh.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": null,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/11/02 11:36:24 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 1679192,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": null,\n" +
                "    \"web_url\": \"http://melange-pnh.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"shouter-pnh\",\n" +
                "    \"git_url\": \"git@heroku.com:shouter-pnh.git\",\n" +
                "    \"stack\": \"cedar\",\n" +
                "    \"slug_size\": 13635584,\n" +
                "    \"requested_stack\": null,\n" +
                "    \"created_at\": \"2011/11/03 15:56:56 -0700\",\n" +
                "    \"create_status\": \"complete\",\n" +
                "    \"id\": 1691756,\n" +
                "    \"domain_name\": null,\n" +
                "    \"repo_migrate_status\": \"complete\",\n" +
                "    \"repo_size\": 9641984,\n" +
                "    \"web_url\": \"http://shouter-pnh.herokuapp.com/\",\n" +
                "    \"dynos\": 0,\n" +
                "    \"workers\": 0\n" +
                "  }\n" +
                "]";
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Object>>() {
        }.getType();
//        Gson gson = new GsonBuilder().registerTypeAdapter(listType, new JsonDeserializer<List<Object>>() {
//            @Override
//            public List<Object> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//
//                return null;  //To change body of implemented methods use File | Settings | File Templates.
//            }
//        }).create();
        JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
        jsonArray.hashCode();
        JsonObject parsed = gson.fromJson(json, JsonObject.class);
        parsed.hashCode();
//        JsonArrayResponse response = new JsonArrayResponse(json.getBytes());
    }
}
