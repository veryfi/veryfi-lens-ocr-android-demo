package com.veryfi.lens.ocr.demo.logs

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.lriccardo.timelineview.TimelineDecorator
import com.veryfi.lens.VeryfiLens
import com.veryfi.lens.VeryfiLensDelegate
import com.veryfi.lens.ocr.demo.R
import com.veryfi.lens.ocr.demo.databinding.ActivityLogsBinding
import com.veryfi.lens.ocr.demo.helpers.ThemeHelper
import org.json.JSONException
import org.json.JSONObject

class LogsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogsBinding
    private lateinit var adapter: LogsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applicationContext?.let { ThemeHelper.setSecondaryColorToStatusBar(this, it) }
        setUpVeryfiLensDelegate()
        VeryfiLens.showCamera()
        setUpToolBar()
        loadData()
    }

    private fun setUpToolBar() {
        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationIcon(R.drawable.ic_vector_close_shape)
        binding.topAppBar.setNavigationOnClickListener { finish() }
    }

    private fun loadData() {
        binding.timelineRv.let {
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            adapter = LogsAdapter()
            it.adapter = adapter

            val colorPrimary = ThemeHelper.getPrimaryColor(this)
            it.addItemDecoration(
                TimelineDecorator(
                    position = TimelineDecorator.Position.Left,
                    indicatorColor = colorPrimary,
                    lineColor = colorPrimary
                )
            )

            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    (it.layoutManager as? LinearLayoutManager)?.let {

                    }
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showLogs(json: JSONObject) {
        val log = Log()
        val jsonString = json.toString()
        val jsonParser = JsonParser()
        val jsonElement = jsonParser.parse(jsonString)
        val gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJsonString = gson.toJson(jsonElement)

        when (if (json.has(STATUS)) json.getString(STATUS) else "") {
            CLOSE -> log.title = resources.getString(R.string.logs_close)
            else -> log.title = resources.getString(R.string.logs_ocr_details)
        }

        log.message = prettyJsonString
        adapter.addItem(log)
        adapter.notifyDataSetChanged()
        binding.timelineRv.scrollToPosition(adapter.itemCount - 1)
    }

    private fun setUpVeryfiLensDelegate() {
        VeryfiLens.setDelegate(object : VeryfiLensDelegate {
            override fun veryfiLensClose(json: JSONObject) {
                mainLooper?.let {
                    Handler(it).post {
                        showLogs(json)
                    }
                }
            }

            override fun veryfiLensError(json: JSONObject) {

            }

            override fun veryfiLensSuccess(json: JSONObject) {
                mainLooper?.let {
                    Handler(it).post {
                        showLogs(json)
                    }
                }
            }

            override fun veryfiLensUpdate(json: JSONObject) {

            }
        })
    }

    private inner class VeryfiServiceJsonReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bundle = intent.extras
            if (bundle != null) {
                val json = JSONObject()
                for (key in bundle.keySet()) {
                    val value = bundle.get(key)
                    if (value != null) {
                        try {
                            json.put(key, JSONObject.wrap(bundle.get(key)))
                        } catch (e: JSONException) {
                            //Handle exception here
                        }
                    }
                }
                showLogs(json)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //do nothing
    }

    companion object {
        const val STATUS = "status"
        const val CLOSE = "close"
    }
}