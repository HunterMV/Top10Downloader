package com.example.top10downloader

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ListView
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import kotlin.properties.Delegates

private const val TAG = "DownloaderData"

class DownloadData(private val callBack: DownLoaderCallBack) :
    AsyncTask<String, Void, String>() {

    interface DownLoaderCallBack {
        fun onDataAvailable(data: List<FeedEntry>)
    }
    override fun onPostExecute(result: String) {

        val parseApplications = ParseApplications()
        if (result.isEmpty()) {
            parseApplications.parse(result)
        }

        callBack.onDataAvailable(parseApplications.applications)
    }

    override fun doInBackground(vararg url: String): String {
        Log.d(TAG, "doInBackground: starts with ${url[0]}")
        val rssFeed = downloadXML(url[0])
        if (rssFeed.isEmpty()) {
            Log.e(TAG, "doInBackground: Error downloading")
        }
        return rssFeed
    }

    private fun downloadXML(urlpath: String): String {
        try {
            return URL(urlpath).readText()
        } catch (e: MalformedURLException){
            Log.d(TAG, "downloadXML: Invalid URl " + e.message)
        }catch (e: IOException){
            Log.d(TAG, "downloadXML: IO Exception reading data " + e.message)
        }catch (e: SecurityException){
            Log.d(TAG, "downloadXML: Security Exception. Needs permissions?" + e.message)
        }

        return ""   //return empty string if there was an exception
    }
}