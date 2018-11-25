package pl.hackyeah.colorando;

import pl.hackyeah.colorando.repository.dto.CodeResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PostScannedQRCode {

    @GET("getGameMethadata")
    Call<CodeResult> postQRCode(@Query("gameId") String gameId, @Query("locationId") String locationId);
}
