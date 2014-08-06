package com.scubbo.mtgmatcher.server.dataclasses;

import com.scubbo.mtgmatcher.server.dataclasses.Player;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class PlayerTest {

    private static final String happyJSONString = "{\"name\":\"myName\", \"dciNumber\":\"myDCINumber\"}"; //Note this doesn't care about order
    private final String invalidJSONString = "Not a real invalidJSONString";

    @Test
    public void happyCase() {
       Player player = new Player("myDCINumber", "myName");
    }

    @Test
    public void happyCaseJSONString() throws JSONException {
        Player player = new Player(happyJSONString);
    }

    @Test
    public void happyCaseJSONObject() throws JSONException {
        Player player = new Player(new JSONObject(happyJSONString));
    }

    @Test(expected=JSONException.class)
    public void invalidJSONString() throws JSONException {
        Player player = new Player(invalidJSONString);
    }

    @Test(expected=JSONException.class)
    public void invalidJSONObject() throws JSONException {
        Player player = new Player(new JSONObject(invalidJSONString));
    }

    @Test(expected=JSONException.class)
    public void missingName() throws JSONException {
        Player player = new Player("{\"dciNumber\":\"myDCINumber\"}");
    }
}
