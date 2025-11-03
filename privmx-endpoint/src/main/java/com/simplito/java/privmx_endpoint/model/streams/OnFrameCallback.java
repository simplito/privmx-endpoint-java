package com.simplito.java.privmx_endpoint.model.streams;

@FunctionalInterface
public interface OnFrameCallback {
 void run(long a, long b, Frame c, String d);
}
