package com.aceventura.voicerecoder.Retrofit.Modals;

public class EventModel {
  String name,startTime,endTime,promoter,venue,artists;
  int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPromoter() {
        return promoter;
    }

    public void setPromoter(String promoter) {
        this.promoter = promoter;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EventModel(String name, String startTime, String endTime, String promoter, String venue, String artists, int id) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.promoter = promoter;
        this.venue = venue;
        this.artists = artists;
        this.id = id;
    }
}
