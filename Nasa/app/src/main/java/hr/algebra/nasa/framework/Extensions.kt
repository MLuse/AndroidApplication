package com.example.jokesapp.framework

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import com.example.jokesapp.JOKE_PROVIDER_CONTENT_URI
import com.example.jokesapp.model.Joke

fun View.applyAnimation(id: Int) =
    startAnimation(AnimationUtils.loadAnimation(context, id))

fun Context.setBooleanPreference(key: String, value: Boolean = true) {
    PreferenceManager.getDefaultSharedPreferences(this).edit {
        putBoolean(key, value)
    }
}

fun Context.getBooleanPreference(key: String): Boolean =
    PreferenceManager.getDefaultSharedPreferences(this).getBoolean(key, false)

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { capabilities ->
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}

@SuppressLint("Range")
fun Context.fetchJokes(selection: String? = null, selectionArgs: Array<String>? = null): MutableList<Joke> {
    val jokes = mutableListOf<Joke>()

    contentResolver.query(
        JOKE_PROVIDER_CONTENT_URI,
        null,
        selection,
        selectionArgs,
        "${Joke::_id.name} DESC"
    ).use { rs ->
        while (rs?.moveToNext() == true) {
            jokes.add(
                Joke(
                    rs.getLong(rs.getColumnIndex(Joke::_id.name)),
                    rs.getInt(rs.getColumnIndex(Joke::apiId.name)),
                    rs.getString(rs.getColumnIndex(Joke::category.name)),
                    rs.getString(rs.getColumnIndex(Joke::type.name)),
                    rs.getString(rs.getColumnIndex(Joke::joke.name)),
                    rs.getInt(rs.getColumnIndex(Joke::safe.name)) == 1,
                    rs.getString(rs.getColumnIndex(Joke::lang.name)),
                    rs.getInt(rs.getColumnIndex(Joke::nsfw.name)) == 1,
                    rs.getInt(rs.getColumnIndex(Joke::religious.name)) == 1,
                    rs.getInt(rs.getColumnIndex(Joke::political.name)) == 1,
                    rs.getInt(rs.getColumnIndex(Joke::racist.name)) == 1,
                    rs.getInt(rs.getColumnIndex(Joke::sexist.name)) == 1,
                    rs.getInt(rs.getColumnIndex(Joke::explicit.name)) == 1,
                    rs.getInt(rs.getColumnIndex(Joke::favorite.name)) == 1
                )
            )
        }
    }

    return jokes
}

fun Context.fetchFavoriteJokes(): MutableList<Joke> =
    fetchJokes("${Joke::favorite.name}=?", arrayOf("1"))

fun callDelayed(delay: Long, work: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(work, delay)
}

inline fun <reified T : Activity> Context.startActivity() = startActivity(
    Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
)

inline fun <reified T : Activity> Context.startActivity(key: String, value: Long) = startActivity(
    Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(key, value)
    }
)

inline fun <reified T : Activity> Context.startActivity(key: String, value: Int) = startActivity(
    Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(key, value)
    }
)

inline fun <reified T : BroadcastReceiver> Context.sendBroadcast() =
    sendBroadcast(Intent(this, T::class.java))
