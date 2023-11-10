package com.aceventura.voicerecoder.Retrofit.Modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterMod {

    @Expose
    @SerializedName("imei")
    private String imei;
    @Expose
    @SerializedName("model")
    private String model;
    @Expose
    @SerializedName("buildNumber")
    private String buildNumber;
    @Expose
    @SerializedName("androidVersion")
    private String androidVersion;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public RegisterMod(String imei, String model, String buildNumber, String androidVersion) {
        this.imei = imei;
        this.model = model;
        this.buildNumber = buildNumber;
        this.androidVersion = androidVersion;
    }

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("secret")
    private String secret;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
