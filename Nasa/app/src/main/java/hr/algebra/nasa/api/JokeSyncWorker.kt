package com.example.jokesapp.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class JokeSyncWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        JokeFetcher(context).fetchItems()
        return Result.success()
    }
}
