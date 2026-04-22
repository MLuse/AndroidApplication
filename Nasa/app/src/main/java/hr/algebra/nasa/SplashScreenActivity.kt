package com.example.jokesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.jokesapp.api.JokeSyncWorker
import com.example.jokesapp.databinding.ActivitySplashScreenBinding
import com.example.jokesapp.framework.applyAnimation
import com.example.jokesapp.framework.callDelayed
import com.example.jokesapp.framework.getBooleanPreference
import com.example.jokesapp.framework.isOnline
import com.example.jokesapp.framework.startActivity
import java.util.concurrent.TimeUnit

private const val DELAY = 3000L
const val DATA_IMPORTED = "com.example.jokesapp.data_imported"
private const val DAILY_WORK = "daily_joke_sync"

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        scheduleDailyUpdates()
        redirect()
    }

    private fun startAnimations() {
        binding.tvSplash.applyAnimation(R.anim.blink)
        binding.ivSplash.applyAnimation(R.anim.bounce)
    }

    private fun scheduleDailyUpdates() {
        val request = PeriodicWorkRequestBuilder<JokeSyncWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            DAILY_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun redirect() {
        if (getBooleanPreference(DATA_IMPORTED)) {
            callDelayed(DELAY) { startActivity<HostActivity>() }
        } else if (isOnline()) {
            WorkManager.getInstance(this).enqueueUniqueWork(
                DATA_IMPORTED,
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequest.from(JokeSyncWorker::class.java)
            )
            callDelayed(DELAY) { startActivity<HostActivity>() }
        } else {
            binding.tvSplash.text = getString(R.string.no_internet)
            callDelayed(DELAY) { finish() }
        }
    }
}
