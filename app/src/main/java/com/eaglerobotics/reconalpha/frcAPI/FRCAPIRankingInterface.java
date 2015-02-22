package com.eaglerobotics.reconalpha.frcAPI;

/**
 * Created by tkotlarek on 2/21/15.
 */
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

public interface FRCAPIRankingInterface {
    @Headers({"Accept: application/json",
    "Authorization: Basic VE9NS09UTEFSRUs6NzdBM0VFRDEtNjY0My00RkY5LUIxQTQtNDM2RkI1NTQ3NUVG"})
    @GET("/rankings/2015/{event}")
    void getRankings(@Path("event") String eventID, Callback<FRCRankings> postRanking);
}

