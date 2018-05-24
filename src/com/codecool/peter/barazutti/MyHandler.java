package com.codecool.peter.barazutti;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MyHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        String requestUrl = t.getRequestURI().getPath();
        String response = null;
        Method[] methods = this.getClass().getMethods();

        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();

            for (Annotation annotation : annotations) {

                if (annotation instanceof WebRoute) {

                    if (((WebRoute) annotation).value().equals(requestUrl)
                            && ((WebRoute) annotation).method().equals((t.getRequestMethod()))) {
                        try {
                            response = (String) method.invoke(this, t);
                            break;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            response = "Error: IllegalAccessException";
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            response = "Error: InvocationTargetException";
                        }
                    }
                }

            }
        }
        if (response == null) {
            response = "Wrong address!";
            t.sendResponseHeaders(404, response.getBytes().length);
        } else {
            t.sendResponseHeaders(200, response.getBytes().length);
        }
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @WebRoute("/test")
    public String testOne(com.sun.net.httpserver.HttpExchange t) {
        return "This is a test page!";
    }

    @WebRoute("/another-test")
    public String testTwo(com.sun.net.httpserver.HttpExchange t) {
        return "Another test page!";
    }

}
