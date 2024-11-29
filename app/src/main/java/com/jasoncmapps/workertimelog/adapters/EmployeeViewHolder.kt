package com.jasoncmapps.workertimelog.adapters

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jasoncmapps.workertimelog.R

/*
 * Created by jasoncastillejos on 2019-09-22.
 */

class EmployeeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
{

    val name: TextView = itemView.findViewById(R.id.name)
    val active: TextView = itemView.findViewById(R.id.isActive)

    fun setIsOnline(isActive:Boolean) {
        active.text = if (isActive) "IN" else "OUT"
        active.setBackgroundColor(
            ContextCompat.getColor(itemView.context,
                if (isActive) R.color.active else R.color.deactivate)
        )
    }

}