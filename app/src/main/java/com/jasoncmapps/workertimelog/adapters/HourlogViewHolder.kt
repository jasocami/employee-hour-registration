package com.jasoncmapps.workertimelog.adapters

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jasoncmapps.workertimelog.R

/*
 * Created by jasoncastillejos on 2019-09-22.
 */

class HourlogViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
{

    val name: TextView = itemView.findViewById(R.id.name)

}