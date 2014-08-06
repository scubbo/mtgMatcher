package com.scubbo.mtgmatcher.serverresponse;

import com.scubbo.mtgmatcher.TestHelper;
import com.scubbo.mtgmatcher.responses.JSONResponse;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import org.json.JSONException;

import static org.junit.Assert.assertTrue;

public class ServerSetupTest {

    private static final Logger logger = Logger.getLogger(ServerSetupTest.class.getName());

    private static final Integer testingPortNumber = 2021;

    @Test
    public void hitLocalhost() throws IOException, JSONException {
        String urlAsString = "http://localhost:" + testingPortNumber.toString() + "/actions/private/getPlayers.py";
        URL url = new URL(urlAsString);
        final String responseFromUrl = TestHelper.getResponseFromUrl(url);

        JSONResponse jsonResponse = new JSONResponse(responseFromUrl);
        assertTrue(jsonResponse.isSuccess());
    }

}
