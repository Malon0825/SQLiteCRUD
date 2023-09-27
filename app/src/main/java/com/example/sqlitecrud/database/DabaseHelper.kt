package com.example.sqlitecrud.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.sqlitecrud.model.TaskListModel
import android.content.Context as Context1

class DabaseHelper (context: Context1) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{
        private val DB_NAME = "task"
        private val DB_VERSION = 1
        private val TABLE_NAME = "tasklist"
        private val ID = "id"
        private val TASK_NAME = "taskname"
        private val TASK_DETAILS = "taskdetails"
    }

//    override fun onCreate(p0: SQLiteDatabase?) {
//    // Drop the table if it exists
//        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
//        p0?.execSQL(DROP_TABLE)
//    // Create a new table with the correct column name
////        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $TASK_NAME TEXT, $TASK_DETAILS TEXT)"
////        p0?.execSQL(CREATE_TABLE)
//    }
//
//    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int)
//    { // Call the onCreate method to recreate the table onCreate(p0) //
//        onCreate(p0)
//    }




    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $TASK_NAME TEXT, $TASK_DETAILS TEXT)"
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
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            while (cursor.moveToNext()) { // use cursor.moveToNext() instead of cursor.moveToFirst()
                val tasks = TaskListModel()
                try {
                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ID)))
                    tasks.name = cursor.getString(cursor.getColumnIndexOrThrow(TASK_NAME))
                    tasks.details = cursor.getString(cursor.getColumnIndexOrThrow(TASK_DETAILS))
                    tasklist.add(tasks)
                } catch (e: IllegalArgumentException) {
                    Log.e("DBHelper", e.message.toString())
                }
            }
        }
        cursor.close()
        return tasklist
    }

    fun addTask(task : TaskListModel): Boolean{ val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, task.name)
        values.put(TASK_DETAILS, task.details)
        var success : Boolean = false
        try
        {
            val _success = db.insertOrThrow(TABLE_NAME, null, values)
            Log.e("DBHelper", _success.toString())
            success = (_success != 0L) // where _success is the long value
        } catch (e: Exception)
        {
            Log.e("DBHelper", e.message.toString())
        }
        finally {
            db.close()
            return success }
    }


//    fun addTask(task : TaskListModel): Boolean{
//        val db = this.writableDatabase
//        val values = ContentValues()
//        values.put(TASK_NAME, task.name)
//        values.put(TASK_DETAILS, task.details)
//        var success : Boolean = false
//        try {
//            val _succes = db.insertOrThrow(TABLE_NAME, null, values)
//            Log.e("DBHelper", _succes.toString())
//            val b = l !== 0 // where l is the long value
//
//            success = _succes as Boolean
//
//        } catch (e: Exception) {
//            Log.e("DBHelper", e.message.toString())
//        } finally {
//            db.close()
//            return success
//        }
//    }

    fun getTask(_id: Int) : TaskListModel{
        val tasks = TaskListModel()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
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