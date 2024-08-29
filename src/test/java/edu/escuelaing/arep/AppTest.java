package edu.escuelaing.arep;

import junit.framework.TestCase;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppTest extends TestCase {

    private static final String SERVER_URL = "http://localhost:8080/";
    private static Thread serverThread;
    private static ExecutorService executorService;
    private static boolean isServerStarted = false;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if (!isServerStarted) {
            startServer();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void finalize() throws Throwable {
        stopServer(); 
        super.finalize();
    }

    private static void startServer() throws Exception {
        executorService = Executors.newFixedThreadPool(5);
        serverThread = new Thread(() -> {
            try {
                SimpleWebServer.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        boolean isServerUp = false;
        int retries = 10;
        while (retries > 0 && !isServerUp) {
            try {
                URL url = new URL(SERVER_URL + "index.html");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    isServerUp = true;
                }
                conn.disconnect();
            } catch (Exception e) {
                Thread.sleep(1000); 
            }
            retries--;
        }

        if (!isServerUp) {
            throw new IllegalStateException("El servidor no se pudo iniciar correctamente.");
        }

        isServerStarted = true; 
    }

    private static void stopServer() throws Exception {
        SimpleWebServer.stop(); 
        executorService.shutdownNow(); 
        if (serverThread != null) {
            serverThread.join(5000); 
        }
        isServerStarted = false;
    }

    public void testHelloServiceResponse() throws Exception {
        executorService.submit(() -> {
            try {
                URL url = new URL(SERVER_URL + "app/hello?name=World");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                conn.disconnect();
                assertTrue(content.toString().contains("{\"nombre\": \"World\"}"));
                assertTrue(content.toString().contains("Hola, World"));
            } catch (Exception e) {
                e.printStackTrace();
                fail("Conexión fallida: " + e.getMessage());
            }
        }).get();  // Espera a que la tarea se complete
    }

    public void testLoadStaticFile() throws Exception {
        executorService.submit(() -> {
            try {
                URL url = new URL(SERVER_URL + "index.html");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                assertEquals(200, responseCode);
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                fail("Conexión fallida: " + e.getMessage());
            }
        }).get();  
    }

    public void testInvalidRequest() throws Exception {
        executorService.submit(() -> {
            try {
                URL url = new URL(SERVER_URL + "nonexistentfile.html");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                assertEquals(404, responseCode);
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                fail("Conexión fallida: " + e.getMessage());
            }
        }).get();  
    }

    public void testPostRequest() throws Exception {
        executorService.submit(() -> {
            try {
                URL url = new URL(SERVER_URL + "app/hellopost?name=Maria%20Jose");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                conn.disconnect();
                assertTrue(content.toString().contains("Post received: Maria Jose"));
            } catch (Exception e) {
                e.printStackTrace();
                fail("Conexión fallida: " + e.getMessage());
            }
        }).get();  
    }

    public void testMultipleConnections() throws Exception {
        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> {
                try {
                    URL url = new URL(SERVER_URL + "index.html");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    int responseCode = conn.getResponseCode();
                    assertEquals(200, responseCode);
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Conexión fallida: " + e.getMessage());
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
