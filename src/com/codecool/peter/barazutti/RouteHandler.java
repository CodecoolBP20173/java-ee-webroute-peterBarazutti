package com.codecool.peter.barazutti;

import com.sun.net.httpserver.HttpExchange;

public class RouteHandler {

    @WebRoute("/test")
    public String testOne(HttpExchange t) {
        return "This is a test page!";
    }

    @WebRoute("/another-test")
    public String testTwo(HttpExchange t) {
        return "Another test page!";
    }
}
