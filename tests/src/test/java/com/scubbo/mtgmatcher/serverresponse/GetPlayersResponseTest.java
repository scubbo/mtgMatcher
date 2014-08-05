package com.scubbo.mtgmatcher.serverresponse;

import com.scubbo.mtgmatcher.TestHelper;
import com.scubbo.mtgmatcher.responses.JSONResponse;
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
            JSONResponse jsonResponse = new JSONResponse(response);
            assertTrue(jsonResponse.isSuccess());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
