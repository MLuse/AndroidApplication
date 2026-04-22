package com.example.jokesapp.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.jokesapp.model.Joke

private const val DB_NAME = "jokes.db"
private const val DB_VERSION = 1
private const val TABLE_NAME = "jokes"
private val CREATE_TABLE = "create table $TABLE_NAME( " +
        "${Joke::_id.name} integer primary key autoincrement, " +
        "${Joke::apiId.name} integer not null unique, " +
        "${Joke::category.name} text not null, " +
        "${Joke::type.name} text not null, " +
        "${Joke::joke.name} text not null, " +
        "${Joke::safe.name} integer not null, " +
        "${Joke::lang.name} text not null, " +
        "${Joke::nsfw.name} integer not null, " +
        "${Joke::religious.name} integer not null, " +
        "${Joke::political.name} integer not null, " +
        "${Joke::racist.name} integer not null, " +
        "${Joke::sexist.name} integer not null, " +
        "${Joke::explicit.name} integer not null, " +
        "${Joke::favorite.name} integer not null" +
        ")"
private const val DROP_TABLE = "drop table if exists $TABLE_NAME"

class DBRepository(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), Repository {

    override fun delete(selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.delete(TABLE_NAME, selection, selectionArgs)

    override fun update(values: ContentValues?, selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.update(TABLE_NAME, values, selection, selectionArgs)

    override fun query(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor = readableDatabase.query(
        TABLE_NAME,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    )

    override fun insert(values: ContentValues?) = writableDatabase.insertWithOnConflict(
        TABLE_NAME,
        null,
        values,
        SQLiteDatabase.CONFLICT_IGNORE
    )

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }
}
