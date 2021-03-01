package com.example.githubrepositories.ui.activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubrepositories.R
import com.example.githubrepositories.api.RepositoryRetriever
import com.example.githubrepositories.data.RepoResult
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "MainActivity"
    }
    private val repoRetriever= RepositoryRetriever()
    private val callback = object :Callback<RepoResult> {
        override fun onResponse(call: Call<RepoResult>, response: Response<RepoResult>) {
            response.isSuccessful.let {
                val resultList = RepoResult(response.body()?.items ?: emptyList())
                repoList.adapter = RepoListAdapter(resultList)
            }
        }

        override fun onFailure(call: Call<RepoResult>, t: Throwable) {
            Log.i(TAG, "Problem calling Github API {${t.localizedMessage}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repoList.layoutManager = LinearLayoutManager(this)

        if (isNetworkConnected()) {
            repoRetriever.getRepositories(callback)
        } else {
            AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please check internet connection and try again later")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }
    }
    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork

        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}