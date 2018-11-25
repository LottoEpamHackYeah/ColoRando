package pl.hackyeah.colorando;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostScannedQRCode {

    @GET("qrcode")
    Call<String> postQRCode(@Query("gameId") String gameId, @Query("locationId") String locationId);
}
