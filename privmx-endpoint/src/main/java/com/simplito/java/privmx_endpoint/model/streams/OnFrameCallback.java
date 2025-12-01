package com.simplito.java.privmx_endpoint.model.streams;

@FunctionalInterface
public interface OnFrameCallback {
 void run(long width, long height, Frame frame, String id);
}