package com.example.protoui.travelmode.route.lyft.data;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LyftApiBuilder {

    private final ApiConfig apiConfig;

    public LyftApiBuilder(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    /**
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link retrofit2.Call}. Used by the LyftButton.
     * See: <a href="http://petstore.swagger.io/?url=https://api.lyft.com/v1/spec#!/Public/">http://petstore.swagger.io/?url=https://api.lyft.com/v1/spec#!/Public/</a>
     */
    public LyftApi build() {
        Retrofit retrofitPublicApi = getRetrofitBuilder().build();
        return retrofitPublicApi.create(LyftApi.class);
    }

    private Retrofit.Builder getRetrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(LyftApi.API_ROOT)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create());
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor(apiConfig.getClientToken()))
                .build();
    }
}
