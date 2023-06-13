package com.example.metroknappp.ui

import android.content.Intent
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.metroknappp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        var cameraId: String
        try{
            cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId,false)
        }
        catch (e: Exception){}


        binding.btnLogin.setOnClickListener {
            doLogin()

        }


    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    fun doLogin() {
        val email = binding.txtInputEmail.text.toString()
        val pwd = binding.txtPass.text.toString()
        if (email=="user" && pwd=="123"){
            val intent = Intent(this, LogoutActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

    }

}