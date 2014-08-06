package com.scubbo.mtgmatcher.server;

import com.scubbo.mtgmatcher.TestHelper;
import com.scubbo.mtgmatcher.server.GetPlayersJSONResponse;
import org.json.JSONException;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertTrue;

public class GetPlayersResponseTest {

    @Test
    public void getResponseTest() throws JSONException {
        URL url = TestHelper.makeRequestURL("getPlayers");
        try {
            final String response = TestHelper.getResponseFromUrl(url);
            GetPlayersJSONResponse jsonResponse = new GetPlayersJSONResponse(response);
            assertTrue(jsonResponse.isSuccess());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
