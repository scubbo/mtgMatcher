package com.scubbo.mtgmatcher;

import org.junit.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

public class ServerSetupTest {

    private static final Logger logger = Logger.getLogger(ServerSetupTest.class.getName());

    private static final String TAG = "In-Testing TAG";

    private static final Integer testingPortNumber = 2021;

    @Test
    public void hitLocalhost() {
        String urlAsString = "http://localhost:" + testingPortNumber.toString() + "/actions/private/getPlayers.py";
        URL url = null;
        try {
            url = new URL(urlAsString);
            System.out.println(getResponseFromUrl(url)); //TODO: check this properly
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fakeTest() {
        //fail();
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
