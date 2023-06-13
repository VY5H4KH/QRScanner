package com.example.metroknappp.ui

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.metroknappp.R
import android.hardware.camera2.CameraManager
import com.example.metroknappp.databinding.ActivityLogoutAcivityBinding
import com.google.zxing.integration.android.IntentIntegrator
@Suppress("DEPRECATION")
class LogoutActivity : AppCompatActivity() {
    private var ison=false
    private var cameraflash = false
    private lateinit var binding: ActivityLogoutAcivityBinding
    private var qrScanIntegrator: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutAcivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        cameraflash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        checkflash()
        setOnClickFlash()
        setOnClickListener()
        setupScanner()
        setOnClickLogOut()
    }

    private fun checkflash() {
        if(ison) {
            val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
            var cameraId: String
            try{
                cameraId = cameraManager.cameraIdList[0]
                cameraManager.setTorchMode(cameraId,true)
            }
            catch (e: Exception){}
        }
        else {
            val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
            var cameraId: String
            try{
                cameraId = cameraManager.cameraIdList[0]
                cameraManager.setTorchMode(cameraId,false)
            }
            catch (e: Exception){}
        }
    }

    private fun setOnClickFlash() {
        binding.flash.setOnClickListener {
            if(cameraflash)
            {
                if(!ison){
                    binding.flash.setBackgroundResource(R.drawable.flashon)
                    ison=true
                    val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
                    var cameraId: String
                    try{
                        cameraId = cameraManager.cameraIdList[0]
                        cameraManager.setTorchMode(cameraId,true)
                    }
                    catch (e: Exception){}
                }
                else{
                    binding.flash.setBackgroundResource(R.drawable.flash)
                    ison=false
                    val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
                    var cameraId: String
                    try{
                        cameraId = cameraManager.cameraIdList[0]
                        cameraManager.setTorchMode(cameraId,false)
                    }
                    catch (e: Exception){}
                }
            }
        }
    }

    private fun setupScanner() {
        qrScanIntegrator = IntentIntegrator(this)
        qrScanIntegrator?.setOrientationLocked(false)
        qrScanIntegrator?.setBeepEnabled(false)
        qrScanIntegrator?.setPrompt("Volume up to Flash on")
    }

    private fun setOnClickLogOut() {
        binding.btnLogout.setOnClickListener {
            val intentLog = Intent(this,SecondActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity((intentLog))
        }
    }

    private fun setOnClickListener() {
        binding.btnScan.setOnClickListener { performAction() }
    }

    private fun performAction() {
        if(ison) {
            qrScanIntegrator?.setTorchEnabled(true)
        }
        else {
            qrScanIntegrator?.setTorchEnabled(false)
        }
        qrScanIntegrator?.initiateScan()

    }

    override fun onBackPressed() {
        binding.flash.setBackgroundResource(R.drawable.flash)
        ison=false
        val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        var cameraId: String
        try{
            cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId,false)
        }
        catch (e: Exception){}
        moveTaskToBack(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if(result.contents==null) {
                Toast.makeText(this,"INVALID QR CODE",Toast.LENGTH_SHORT).show()
            }
            else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Result")
                builder.setMessage(result.contents)
                Toast.makeText(this, "VERIFIED", Toast.LENGTH_SHORT).show()
                builder.setPositiveButton("OK") { dialog: DialogInterface, which: Int -> dialog.dismiss()
                    checkflash()
                }
                    .show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        checkflash()
    }
}