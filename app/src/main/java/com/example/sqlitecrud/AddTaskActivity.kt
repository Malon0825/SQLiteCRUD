package com.example.sqlitecrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sqlitecrud.adapter.TaskListAdapter
import com.example.sqlitecrud.database.DabaseHelper
import com.example.sqlitecrud.model.TaskListModel

class AddTaskActivity : AppCompatActivity() {

    lateinit var btn_save : Button
    lateinit var btn_del : Button
    lateinit var et_name : EditText
    lateinit var et_details : EditText

    var dbHandler : DabaseHelper ?= null
    var isEditMode : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        btn_save = findViewById(R.id.button_save)
        btn_del = findViewById(R.id.button_details)
        et_name = findViewById(R.id.et_name)
        et_details = findViewById(R.id.et_details)

        dbHandler = DabaseHelper(this)

        if (intent != null && intent.getStringExtra("Model") == "E"){
            isEditMode = true
            btn_save.text = "Update Data"
            btn_del.visibility = View.VISIBLE
            //Toast.makeText(applicationContext, "edit mode is true", Toast.LENGTH_LONG).show()

            val tasks : TaskListModel = dbHandler!!.getTask(intent.getIntExtra("Id", 0))
            et_name.setText(tasks.name)
            et_details.setText(tasks.details)

        }else{
            //Toast.makeText(applicationContext, "edit mode is false", Toast.LENGTH_LONG).show()
            isEditMode = false
            btn_save.text = "Save Data"
            btn_del.visibility = View.GONE

        }

        btn_save.setOnClickListener{
            var success : Boolean = false
            val tasks : TaskListModel = TaskListModel()
            if (isEditMode){
                //Toast.makeText(applicationContext, "Your not in edit mode", Toast.LENGTH_LONG).show()
                tasks.id = intent.getIntExtra("Id", 0)
                tasks.name = et_name.text.toString()
                tasks.details = et_details.toString()

                success = dbHandler?.addTask(tasks) as Boolean
                //Toast.makeText(applicationContext, success.toString(), Toast.LENGTH_LONG).show()

            }else{
                //Toast.makeText(applicationContext, "Your in edit mode", Toast.LENGTH_LONG).show()
                tasks.name = et_name.text.toString()
                tasks.details = et_details.text.toString()

                success = dbHandler?.addTask(tasks) as Boolean

            }
            if (success) {
                Toast.makeText(applicationContext, "Data inserted!!", Toast.LENGTH_LONG).show()
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
                finish()
            }else{
                Toast.makeText(applicationContext, "Data is not inserted!!", Toast.LENGTH_LONG).show()
            }
        }

        btn_del.setOnClickListener{
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Yes or No")
                .setPositiveButton("Yes") { dialog, i ->
                    val success = dbHandler?.deleteTask(intent.getIntExtra("Id", 0)) as Boolean
                    if (success)
                        finish()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, i ->
                    dialog.dismiss()
                }
            dialog.show()
        }
    }
}