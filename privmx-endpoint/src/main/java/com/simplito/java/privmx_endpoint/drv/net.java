package com.simplito.java.privmx_endpoint.drv;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import okhttp3.Address;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class net {

    public static class HttpSession {
        private final OkHttpClient client = new OkHttpClient();
        private final Address baseAddress;

        public HttpSession(String uri) {
            baseAddress = client.address(Objects.requireNonNull(HttpUrl.parse(uri)));
        }

        private HttpResponse makeRequest(
                byte[] data,
                privmxDrvNet_HttpRequestOptions options
        ) throws IOException {
            RequestBody body = null;
            if (data != null) {
                body = RequestBody.create(data,MediaType.parse(options.contentType));
            }
            Headers.Builder builder = new Headers.Builder();
            options.headers.forEach(header -> {
                builder.add(header.name, header.value);
            });
            Request request = new Request.Builder()
                    .url(Objects.requireNonNull(baseAddress.url().resolve(options.path)))
                    .method(options.method, body)
                    .headers(builder.build())
                    .build();
            //TODO: Add catching exception and throw on java thread or throw to privmx lib as error of connection
            Response response = client.newCall(request).execute();
            HttpResponse httpResponse = new HttpResponse(
                    response.code(),
                    response.body().bytes()
            );
            response.close();
            return httpResponse;
        }

        private void destroySession() {
        }

    }

    static class HttpResponse{
        int statusCode;
        byte[] out;
        HttpResponse(
                int statusCode,
                byte[] out
        ){
            this.statusCode = statusCode;
            this.out = out;
        }
        int length(){
            return out.length;
        }
    }

    static class WebSocket {
        private final OkHttpClient client = new OkHttpClient();
        private final Address baseAddress;
        private okhttp3.WebSocket webSocket = null;

        WebSocket(String uri) {
            baseAddress = client.address(Objects.requireNonNull(HttpUrl.parse(uri)));
        }

        void connect(
                privmxDrvNet_WsOptions options,
                long listener
        ) {
            Request request = new Request.Builder().url(Objects.requireNonNull(baseAddress.url())).build();
            webSocket = client.newWebSocket(
                    request,
                    new WebsocketListener(listener)
            );

        }

        void disconnect() {
//            webSocket.cancel();
            webSocket.close(1000, "");
            System.out.println("websocket closed");
        }

        void send(byte[] data) {
            webSocket.send(ByteString.of(data));
        }

        static class WebsocketListener extends WebSocketListener {
            private long listener_ptr;

            WebsocketListener(long listener_ptr) {
                this.listener_ptr = listener_ptr;
            }

            public void onOpen(@NotNull okhttp3.WebSocket webSocket, @NotNull Response response) {
                onOpen();
            }

            public void onMessage(@NotNull okhttp3.WebSocket webSocket, @NotNull String text) {
                onMessage(text.getBytes(StandardCharsets.UTF_8));
            }

            public void onMessage(@NotNull okhttp3.WebSocket webSocket, @NotNull ByteString bytes) {
                onMessage(bytes.toByteArray());
            }

            public void onClosed(@NotNull okhttp3.WebSocket webSocket, int code, @NotNull String reason) {
                //TODO: select correct flag
                System.out.println("websocket closed");
                onClose(true);
            }

            public void onFailure(@NotNull okhttp3.WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                onError(t.getMessage());
            }

            private native void onOpen();

            private native void onMessage(byte[] message);

            private native void onError(String message);

            private native void onClose(boolean wasClean);
        }
    }

    static class privmxDrvNet_HttpOptions {
        String baseUrl;
        boolean keepAlive;

        privmxDrvNet_HttpOptions(String baseUrl, boolean keepAlive){
            this.baseUrl = baseUrl;
            this.keepAlive = keepAlive;
        }
    }

    static class privmxDrvNet_HttpHeader {
        String name;
        String value;

        privmxDrvNet_HttpHeader(String name, String value){
            this.name = name;
            this.value = value;
        }
    }

    static class privmxDrvNet_HttpRequestOptions {
        String path;
        String method; // methods: GET, POST
        String contentType;
        List<privmxDrvNet_HttpHeader> headers;
        int headerslen;
        boolean keepAlive;

        privmxDrvNet_HttpRequestOptions(
                String path,
                String method, // methods: GET, POS,
                String contentType,
                List<privmxDrvNet_HttpHeader> headers,
                int headerslen,
                boolean keepAlive
        ) {
            this.path = path;
            this.method = method;
            this.contentType = contentType;
            this.headers = headers;
            this.headerslen = headerslen;
            this.keepAlive = keepAlive;
        }
    }

    static class privmxDrvNet_WsOptions {
        String url;

        privmxDrvNet_WsOptions(String url){
            this.url = url;
        }
    }

}


