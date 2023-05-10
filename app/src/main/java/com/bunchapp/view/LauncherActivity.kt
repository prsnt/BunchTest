package com.bunchapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bunchapp.R
import com.bunchapp.databinding.ActivityLauncherBinding
import com.bunchapp.databinding.ActivityListingBinding

class LauncherActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityLauncherBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launcher)
        init()
    }

    private fun init() {
        binding?.btnEmpAdd?.setOnClickListener(this)
        binding?.btnEmpList?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnEmpAdd -> {
                startActivity(Intent(this, AddEmployeeActivity::class.java))
            }
            R.id.btnEmpList -> {
                startActivity(Intent(this, ListingActivity::class.java))
            }
        }
    }
}