package com.ram.airquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ram.airquality.dao.AirQualityDao
import com.ram.airquality.model.AirQualityModelItem

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
        ): AirQualityDatabase {

            synchronized(this)
            {
                var tempInstance = INSTANCE
                if (tempInstance == null) {
                    tempInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AirQualityDatabase::class.java,
                        "AirQuality_Database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = tempInstance

                }
                return tempInstance!!
            }
        }
    }
}