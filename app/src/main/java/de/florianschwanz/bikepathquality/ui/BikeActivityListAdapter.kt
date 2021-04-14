package de.florianschwanz.bikepathquality.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.florianschwanz.bikepathquality.R
import de.florianschwanz.bikepathquality.storage.bike_activity.BikeActivityWithDetails
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class BikeActivityListAdapter :
    RecyclerView.Adapter<BikeActivityListAdapter.BikeActivityViewHolder>() {

    var data = listOf<BikeActivityWithDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeActivityViewHolder {
        return BikeActivityViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BikeActivityViewHolder, position: Int) {
        val current = data[position]
        holder.bind(current)
    }

    class BikeActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvStartTime: TextView = itemView.findViewById(R.id.tvStartTime)
        private val ivOngoing: ImageView = itemView.findViewById(R.id.ivOngoing)
        private val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        private val tvDetails: TextView = itemView.findViewById(R.id.tvDetails)

        private var sdfShort: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        private var sdf: SimpleDateFormat = SimpleDateFormat("MMM dd HH:mm:ss", Locale.ENGLISH)

        fun bind(item: BikeActivityWithDetails) {
            val resources = itemView.context.resources

            if (item.bikeActivity.endTime != null) {
                ivOngoing.visibility = View.INVISIBLE

                val diff =
                    item.bikeActivity.endTime.toEpochMilli() - item.bikeActivity.startTime.toEpochMilli()
                val duration = (diff / 1000 / 60).toInt()
                tvDuration.text =
                    resources.getQuantityString(R.plurals.duration, duration, duration)
                tvDetails.text = resources.getQuantityString(
                    R.plurals.details,
                    item.bikeActivityDetails.size,
                    item.bikeActivityDetails.size
                )
            } else {
                val animation: Animation = AlphaAnimation(1f, 0f)
                animation.duration = 1000
                animation.interpolator = LinearInterpolator()
                animation.repeatCount = Animation.INFINITE
                animation.repeatMode = Animation.REVERSE
                ivOngoing.visibility = View.VISIBLE
                ivOngoing.startAnimation(animation)
            }

            tvStartTime.text =
                if (item.bikeActivity.startTime.isToday()) sdfShort.format(Date.from(item.bikeActivity.startTime)) else sdf.format(
                    Date.from(item.bikeActivity.startTime)
                )
        }

        private fun Instant.isToday() =
            this.truncatedTo(ChronoUnit.DAYS).equals(Instant.now().truncatedTo(ChronoUnit.DAYS))

        companion object {
            fun create(parent: ViewGroup): BikeActivityViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.activities_item, parent, false)
                return BikeActivityViewHolder(view)
            }
        }
    }
}