package com.simplito.java.privmx_endpoint_streams.model;

import com.simplito.java.privmx_endpoint.model.Frame;

@FunctionalInterface
public interface OnFrameCallback {
 void run(long a, long b, Frame c, String d);
}
