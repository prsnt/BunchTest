package com.bunchapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bunchapp.model.Employee
import java.util.*

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "+
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COl + " TEXT NOT NULL )"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addEmployee(name : String){
        val values = ContentValues()

        values.put(NAME_COl, name)

        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)

        db.close()
    }

    fun getEmployee(): List<Employee>? {
        val list = ArrayList<Employee>()

        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val model = Employee(cursor.getInt(0),cursor.getString(1))
                list.add(model)
            } while (cursor.moveToNext());
        }
        cursor.close()
        db.close()
        return list
    }

    fun getEmployeeExcel(): Cursor? {

        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        //cursor.close()
        //db.close()
        return cursor
    }

    companion object {
        // below is variable for database name
        private const val DATABASE_NAME = "BUNCH_TEST"

        // below is the variable for database version
        private const val DATABASE_VERSION = 1

        // below is the variable for table name
        const val TABLE_NAME = "employee"

        // below is the variable for id column
        const val ID_COL = "EmpID"

        // below is the variable for name column
        const val NAME_COl = "EmpName"
    }
}