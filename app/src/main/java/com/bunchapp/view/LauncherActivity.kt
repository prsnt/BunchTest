package com.bunchapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bunchapp.R
import com.bunchapp.databinding.ActivityLauncherBinding
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

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

        AppCenter.start(
            application, "bf2c1c83-5a3b-4023-b524-45d770e58e2e",
            Analytics::class.java, Crashes::class.java
        )
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