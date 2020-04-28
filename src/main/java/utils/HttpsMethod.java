package utils;

public enum HttpsMethod {
    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    DELETE("DELETE");

    private String method;
    HttpsMethod(String in) {
        this.method = in;
    }
}
