package com.example.inittest

import android.provider.BaseColumns

class Channel {
    companion object {
        const val TABLE_NAME = "channels"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_NAME = "name"

        val CHANNELS = arrayOf(
            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Babybel", "Baguette Laonnaise", "Bakers", "Baladi", "Balaton", "Bandal", "Banon"
        )
    }
}