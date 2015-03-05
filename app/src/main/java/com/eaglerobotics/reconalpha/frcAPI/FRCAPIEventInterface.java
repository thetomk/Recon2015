package com.eaglerobotics.reconalpha.frcAPI;

/**
 * Created by tkotlarek on 2/21/15.
 */

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

public interface FRCAPIEventInterface {
    @Headers({"Accept: application/json",
    "Authorization: Basic VE9NS09UTEFSRUs6NzdBM0VFRDEtNjY0My00RkY5LUIxQTQtNDM2RkI1NTQ3NUVG"})
    @GET("/events/2015/")
    void getEvents(Callback<FRCEvents> cb);
}

