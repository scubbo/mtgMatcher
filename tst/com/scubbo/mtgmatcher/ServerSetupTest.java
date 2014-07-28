package com.scubbo.mtgmatcher;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class ServerSetupTest {

    private static final Logger logger = Logger.getLogger(ServerSetupTest.class.getName());

    private static final String TAG = "In-Testing TAG";
    private static Process serverProcess;

    private static final Integer testingPortNumber = 2021;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Process process;
        try {
            //TODO: Don't hardcode directory
            process = Runtime.getRuntime()
                    .exec("./startServer.py --port " +
                            testingPortNumber.toString(),
                            null, //no env variables
                            new File("/Users/jackjack/Code/mtgMatcher/src/server/webdocs/"));
            Thread.sleep(1000);
            try {
                process.exitValue(); //we should not be able to get this, because the server should be running
                //TODO: Neaten this up - perhaps make it work on isAlive() ?
                throw new Exception("The server did not start correctly");
            } catch (IllegalThreadStateException e) {
                serverProcess = process;
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @AfterClass
    public static void tearDownClass() {
        if (!(serverProcess == null)) {
            logger.info("Tearing down class");
            serverProcess.destroyForcibly();
        }
    }

    @Test
    public void hitLocalhost() {
        String urlAsString = "http://localhost:" + testingPortNumber.toString() + "/actions/private/getPlayers.py";
        URL url = null;
        try {
            url = new URL(urlAsString);
            System.out.println(getResponseFromUrl(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fakeTest() {

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
