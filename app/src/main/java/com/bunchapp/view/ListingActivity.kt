package com.bunchapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bunchapp.EmployeeAdapter
import com.bunchapp.R
import com.bunchapp.database.DBHelper
import com.bunchapp.databinding.ActivityListingBinding
import com.bunchapp.model.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook


class ListingActivity : AppCompatActivity() {
    private val dbHelper = DBHelper(this, null)
    private var binding: ActivityListingBinding? = null
    private val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_listing)
        init()
    }

    private fun init() {
        binding?.recyclerviewList?.setLayoutManager(LinearLayoutManager(this))
        lifecycleScope.launch {
            getData()
        }
        binding?.btnExport?.setOnClickListener {
            exportToExcel()
        }
    }

    private suspend fun getData() {
        var list: List<Employee>
        withContext(Dispatchers.IO)
        {
            list = dbHelper.getEmployee()!!
            withContext(Dispatchers.Main) {
                if (list.isNotEmpty()) {
                    binding?.recyclerviewList?.visibility = View.VISIBLE
                    binding?.tvNoData?.visibility = View.GONE
                    binding?.btnExport?.isEnabled = true
                    binding?.recyclerviewList?.adapter =
                        EmployeeAdapter(list, this@ListingActivity)
                } else {
                    binding?.tvNoData?.visibility = View.VISIBLE
                    binding?.recyclerviewList?.visibility = View.GONE
                    binding?.btnExport?.isEnabled = false
                }
            }
        }
    }

    //Export to excel
    private fun exportToExcel() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/vnd.ms-excel"
            putExtra(Intent.EXTRA_TITLE, "data.xls")
        }
        startActivityForResult(intent, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION && resultCode == RESULT_OK) {
            data?.data?.also { uri ->
                GlobalScope.launch(Dispatchers.IO) {
                    exportDataToExcel(uri)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private suspend fun exportDataToExcel(uri: Uri) = withContext(Dispatchers.IO) {
        contentResolver.openOutputStream(uri)?.use {
            val cursor = dbHelper.getEmployeeExcel()
            // Add data to the sheet here
            if (cursor != null && cursor.count > 0) {
                // Set up the Excel workbook and sheet
                val wb = XSSFWorkbook()
                val sheet = wb.createSheet("EmployeeData")

                // add table header
                val headerRow: Row = sheet.createRow(0)
                headerRow.createCell(0).setCellValue("EmpID")
                headerRow.createCell(1).setCellValue("EmpName")

                // retrieve data from the SQLite database and add it to the Excel sheet
                var rowIndex = 1
                while (cursor.moveToNext()) {
                    val dataRow: Row = sheet.createRow(rowIndex++)
                    dataRow.createCell(0).setCellValue(cursor.getInt(0).toString())
                    dataRow.createCell(1).setCellValue(cursor.getString(1))
                }

                // Create cell styles
                val styles = Array(6) { wb.createCellStyle() }.apply {
                    // Set border styles for each cell style
                    this[0].borderTop = BorderStyle.THIN
                    this[0].borderLeft = BorderStyle.THIN
                    this[1].borderTop = BorderStyle.THIN
                    this[1].borderRight = BorderStyle.THIN
                    this[2].borderBottom = BorderStyle.THIN
                    this[2].borderLeft = BorderStyle.THIN
                    this[3].borderBottom = BorderStyle.THIN
                    this[3].borderRight = BorderStyle.THIN
                    this[4].borderLeft = BorderStyle.THIN
                    this[5].borderRight = BorderStyle.THIN

                    // Set border colors for each cell style
                    val color = IndexedColors.BLACK.getIndex()
                    this[2].bottomBorderColor = color
                    this[3].bottomBorderColor = color
                    this[1].rightBorderColor = color
                    this[1].topBorderColor = color
                    this[3].rightBorderColor = color
                    this[4].topBorderColor = color
                    this[4].leftBorderColor = color
                    this[0].topBorderColor = color
                    this[0].leftBorderColor = color
                    this[2].leftBorderColor = color
                    this[2].bottomBorderColor = color
                }

                // Loop through cells and set the appropriate style
                for (i in 0 until rowIndex) {
                    val row = sheet.getRow(i) ?: sheet.createRow(i)
                    for (j in 0..1) {
                        val cell = row.getCell(j) ?: row.createCell(j)
                        cell.cellStyle = when {
                            i == (rowIndex - 1) && j == 0 -> styles[2]
                            i == (rowIndex - 1) && j == 1 -> styles[3]
                            i == 0 && j == 1 -> styles[1]
                            i == 0 && j == 0 -> styles[0]
                            j == 0 -> styles[4]
                            else -> styles[5]
                        }
                    }
                }


                wb.write(it)
                wb.close()

                Log.d("PRT", "exportDataToExcel: Success")
                // Create an intent to send the Excel file via email
                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/vnd.ms-excel"
                    putExtra(Intent.EXTRA_EMAIL, "prashant.thakkar@yahoo.com")
                    putExtra(Intent.EXTRA_SUBJECT, "Excel File")
                    putExtra(Intent.EXTRA_TEXT, "Please find attachment!")
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    `package` = "com.google.android.gm"
                }

                // Launch the Gmail app
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        "Excel file created successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(emailIntent)
                }
            } else {
                // Display toast message
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        "Excel file creation Failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.d("PRT", "exportDataToExcel: Failed")
            }
        }
    }
}