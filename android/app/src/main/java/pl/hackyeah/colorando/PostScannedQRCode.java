package pl.hackyeah.colorando;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PostScannedQRCode {

    @POST("qrcode")
    Call<ResponseBody> postQRCode(@Body RequestBody body);
}
