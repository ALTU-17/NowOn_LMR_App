package com.aceventura.voicerecoder.Retrofit.Modals;

public class TempMod {
    private String deviceSecret;
    private String imei;

//    public TempMod(String deviceSecret, String imei) {
//        this.deviceSecret = deviceSecret;
//        this.imei = imei;
//    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public void setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
