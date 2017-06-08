package com.example.x550v.five;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by X550V on 2017/6/8.
 */

public interface GitHubService {
    @GET("users/{user}/repos")
    Call<JSONObject> listRepos(@Path("user") String user);
}
