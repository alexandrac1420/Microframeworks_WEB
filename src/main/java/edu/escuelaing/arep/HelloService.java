package edu.escuelaing.arep;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class HelloService implements RestService {

    @Override
    public String response(Request req, Response resp) {
        String name = req.getQueryParam("name");
        System.out.println(name);
        String decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8);

        // Formato JSON
        String jsonResponse = "{\"nombre\": \"" + decodedName + "\"}";

        // Texto plano
        String plainTextResponse = "Hola, " + decodedName;

        // Concatenar ambos formatos
        return jsonResponse + "<br />" + plainTextResponse;
    }
}
