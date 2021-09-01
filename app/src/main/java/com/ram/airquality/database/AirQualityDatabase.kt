package com.ram.airquality.database

import android.content.Context
import android.content.res.Resources
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ram.airquality.dao.AirQualityDao
import com.ram.airquality.model.AirQualityModelItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Ramashish Prajapati on 31,August,2021
 */

@Database(
    entities = [AirQualityModelItem::class],
    version = 1,
    exportSchema = false
)
abstract class AirQualityDatabase : RoomDatabase() {

    abstract fun airQualityDao(): AirQualityDao

    companion object {
        @Volatile
        private var INSTANCE: AirQualityDatabase? = null

        fun getDatabase(
            context: Context,
            coroutineScope: CoroutineScope,
            resources: Resources
        ): AirQualityDatabase {
            val tempInstanc = INSTANCE
            if (tempInstanc != null) {
                return tempInstanc
            }

            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AirQualityDatabase::class.java,
                    "AirQuality_Database"
                )
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }

    private class AirQualityDatabaseCallback(
        private val scope: CoroutineScope,
        private val resources: Resources
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE.let { database ->
                scope.launch {

                }
            }

        }
    }


}