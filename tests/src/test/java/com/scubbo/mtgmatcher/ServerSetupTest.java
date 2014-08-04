package com.scubbo.mtgmatcher;

import com.scubbo.mtgmatcher.responses.JSONResponse;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import org.json.JSONException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ServerSetupTest {

    private static final Logger logger = Logger.getLogger(ServerSetupTest.class.getName());

    private static final String TAG = "In-Testing TAG";

    private static final Integer testingPortNumber = 2021;

    @Test
    public void hitLocalhost() throws IOException, JSONException {
        String urlAsString = "http://localhost:" + testingPortNumber.toString() + "/actions/private/getPlayers.py";
        URL url = new URL(urlAsString);
        final String responseFromUrl = getResponseFromUrl(url);

        JSONResponse jsonResponse = new JSONResponse(responseFromUrl);
        assertTrue(jsonResponse.isSuccess());
    }

    private static String getResponseFromUrl(URL url) throws IOException {
        String response = "";

        URLConnection connection = url.openConnection();
        DataInputStream dis = new DataInputStream(connection.getInputStream());
        String inputLine;

        while ((inputLine = dis.readLine()) != null) {
            response += inputLine + "\r\n";
        }
        dis.close();

        return response;
    }

}
