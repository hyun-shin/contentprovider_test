package com.example.inittest

import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
        const val LOADER_CHANNELS = 1
    }
    private var channelAdapter: ChannelAdapter = ChannelAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        populateInitialDataIfNeeded()

        val list = findViewById<RecyclerView>(R.id.list)
        list.layoutManager = LinearLayoutManager(list.context)
        list.adapter = channelAdapter

        LoaderManager.getInstance(this)
            .initLoader(`LOADER_CHANNELS`, null, loaderCallbacks)
    }

    private val loaderCallbacks: LoaderManager.LoaderCallbacks<Cursor> =
        object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor?> {
                return CursorLoader(
                    applicationContext,
                    DtvContentProvider.URI_CHANNEL, // uri
                    arrayOf<String>(Channel.COLUMN_NAME), // projection
                    null, // selection
                    null, // selectionArgs
                    Channel.COLUMN_NAME // sortOrder
                )
            }

            override fun onLoadFinished(loader: Loader<Cursor?>, data: Cursor?) {
                channelAdapter.setChannels(data)
            }

            override fun onLoaderReset(loader: Loader<Cursor?>) {
                channelAdapter.setChannels(null)
            }
        }

    internal class ChannelAdapter : RecyclerView.Adapter<ChannelAdapter.ViewHolder?>() {
        private var cursor: Cursor? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (cursor!!.moveToPosition(position)) {
                holder.text.text = cursor!!.getString(
                    cursor!!.getColumnIndexOrThrow(Channel.COLUMN_NAME)
                )
            }
        }

        fun setChannels(cursor: Cursor?) {
            this.cursor = cursor
            notifyDataSetChanged()
        }

        internal class ViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    android.R.layout.simple_list_item_1, parent, false
                )
            ) {
            val text: TextView = itemView.findViewById(android.R.id.text1)
        }

        override fun getItemCount(): Int {
            return if (cursor == null) {
                0
            } else {
                cursor!!.count
            }
        }
    }

    private fun populateInitialDataIfNeeded() {
        val cursor = contentResolver.query(
            DtvContentProvider.URI_CHANNEL,
            null,
            null,
            null,
            null
        )
        if (cursor != null && cursor.count == 0) {
            Log.d(TAG, "Add initial data")
            for (channel in  Channel.CHANNELS) {
                val values = ContentValues()
                values.put(Channel.COLUMN_NAME, channel)
                contentResolver.insert(DtvContentProvider.URI_CHANNEL, values)
            }
        }
    }

    fun addItem(view: View) {
        val values = ContentValues()
        values.put(Channel.COLUMN_NAME, "New Item")
        val uri = contentResolver.insert(DtvContentProvider.URI_CHANNEL, values)
        Log.d(TAG, "Added item: $uri")
    }

    fun updateItem(view: View) {
        val uri = queryAndGetOne()
        if (uri != null) {
            Log.d(TAG, "Update item: $uri")
            val values = ContentValues()
            values.put(Channel.COLUMN_NAME, "Updated Item")
            contentResolver.update(uri, values, null, null)
        }
    }

    fun removeItem(view: View) {
        val uri = queryAndGetOne()
        if (uri != null) {
            Log.d(TAG, "Remove item: $uri")
            contentResolver.delete(
                uri,
                null,
                null
            )
        }
    }

    private fun queryAndGetOne() : Uri? {
        val cursor = contentResolver.query(
            DtvContentProvider.URI_CHANNEL, // uri
            null, // projection
            null, // selection
            null, // selectionArgs
            Channel.COLUMN_NAME // sortOrder
        )
        return if (cursor != null && cursor.count != 0) {
            cursor.moveToFirst()
            val id = cursor.getString(cursor.getColumnIndex(Channel.COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndex(Channel.COLUMN_NAME));
            val uri = ContentUris.withAppendedId(DtvContentProvider.URI_CHANNEL, id.toLong())
            Log.d(TAG, "query and return uri: $uri (id: $id, name: $name)")
            uri
        } else {
            null
        }
    }
}