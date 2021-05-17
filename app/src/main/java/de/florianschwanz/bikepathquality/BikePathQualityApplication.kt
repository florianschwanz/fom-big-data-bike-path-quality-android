package de.florianschwanz.bikepathquality

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import de.florianschwanz.bikepathquality.data.storage.AppDatabase
import de.florianschwanz.bikepathquality.data.storage.bike_activity.BikeActivityMeasurementRepository
import de.florianschwanz.bikepathquality.data.storage.bike_activity.BikeActivityRepository
import de.florianschwanz.bikepathquality.data.storage.bike_activity_sample.BikeActivitySampleRepository
import de.florianschwanz.bikepathquality.data.storage.log_entry.LogEntry
import de.florianschwanz.bikepathquality.data.storage.log_entry.LogEntryRepository
import de.florianschwanz.bikepathquality.data.storage.log_entry.LogEntryViewModel
import de.florianschwanz.bikepathquality.data.storage.user_data.UserDataRepository
import de.florianschwanz.bikepathquality.services.TrackingForegroundService
import de.florianschwanz.bikepathquality.services.TrackingForegroundService.Companion.ACTION_START

class BikePathQualityApplication : Application() {

    private lateinit var logEntryViewModel: LogEntryViewModel

    val database by lazy { AppDatabase.getDatabase(this) }
    val logEntryRepository by lazy { LogEntryRepository(database.logEntryDao()) }
    val bikeActivityRepository by lazy { BikeActivityRepository(database.bikeActivityDao()) }
    val bikeActivitySampleRepository by lazy { BikeActivitySampleRepository(database.bikeActivitySampleDao()) }
    val bikeActivityMeasurementRepository by lazy { BikeActivityMeasurementRepository(database.bikeActivityMeasurementDao()) }
    val userDataRepository by lazy { UserDataRepository(database.userDataDao()) }

    //
    // Lifecycle phases
    //

    /**
     * Handles on-create lifecycle phase
     */
    override fun onCreate() {
        super.onCreate()

        logEntryViewModel = LogEntryViewModel(logEntryRepository)
        logEntryViewModel.insert(LogEntry(message = "\nStart tracking service"))

        val trackingForegroundServiceIntent = Intent(this, TrackingForegroundService::class.java)
        trackingForegroundServiceIntent.setAction(ACTION_START)
        ContextCompat.startForegroundService(this, trackingForegroundServiceIntent)
    }
}