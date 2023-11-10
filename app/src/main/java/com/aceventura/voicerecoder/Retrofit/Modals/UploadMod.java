package com.aceventura.voicerecoder.Retrofit.Modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadMod {

    @Expose
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
