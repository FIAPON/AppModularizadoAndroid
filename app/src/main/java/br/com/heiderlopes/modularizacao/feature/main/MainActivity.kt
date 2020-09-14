package br.com.heiderlopes.modularizacao.feature.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.heiderlopes.modularizacao.R
import br.com.heiderlopes.modularizacao.extensions.visible
import br.com.heiderlopes.modularizacao.feature.listproducts.ProductListActivity
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var splitInstallManager: SplitInstallManager
    lateinit var request: SplitInstallRequest
    val DYNAMIC_FEATURE = "about_feature"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDynamicModules()

        setClickListeners()

    }

    private fun initDynamicModules() {
        splitInstallManager = SplitInstallManagerFactory.create(this)
        request = SplitInstallRequest
            .newBuilder()
            .addModule(DYNAMIC_FEATURE)
            .build()
    }

    private fun setClickListeners() {

        btProducts.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProductListActivity::class.java))
        }

        btDownloadAbout.setOnClickListener {
            if (!isDynamicFeatureDownloaded(DYNAMIC_FEATURE)) {
                downloadFeature()
            } else {
                btDeleteNewsModule.visible(true)
                btOpenNewsModule.visible(true)
                btDownloadAbout.visible(false)
            }
        }

        btOpenNewsModule.setOnClickListener {
            //Chama a classe do módulo a ser executado
            val intent = Intent().setClassName(this, "br.com.heiderlopes.about_feature.AboutActivity")
            startActivity(intent)
        }

        btDeleteNewsModule.setOnClickListener {
            val list = ArrayList<String>()
            list.add(DYNAMIC_FEATURE)
            uninstallDynamicFeature(list)
        }
    }

    private fun uninstallDynamicFeature(list: List<String>) {
        splitInstallManager.deferredUninstall(list)
            .addOnSuccessListener {
                btDeleteNewsModule.visible(false)
                btOpenNewsModule.visible(false)
                btDownloadAbout.visible(true)
            }
            .addOnCompleteListener {
                Log.i("TAG", "DEU RUIM")
            }
    }

    private fun isDynamicFeatureDownloaded(feature: String): Boolean =
        splitInstallManager.installedModules.contains(feature)

    private fun downloadFeature() {
        splitInstallManager.startInstall(request)
            .addOnFailureListener {
            }
            .addOnSuccessListener {
                btOpenNewsModule.visible(true)
                btDeleteNewsModule.visible(true)
                btDownloadAbout.visible(false)
            }
            .addOnCompleteListener {
            }
    }

    //Check status download dynamic module
    /*var mySessionId = 0
    val listener = SplitInstallStateUpdatedListener {
        mySessionId = it.sessionId()
        when (it.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                val totalBytes = it.totalBytesToDownload()
                val progress = it.bytesDownloaded()
                // Update progress bar.
            }
            SplitInstallSessionStatus.INSTALLING -> Log.d("Status", "INSTALLING")
            SplitInstallSessionStatus.INSTALLED -> Log.d("Status", "INSTALLED")
            SplitInstallSessionStatus.FAILED -> Log.d("Status", "FAILED")
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> Log.d("Status", "REQUIRES_USER_CONFIRMATION")
        }
    }*/
}
