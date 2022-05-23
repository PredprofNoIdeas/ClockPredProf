package com.example.clockpredprof;

import com.example.clockpredprof.Weather.WeatherInWorld;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface WeatherInterface {
    @GET("weather")
    @Headers({
            "X-RapidAPI-Host: community-open-weather-map.p.rapidapi.com",
            "X-RapidAPI-Key: 2ec253d014msh98189487d6b499bp1d03c2jsn2ab4b4a80c84"
    })
    Call<WeatherInWorld> weatherInWorld(@Query("q") String q, @Query("units") String units);
}
