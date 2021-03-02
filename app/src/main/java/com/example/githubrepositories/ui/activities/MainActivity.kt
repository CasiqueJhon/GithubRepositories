package com.example.githubrepositories.ui.activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubrepositories.R
import com.example.githubrepositories.api.RepositoryRetriever
import com.example.githubrepositories.data.RepoResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repoList.layoutManager = LinearLayoutManager(this)
        initConnectivity()
    }
    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        Log.i(TAG, "network manager $connectivityManager")
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        Log.i(TAG, "network $activeNetwork")
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun initConnectivity() {
        if (isNetworkConnected()) {
            retrieveRepositories()
        } else {
            AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please check internet connection and try again later")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }
    }

    /**
     * Coroutines process
     * 1 Create Job() to get control
     * 2 next handle exception if we have any
     * 3 next set the scope where the Dispatchers if going to work
     * 4 finally set the result.
     *
     * */
    private fun retrieveRepositories() {
        val mainActivityJob = Job()
        val errorHandler : CoroutineExceptionHandler = CoroutineExceptionHandler {_, exception ->
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(exception.localizedMessage)
                .setPositiveButton(android.R.string.ok) {_, _ ->}
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }
        val coroutineScope : CoroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            val resultList : RepoResult = RepositoryRetriever().getRepositories()
            Log.i(TAG, "mainThread $coroutineScope")
            repoList.adapter = RepoListAdapter(resultList)
        }
    }
}