package com.scubbo.mtgmatcher;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {
    public static final Integer TESTING_PORT_NUMBER = 2021;

    public static final String LOCAL_HOST_URL = "http://localhost:" + TESTING_PORT_NUMBER.toString();

    //TODO: Implement these as an enum
    public static final List<String> PRIVATE_ENDPOINTS = new ArrayList<String>() {{
       add("getPlayers");
    }};

    public static final List<String> PUBLIC_ENDPOINTS = new ArrayList<String>() {{
        add("registerPlayer");
    }};

    public static URL makeRequestURL(String endpoint) {
        try {
            if (PRIVATE_ENDPOINTS.contains(endpoint)) {
                return new URL(LOCAL_HOST_URL + "/actions/private/" + endpoint + ".py");
            }
            if (PUBLIC_ENDPOINTS.contains(endpoint)) {
                return new URL(LOCAL_HOST_URL + "/actions/public/" + endpoint + ".py");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("No such endpoint " + endpoint + " known");
    }

    public static String getResponseFromUrl(URL url) {
        String response = "";

        try {
            URLConnection connection = url.openConnection();
            DataInputStream dis = new DataInputStream(connection.getInputStream());
            String inputLine;

            //TODO: http://stackoverflow.com/questions/5611387/datainputstream-deprecated-readline-method
            while ((inputLine = dis.readLine()) != null) {
                response += inputLine + "\r\n";
            }
            dis.close();

            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
