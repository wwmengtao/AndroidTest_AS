package com.example.protoui.travelmode.route.uber.data;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UberApiBuilder {

    private final ApiConfig apiConfig;

    public UberApiBuilder(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    /**
     * @return An implementation of Lyft's Public API endpoints that do not require a user.
     * The return type of API calls will be {@link retrofit2.Call}. Used by the LyftButton.
     * See: <a href="http://petstore.swagger.io/?url=https://api.lyft.com/v1/spec#!/Public/">http://petstore.swagger.io/?url=https://api.lyft.com/v1/spec#!/Public/</a>
     */
    public UberApi build() {
        Retrofit retrofitPublicApi = getRetrofitBuilder().build();
        return retrofitPublicApi.create(UberApi.class);
    }

    private Retrofit.Builder getRetrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(UberApi.API_ROOT)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create());
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor(apiConfig.getClientToken()))
                .build();
    }
}
