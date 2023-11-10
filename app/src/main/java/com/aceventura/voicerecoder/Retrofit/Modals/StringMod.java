package com.aceventura.voicerecoder.Retrofit.Modals;

import com.google.gson.annotations.SerializedName;

public class StringMod {


    @SerializedName("eventId")
    private long eventId;

    @SerializedName("checksum")
    private String checksum;

    public StringMod(long eventId, String checksum) {
        this.eventId = eventId;
        this.checksum = checksum;
    }


}
