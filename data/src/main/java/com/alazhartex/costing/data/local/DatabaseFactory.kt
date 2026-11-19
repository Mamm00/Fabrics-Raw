package com.alazhartex.costing.data.local

import android.content.Context
import androidx.room3.Room

/** Builds the Room database. Migrations are added here as schema versions grow. */
object DatabaseFactory {
    fun build(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME)
            // No fallbackToDestructiveMigration; explicit migrations only.
            .build()
}
