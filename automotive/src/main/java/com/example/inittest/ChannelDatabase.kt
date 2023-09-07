package com.example.inittest

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ChannelDatabase {
    companion object {
        private const val DATABASE_NAME = "channel.db"
        private const val DATABASE_VERSION = 1
        private var sInstance: DatabaseHelper? = null

        @Synchronized
        fun getInstance(context: Context): SQLiteDatabase {
            if (sInstance == null) {
                sInstance = DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
            }
            return sInstance!!.writableDatabase
        }

        class DatabaseHelper(
            context: Context?,
            name: String?,
            factory: SQLiteDatabase.CursorFactory?,
            version: Int) : SQLiteOpenHelper(context, name, factory, version) {
            override fun onCreate(_db: SQLiteDatabase?) {
                _db?.execSQL("CREATE TABLE ${Channel.TABLE_NAME}"
                        + " (${Channel.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + " ${Channel.COLUMN_NAME} TEXT NOT NULL);")
            }

            override fun onUpgrade(_db: SQLiteDatabase?, _oldVersion: Int, _newVersion: Int) {
                _db?.execSQL("DROP TABLE IF EXISTS ${Channel.TABLE_NAME}")
                onCreate(_db)
            }
        }
    }
}