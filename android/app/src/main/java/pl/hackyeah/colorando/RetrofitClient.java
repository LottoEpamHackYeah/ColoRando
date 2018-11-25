package pl.hackyeah.colorando;

import retrofit2.Retrofit;

public class RetrofitClient {

    public static Retrofit getInstance(String url) {
        return new Retrofit.Builder().baseUrl(url).build();
    }
}
