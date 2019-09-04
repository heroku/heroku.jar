package com.heroku.api.parser;

import com.heroku.api.App;
import com.heroku.api.Heroku;
import com.heroku.api.request.app.AppCreate;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

/**
 * @author Joe Kutner
 */
public class JsonTest {
  @Test
  public void parseRequest() {
    App app = new App().named("foobar").on(Heroku.Stack.Heroku18);
    AppCreate request = new AppCreate(app);

    String json = request.getBody();
    App deserializedApp = Json.parse(json.getBytes(), App.class);

    assertEquals(deserializedApp.getName(), "foobar");
  }
}