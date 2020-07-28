package com.free.simpledynamicfeatureapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val manager: SplitInstallManager by lazy {
        SplitInstallManagerFactory.create(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        download_module.setOnClickListener {
            val request = SplitInstallRequest.newBuilder()
                .addModule("dynamicfeature")
                .build()

            manager.startInstall(request)
        }

        open_module.setOnClickListener {
            val isEnabled = manager.installedModules.contains("dynamicfeature")
            if(isEnabled){
                val intent = Intent()
                intent.setClassName(this, "com.free.dynamicfeature.OnDemandActivity")
                startActivity(intent)
            }
        }

        manager.registerListener {
            when (it.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> showToast("Downloading feature")
                SplitInstallSessionStatus.INSTALLED -> {
                    showToast("Feature is ready")
                    open_module.isEnabled = true
                }
                else -> { /* Do nothing in this example */ }
            }
        }

    }

    private fun showToast(message: String){
        Toast.makeText(this, message,Toast.LENGTH_LONG).show()
    }
}
