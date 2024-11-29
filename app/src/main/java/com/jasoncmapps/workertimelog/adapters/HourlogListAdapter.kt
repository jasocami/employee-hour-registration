package com.jasoncmapps.workertimelog.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jasoncmapps.workertimelog.R
import com.jasoncmapps.workertimelog.db.entity.EmployeeItem
import com.jasoncmapps.workertimelog.db.entity.HourlogItem
import com.jasoncmapps.workertimelog.extensions.withGMT
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

/*
 * Created by jasoncastillejos on 2019-09-21.
 */

class HourlogListAdapter internal constructor(
    context: Context,
    listListener: HourlogListListener?
) : RecyclerView.Adapter<HourlogViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var hourlogs = emptyList<HourlogItem>()
    private var listener = listListener

    private val dateTimeFormat = "dd/MM/yyyy HH:mm:ss"
    private val formatter = SimpleDateFormat(dateTimeFormat, Locale.getDefault())


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlogViewHolder
    {
        val itemView = inflater.inflate(R.layout.row_hourlog, parent, false)
        return HourlogViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: HourlogViewHolder, position: Int)
    {
        val current = hourlogs[position]
        holder.name.text = "#${current.id} - ${formatter.format(current.timestamp)}"

          holder.itemView.setOnClickListener { view ->
              listener?.onHourlogClick(current)
          }

    }

    internal fun setLogs(logs: List<HourlogItem>) {
        this.hourlogs = logs
        notifyDataSetChanged()
    }

    override fun getItemCount() = hourlogs.size


}
