package com.example.githubrepositories.data

import com.google.gson.Gson
import java.net.URL

class Request {

    companion object {
        private const val URL = "https://api.github.com/search/repositories"
        private const val SEARCH = "q=language:kotlin&sort=stars&order=desc&?per_page=50"
        private const val COMPLETE_URL = "$URL?$SEARCH"
    }

    fun run(): RepoResult {
        val repoListJsonStr = URL(COMPLETE_URL).readText()
        return Gson().fromJson(repoListJsonStr, RepoResult::class.java)
    }
}