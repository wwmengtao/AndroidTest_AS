package com.example.protoui.travelmode.route.uber.data;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class RequestInterceptor implements Interceptor {

    static final String AUTHORIZATION_HEADER = "Authorization";
    static final String USER_AGENT_HEADER = "User-Agent";
    private static final String USER_AGENT = "uber-mobile-sdk:android::1.0.1";
    private final String clientToken;

    RequestInterceptor(String clientToken) {
        this.clientToken = clientToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder()
                .addHeader(AUTHORIZATION_HEADER, "Token " + clientToken)
                .addHeader(USER_AGENT_HEADER, USER_AGENT);
        request = requestBuilder.build();
        return chain.proceed(request);
    }
}
