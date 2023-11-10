package com.aceventura.voicerecoder;

import com.aceventura.voicerecoder.Retrofit.ApiInterface;
import com.aceventura.voicerecoder.Retrofit.Modals.UploadMod;
import com.aceventura.voicerecoder.Retrofit.WebApi;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitTest {

    private String FILE_PATH = "src/test/java/com/aceventura/voicerecoder/ExampleUnitTest.java";
    private String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4eHgxMjMiLCJpc3MiOiJub3dvbiIsImV4cCI6MTY5OTYzNjYxN30.52ZG-xdYGOCruW3WfTuLZJehm_909qI56IJx1giZ_nQicztacWKlR3GGhNS-ewahiQ31KXk3pdD7t4vIoCe3aQ";

    @Test
    public void checksumMismatch() throws IOException {
        ApiInterface apiInterface = getApiInterface();
        MultipartBody.Part file = filePart();
        RequestBody eventInfoPart = eventInfoPart("checksum");

        Response<UploadMod> execute = apiInterface.Upload(TOKEN, eventInfoPart, file).execute();

        Assert.assertEquals(400, execute.raw().code());
        Assert.assertEquals("{\"message\":\"Checksum mismatch\"}", execute.errorBody().string());
    }

    @Test
    public void upload() throws IOException {
        ApiInterface apiInterface = getApiInterface();
        MultipartBody.Part body = filePart();
        RequestBody eventInfoPart = eventInfoPart("290edf9839ce816fc4a91a88b2f83665");

        Response<UploadMod> execute = apiInterface.Upload(TOKEN, eventInfoPart, body).execute();

        Assert.assertEquals(200, execute.raw().code());
        Assert.assertEquals("Event data successfully uploaded!", execute.body().getMessage());
    }

    private RequestBody eventInfoPart(String checksum) {
        String json = "{\"eventId\": 1698573805068, \"checksum\": \""+checksum+"\"}";
        RequestBody eventInfo = RequestBody.create(json, MediaType.parse("application/json"));
        return eventInfo;
    }

    private ApiInterface getApiInterface() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiInterface.class);
    }

    private MultipartBody.Part filePart() {
        File file = new File(FILE_PATH);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return body;
    }


}
