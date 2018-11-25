package pl.hackyeah.colorando;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    public static Retrofit getInstance(String url) {
        return new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
}
