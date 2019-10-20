package com.lifetime.google_map_api_mvvm_retrofit.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://route.api.here.com/routing/7.2/";

    public static Retrofit getRetrofitInstance(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor(){

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original  = chain.request();

                Request request = original.newBuilder()
                        .method(original.method(),original.body())
                        .build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        if(retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
