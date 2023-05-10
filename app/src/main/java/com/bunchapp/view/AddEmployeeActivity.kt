package com.bunchapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bunchapp.R
import com.bunchapp.database.DBHelper
import com.bunchapp.databinding.ActivityAddEmployeeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEmployeeActivity : AppCompatActivity(), View.OnClickListener {
    val dbHelper = DBHelper(this, null)
    var binding: ActivityAddEmployeeBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_employee)
        init()
    }

    private fun init() {
        binding?.btnAddEmployee?.setOnClickListener(this)
    }

    private suspend fun insertData(name: String) {
        withContext(Dispatchers.IO)
        {
            dbHelper.addEmployee(name)
            // Display a toast message to indicate success
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@AddEmployeeActivity,
                    "Employee added successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAddEmployee -> {
                if (binding?.etEmployee?.text?.isEmpty() == false)
                    lifecycleScope.launch {
                        insertData(binding?.etEmployee?.text.toString())
                    }
                else
                    Toast.makeText(
                        applicationContext,
                        "Please Enter Employee Name!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }
}