package edu.escuelaing.arep;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private Map<String, String> queryParams;

    // Constructor que toma un mapa con los parámetros de consulta
    public Request(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    // Constructor que analiza una cadena de consulta
    public Request(String queryString) {
        this.queryParams = parseQueryString(queryString);
    }

    public String getQueryParam(String key) {
        String value = queryParams.get(key);
        return value != null ? URLDecoder.decode(value, StandardCharsets.UTF_8) : null;
    }

    // Método para analizar la cadena de consulta y llenar el mapa de parámetros
    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString != null && !queryString.isEmpty()) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                String key = keyValue[0];
                String value = keyValue.length > 1 ? keyValue[1] : "";
                params.put(key, value);
            }
        }
        return params;
    }
}
