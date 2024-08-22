package edu.escuelaing.arep;

import java.util.HashMap;
import java.util.Map;

public class HelloWorld {
    public static Map<String, Service> services = new HashMap();

    
    public static void main(String[] args) {
        get("/hello", (req, resp) -> "Hello "+req);
        get("/pi", (req, resp) -> {return String.valueOf(Math.PI);});

        String requestedUrl = "/app/hello?name=Pedro";
        // Metodos necesarios = get, queryParama (request.queryParams("name")) y un metodo adicional que le diga al programa donde se va a poner el contenido estatico 
        System.out.println(services.get("/pi").getValue("",""));
        System.out.println(services.get("/hello").getValue("", ""));
        
    }

    public static void get(String url, Service s){
        services.put(url, s);
    }
}