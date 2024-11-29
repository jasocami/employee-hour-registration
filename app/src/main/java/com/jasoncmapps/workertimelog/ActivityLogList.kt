package com.jasoncmapps.workertimelog

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jasoncmapps.workertimelog.adapters.HourlogListAdapter
import com.jasoncmapps.workertimelog.adapters.HourlogListListener
import com.jasoncmapps.workertimelog.db.entity.EmployeeItem
import com.jasoncmapps.workertimelog.db.entity.HourlogItem
import com.jasoncmapps.workertimelog.db.viewmodel.MyViewModel
import com.jasoncmapps.workertimelog.extensions.endOfDay
import com.jasoncmapps.workertimelog.extensions.isToday
import com.jasoncmapps.workertimelog.extensions.startOfDay
import com.jasoncmapps.workertimelog.extensions.withUTC
import kotlinx.android.synthetic.main.activity_log_list.*
import kotlinx.android.synthetic.main.activity_loglist_content.*
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class ActivityLogList : AppCompatActivity(), HourlogListListener {

    companion object {
        const val ARG_EMPLOYEE_OBJ = "employee_obj"
    }

    private val fullTimeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).withUTC()

    private lateinit var myViewModel: MyViewModel

    private var employee: EmployeeItem? = null

    private var fromDate: Date = Date()
    private var toDate: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_list)
        setSupportActionBar(toolbar)

        employee = intent.getParcelableExtra(ARG_EMPLOYEE_OBJ)

        if ( employee == null ) return

        // init dates
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        fromDate = calendar.time
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        toDate = calendar.time

        // Init adapter and list
        val adapter = HourlogListAdapter(this, this)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
        list.addItemDecoration(DividerItemDecoration(list.context, DividerItemDecoration.VERTICAL))

        // init data and set data
        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        myViewModel.allHourLog(employee!!.id, fromDate.startOfDay(), toDate.endOfDay())
            .observe(this, Observer { logs ->
                logs?.let {
                    adapter.setLogs(it)
                    val todayList: ArrayList<HourlogItem> = ArrayList()
                    for ( i in logs ) {
                        if ( i.timestamp.isToday() )
                            todayList.add(i)
                    }
                    totalHoursDayLabel.text = String.format(
                        getString(R.string.hours_day_with_pholder),
                        fullTimeFormatter.format(
                            Date(addAllTimes(todayList))
                        )
                    )
                    addWeekTimes(it)
                }
            })

    }

    /*
            Header labels
     */

    private fun addAllTimes(logs: List<HourlogItem>): Long {
       // runBlocking {
            val rlogs = logs.reversed()
            var total:Long = 0
            for ( i in 0..rlogs.size-1 step 2) {
                val f = rlogs[i].timestamp
                val s: Date = if ( i + 1 <rlogs.size ) rlogs[i+1].timestamp else Date()
                total += (s.time - f.time)
            }
            return total
     //   }
    }

    private fun addWeekTimes(list: List<HourlogItem>) {
        runBlocking {
            val total = addAllTimes(list)
            totalHoursWeekLabel.text = String.format(
                getString(R.string.hours_week_pholder),
                parseToTimeString(total)
            )
        }
    }

    private fun parseToTimeString(time: Long): String {
        return String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(time),
            TimeUnit.MILLISECONDS.toMinutes(time) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
            TimeUnit.MILLISECONDS.toSeconds(time) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)))
    }

    /*
            Actions
     */

    override fun onHourlogClick(hourlogItem: HourlogItem)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.select_action))
        val actions = arrayOf(getString(R.string.delete), getString(R.string.edit))
        builder.setItems(actions) { dialog: DialogInterface?, which: Int ->
            when (which) {
                0 -> {
                    // Delete
                    myViewModel.deleteHourLog(hourlogItem)
                    setResult(Activity.RESULT_OK)
                }
                1 -> {
                    // Edit
                    editHourLogTimestamp(hourlogItem)
                }
            }

        }
        builder.setNegativeButton(android.R.string.cancel){ dialog, p1 ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun editHourLogTimestamp(hourlogItem: HourlogItem?) {

        val calendar = Calendar.getInstance()
        calendar.time = hourlogItem?.timestamp ?: Date()

        val dialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                run {
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    editTime(calendar, hourlogItem)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    private fun editTime(calendar: Calendar, hourlogItem: HourlogItem?) {
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                run {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    if (hourlogItem == null ) {
                        myViewModel.insertLog(
                            HourlogItem(0, calendar.time, employee!!.id)
                        )
                    } else {
                        hourlogItem.timestamp = calendar.time
                        myViewModel.updateHourLog(hourlogItem)
                    }
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    /*
            Menu
     */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hourlog, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_employee -> {
                deleteEmployeeAlert()
                true
            }
            R.id.action_delete_all -> {
                deleteAllAlert()
                true
            }
            R.id.action_add_register_custom -> {
                editHourLogTimestamp(null)
                true
            }
            R.id.action_add_register_now -> {
                registerLogNow()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*
            Menu actions
     */

    private fun registerLogNow() {
        myViewModel.insertLog(
            HourlogItem(0, Date(), employee!!.id)
        )
        setResult(Activity.RESULT_OK)
    }

    /**
     * Show alert to make sure that want to delete de employee
     */
    private fun deleteEmployeeAlert()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.atention))
        builder.setMessage(getString(R.string.are_you_sure_to_delete_employee))
        builder.setPositiveButton(R.string.delete) { dialog, p1 ->
            myViewModel.deleteEmployee(employee!!)
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun deleteAllAlert()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.atention))
        builder.setMessage(getString(R.string.are_you_sure_delete_all_logs_from_employee))
        builder.setPositiveButton(R.string.delete) { dialog, p1 ->
            myViewModel.deleteAllHourLog(employee!!.id)
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.dismiss()
        }
        builder.show()
    }

}
