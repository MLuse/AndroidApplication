package com.example.jokesapp

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import com.example.jokesapp.dao.Repository
import com.example.jokesapp.dao.getRepository
import com.example.jokesapp.model.Joke

private const val AUTHORITY = "com.example.jokesapp.provider"
private const val PATH = "jokes"
val JOKE_PROVIDER_CONTENT_URI: Uri = "content://$AUTHORITY/$PATH".toUri()

private const val JOKES = 10
private const val JOKE_ITEM_ID = 20

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH, JOKES)
    addURI(AUTHORITY, "$PATH/#", JOKE_ITEM_ID)
    this
}

class JokeProvider : ContentProvider() {

    private lateinit var repository: Repository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when (URI_MATCHER.match(uri)) {
            JOKES -> return repository.delete(selection, selectionArgs)
            JOKE_ITEM_ID -> {
                val id = uri.lastPathSegment
                if (id != null) {
                    return repository.delete("${Joke::_id.name}=?", arrayOf(id))
                }
            }
        }
        throw IllegalArgumentException("Wrong URI")
    }

    override fun getType(uri: Uri): String = when (URI_MATCHER.match(uri)) {
        JOKES -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH"
        JOKE_ITEM_ID -> "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH"
        else -> throw IllegalArgumentException("Wrong URI")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val id = repository.insert(values)
        return ContentUris.withAppendedId(JOKE_PROVIDER_CONTENT_URI, id)
    }

    override fun onCreate(): Boolean {
        repository = getRepository(context)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor = repository.query(projection, selection, selectionArgs, sortOrder)

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when (URI_MATCHER.match(uri)) {
            JOKES -> return repository.update(values, selection, selectionArgs)
            JOKE_ITEM_ID -> {
                val id = uri.lastPathSegment
                if (id != null) {
                    return repository.update(values, "${Joke::_id.name}=?", arrayOf(id))
                }
            }
        }
        throw IllegalArgumentException("Wrong URI")
    }
}
