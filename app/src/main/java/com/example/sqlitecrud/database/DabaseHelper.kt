package com.example.sqlitecrud.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.number.IntegerWidth
import android.widget.Toast
import androidx.core.content.contentValuesOf
import com.example.sqlitecrud.MainActivity
import com.example.sqlitecrud.model.TaskListModel

class DabaseHelper (context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{
        private val DB_NAME = "task"
        private val DB_VERSION = 1
        private val TABLE_NAME = "tasklist"
        private val ID = "id"
        private val TASK_NAME = "taskname"
        private val TASK_DETAILS = "taskdetails"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $TASK_NAME TEXT,T$TASK_DETAILS TEXT)"
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        p0?.execSQL(DROP_TABLE)
        onCreate(p0)
    }

    fun getALlTask(): List<TaskListModel>{
        val tasklist = ArrayList<TaskListModel>()
        val db = writableDatabase
        val selectQuery = "SELECT *FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()){
                do {
                    val tasks = TaskListModel()
                    try {
                        tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ID)))
                        tasks.name = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_NAME))
                        tasks.details = cursor.getString(cursor.getColumnIndexOrThrow(TASK_DETAILS))
                        tasklist.add(tasks)
                    } catch (e: IllegalArgumentException) {

                    }
                }while (cursor.moveToFirst())
            }
        }
        cursor.close()
        return tasklist
    }


    fun addTask(task : TaskListModel): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, task.name)
        values.put(TASK_DETAILS, task.details)
        val _succes = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$_succes") != -1)

    }

    fun getTask(_id: Int) : TaskListModel{
        val tasks = TaskListModel()
        val db = writableDatabase
        val selectQuery = "SELECT *FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        try {
            cursor?.moveToFirst()
            tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ID)))
            tasks.name = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_NAME))
            tasks.details = cursor.getString(cursor.getColumnIndexOrThrow(TASK_DETAILS))
        } catch (e: IllegalArgumentException) {

        }
        cursor.close()
        return tasks

    }


    fun deleteTask(_id: Int) : Boolean{
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID +"=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1

    }

    fun updateTask(tasks: TaskListModel) : Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, tasks.name)
        values.put(TASK_DETAILS, tasks.details)
        val _success = db.update(TABLE_NAME,values, ID + "=?", arrayOf(tasks.id.toString()))
        db.close()
        return Integer.parseInt("$_success") != -1
    }
}