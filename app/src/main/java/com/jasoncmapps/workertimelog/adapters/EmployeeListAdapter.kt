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

/*
 * Created by jasoncastillejos on 2019-09-21.
 */

class EmployeeListAdapter internal constructor(
    context: Context,
    listListener: EmployeeListListener?
) : RecyclerView.Adapter<EmployeeViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var employees = emptyList<EmployeeItem>()
    private var listener = listListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder
    {
        val itemView = inflater.inflate(R.layout.row_employee, parent, false)
        return EmployeeViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int)
    {
        val current = employees[position]
        holder.name.text = "#${current.id} - ${current.name}"
        holder.setIsOnline(current.isActive)

        holder.itemView.setOnClickListener { view ->
            listener?.onEmployeeClick(current)
        }

    }

    internal fun setEmployees(items: List<EmployeeItem>) {
        this.employees = items
        notifyDataSetChanged()
    }

    internal fun getEmployees() = employees

    override fun getItemCount() = employees.size

}