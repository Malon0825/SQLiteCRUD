package com.example.sqlitecrud

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlitecrud.adapter.TaskListAdapter
import com.example.sqlitecrud.database.DabaseHelper
import com.example.sqlitecrud.model.TaskListModel

class MainActivity : AppCompatActivity() {

    lateinit var recycler_task : RecyclerView
    lateinit var btn_add : Button
    var taskListAdapter : TaskListAdapter ?= null
    var dbHandler : DabaseHelper ?= null
    var taskList : List<TaskListModel> = ArrayList<TaskListModel>()
    var linearlayoutManager : LinearLayoutManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_task = findViewById(R.id.rv_list)
        btn_add = findViewById(R.id.bt_add_items)

        dbHandler = DabaseHelper(this)
        fetchList()


        btn_add.setOnClickListener{
            //Toast.makeText(applicationContext, "Data inserted!!", Toast.LENGTH_LONG).show()
            val i = Intent(applicationContext,AddTaskActivity::class.java)
            startActivity(i)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchList(){
        taskList = dbHandler!!.getALlTask()
        taskListAdapter = TaskListAdapter(taskList, applicationContext)
        linearlayoutManager = LinearLayoutManager(applicationContext)
        recycler_task.layoutManager = linearlayoutManager
        recycler_task.adapter = taskListAdapter
        taskListAdapter?.notifyDataSetChanged()
    }
}