package com.example.inittest

import android.provider.BaseColumns

class Channel {
    companion object {
        const val TABLE_NAME = "channels"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_NAME = "name"

        val CHANNELS = arrayOf(
            "Channel1", "Channel2", "Channel3", "Channel4", "Channel5"
        )
    }
}