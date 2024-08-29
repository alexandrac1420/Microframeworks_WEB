package edu.escuelaing.arep;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class SimpleWebServer {
    private static final int PORT = 8080;
    public static String WEB_ROOT;
    public static Map<String, RestService> services = new HashMap<>();
    private static boolean running = true;

    public static void main(String[] args) throws IOException {
        SimpleWebServer.staticfiles("src/main/java/edu/escuelaing/arep/resources/");
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Ready to receive on port " + PORT + "...");
        addServices();
        while (running) {
            Socket clientSocket = serverSocket.accept();
            threadPool.submit(new ClientHandler(clientSocket));
        }
        serverSocket.close();
        threadPool.shutdown();
    }

    private static void addServices() {
        // Agregar servicios REST
        get("/app/hello", new HelloService());
        get("/app/pi", (req, resp) -> String.valueOf(Math.PI));
        post("/app/hellopost", (req, resp) -> "Post received: " + req.getQueryParam("name"));
    }

    // Método para registrar servicios REST
    public static void get(String url, RestService s) {
        services.put(url, s);
    }

    public static void post(String url, RestService s) {
        services.put(url, s);
    }

    // Método para especificar la ubicación de archivos estáticos
    public static void staticfiles(String location) {
        File directory = new File(location);
        if (directory.exists() && directory.isDirectory()) {
            WEB_ROOT = location;
            System.out.println("Static files location set to: " + WEB_ROOT);
        } else {
            throw new IllegalArgumentException("The specified path does not exist or is not a directory: " + location);
        }
    }

    public static void stop() {
        running = false;
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream())) {

            String requestLine = in.readLine();
            if (requestLine == null)
                return;
            String[] tokens = requestLine.split(" ");
            String method = tokens[0];
            String fileRequested = tokens[1];

            printRequestLine(requestLine, in);

            if (fileRequested.startsWith("/app")) {
                handleAppRequest(method, fileRequested, out);
            } else {
                if (method.equals("GET")) {
                    handleGetRequest(fileRequested, out, dataOut);
                } else if (method.equals("POST")) {
                    handlePostRequest(fileRequested, out, dataOut);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close(); // Cerrando el socket aquí, después de procesar la solicitud
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printRequestLine(String requestLine, BufferedReader in) {
        System.out.println("Request line: " + requestLine);
        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
                System.out.println("Header: " + inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGetRequest(String fileRequested, PrintWriter out, BufferedOutputStream dataOut)
            throws IOException {
        File file = new File(SimpleWebServer.WEB_ROOT, fileRequested);
        int fileLength = (int) file.length();
        String content = getContentType(fileRequested);

        if (file.exists()) {
            byte[] fileData = readFileData(file, fileLength);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-type: " + content);
            out.println("Content-length: " + fileLength);
            out.println();
            out.flush();
            dataOut.write(fileData, 0, fileLength);
            dataOut.flush();
        } else {
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-type: text/html");
            out.println();
            out.flush();
            out.println("<html><body><h1>File Not Found</h1></body></html>");
            out.flush();
        }
    }

    private void handlePostRequest(String fileRequested, PrintWriter out, BufferedOutputStream dataOut)
            throws IOException {
        StringBuilder payload = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                payload.append(line);
            }
        }
        out.println("HTTP/1.1 200 OK");
        out.println("Content-type: text/html");
        out.println();
        out.println("<html><body><h1>POST data received:</h1>");
        out.println("<p>" + payload.toString() + "</p>");
        out.println("</body></html>");
        out.flush();
    }

    private void handleAppRequest(String method, String fileRequested, PrintWriter out) {
        // Extrae la parte de la URL del servicio sin los parámetros de consulta
        String serviceRequest = fileRequested.contains("?") ? fileRequested.substring(0, fileRequested.indexOf("?"))
                : fileRequested;

        // Extrae los parámetros de la consulta
        String queryString = fileRequested.contains("?") ? fileRequested.split("\\?")[1] : "";
        Request req = new Request(queryString); // Utiliza el constructor adecuado
        Response resp = new Response();

        // Llama al servicio REST correspondiente
        if (SimpleWebServer.services.containsKey(serviceRequest)) {
            RestService service = SimpleWebServer.services.get(serviceRequest);
            String response = service.response(req, resp);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-type: text/html");
            out.println();
            out.println(response);
        } else {
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-type: text/html");
            out.println();
            out.println("<html><body><h1>Service Not Found</h1></body></html>");
        }
        out.flush();
    }

    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".html"))
            return "text/html";
        else if (fileRequested.endsWith(".css"))
            return "text/css";
        else if (fileRequested.endsWith(".js"))
            return "application/javascript";
        else if (fileRequested.endsWith(".png"))
            return "image/png";
        else if (fileRequested.endsWith(".jpg"))
            return "image/jpeg";
        return "text/plain";
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }
        return fileData;
    }
}
