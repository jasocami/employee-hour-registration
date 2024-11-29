package com.jasoncmapps.workertimelog

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.jasoncmapps.workertimelog.adapters.EmployeeListAdapter
import com.jasoncmapps.workertimelog.adapters.EmployeeListListener
import com.jasoncmapps.workertimelog.db.entity.EmployeeItem
import com.jasoncmapps.workertimelog.db.viewmodel.MyViewModel
import com.jasoncmapps.workertimelog.extensions.buildActivityHourLogList
import com.jasoncmapps.workertimelog.extensions.endOfDay
import com.jasoncmapps.workertimelog.extensions.startOfDay
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.coroutines.runBlocking
import java.util.*

class ActivityMain : AppCompatActivity(), EmployeeListListener {

    companion object {
        const val INTENT_RESULT_LOG_LIST = 100
    }

    /*
    Example app with room
    https://github.com/googlesamples/android-architecture-components/tree/master/BasicSample/app/src/main/java/com/example/android/persistence/viewmodel
     */

    private lateinit var myViewModel: MyViewModel

    var adapter : EmployeeListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Init adapter and list
        adapter = EmployeeListAdapter(this, this)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)


        // init data and set data
        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        myViewModel.allEmployee().observe(this, Observer { employees ->
            employees?.let {
               updateEmployeeStatusAndList(employees)
            }
        })

        // Fab

        fab.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.new_employee))
            val dialogView = layoutInflater.inflate(R.layout.dialog_new_employee, null)
            val categoryEditText = dialogView.findViewById(R.id.nameInput) as TextInputEditText
            builder.setView(dialogView)

            // set up the ok button
            builder.setPositiveButton(R.string.create) { dialog, p1 ->
                val newName = categoryEditText.text.toString()
                var isValid = true
                if (newName.isBlank()) {
                    categoryEditText.error = getString(R.string.cant_be_empty)
                    isValid = false
                }

                if (isValid) {
                    myViewModel.insertEmployee(
                        EmployeeItem(0, newName, false)
                    )

                    dialog.dismiss()
                } else {

                }
            }

            builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
                dialog.cancel()
            }

            builder.show()
        }

    }

    private fun updateEmployeeStatusAndList(employeeList: List<EmployeeItem>){
        runBlocking {
           for ( e in employeeList) {
            e.isActive = myViewModel.getAllHourLog(e.id, Date().startOfDay(), Date().endOfDay()).size % 2 != 0
        }
            adapter?.setEmployees(employeeList)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( requestCode == INTENT_RESULT_LOG_LIST ) {
            if ( resultCode == Activity.RESULT_OK ) {
                adapter?.let {
                    updateEmployeeStatusAndList(it.getEmployees())
                }
            }
        }

    }

    // List click listener

    override fun onEmployeeClick(employee: EmployeeItem)
    {
        startActivityForResult(
            buildActivityHourLogList(employee),
            INTENT_RESULT_LOG_LIST
        )
    }
}
