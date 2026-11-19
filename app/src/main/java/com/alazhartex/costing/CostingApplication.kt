package com.alazhartex.costing

import android.app.Application
import com.alazhartex.costing.di.AppContainer

/** Application entry point; owns the shared manual DI container. */
class CostingApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
