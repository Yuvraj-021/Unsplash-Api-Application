package com.example.Unsplash_Api.api;

import com.example.Unsplash_Api.models.ImageModel;
import com.example.Unsplash_Api.models.SearchModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static com.example.Unsplash_Api.api.ApiUtilities.API_KEY;


public interface ApiInterface {

    @Headers("Authorization: Client-ID "+API_KEY)
    @GET("/photos")
    Call<List<ImageModel>> getImages(
            @Query("page") int page,
            @Query("per_page") int perPage
    );


    @Headers("Authorization: Client-ID "+API_KEY)
    @GET("/search/photos")
    Call<SearchModel> searchImages(
            @Query("query") String query
    );

}
