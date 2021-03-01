package com.example.githubrepositories.api

import com.example.githubrepositories.data.RepoResult
import retrofit2.Call
import retrofit2.http.GET

interface GithubService {

    @GET("/repositories")
    fun retrieveRepositories(): Class<RepoResult>

    @GET("/search/repositories?q=language:kotlin&sort=stars&order=desc&per_page=50")
    //sample search
    fun searchRepositories(): Call<RepoResult>
}