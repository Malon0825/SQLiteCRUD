package com.example.sqlitecrud.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlitecrud.AddTaskActivity
import com.example.sqlitecrud.R
import com.example.sqlitecrud.model.TaskListModel

class TaskListAdapter(tasklist : List<TaskListModel>, internal var context: Context)
    : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>()
{

    internal  var tasklist : List<TaskListModel> = ArrayList()
    init {
        this.tasklist = tasklist
    }

    inner  class TaskViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var name : TextView = view.findViewById(R.id.et_recycler_name)
        var details : TextView = view.findViewById(R.id.et_recycler_details)
        var btn_edit : Button = view.findViewById(R.id.button_edit)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
         val view = LayoutInflater.from(context).inflate(R.layout.recycler_task_list,parent,false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskListAdapter.TaskViewHolder, position: Int) {
        val task = tasklist[position]
        holder.name.text = task.name
        holder.details.text = task.details

        holder.btn_edit.setOnClickListener{
            val i = Intent(context,AddTaskActivity::class.java)
            i.putExtra("Model", "E")
            i.putExtra("Id", task.id)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return tasklist.size
    }
}