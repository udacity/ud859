package com.google.training.helloworld;

public class HelloClass {
    public String message = "Hello World";

    public HelloClass () {
    }

    public HelloClass (String name) {
        this.message = "Hello " + name + "!";
    }

    public HelloClass (String name, String period) {
        this.message = "Good " + period + " " + name + "!";
    }

    public String getMessage() {
        return message;
    }
}
