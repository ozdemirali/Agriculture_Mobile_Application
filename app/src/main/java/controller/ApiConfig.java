package controller;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiConfig {
    @Multipart
    @POST("api/Upload")
    Call uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);
}
