package uz.ssh.flashcard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    private var cameraManager: CameraManager? = null
    private var isFlashlightOn = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<ImageButton>(R.id.btn)

        if (checkCameraPermission()) {
            // Initialize camera manager
            cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }

        button.setOnClickListener() {

            if (isFlashlightOn) {
                turnOffFlashlight()
                button.setImageResource(R.drawable.turn_off)
                isFlashlightOn = false
            } else {
                turnOnFlashlight()
                button.setImageResource(R.drawable.turn_on)


                for (i in 0..100000) {

                    turnOnFlashlight()

                    Thread.sleep(3000)
                    turnOffFlashlight()
                    Thread.sleep(3000)
                }
                isFlashlightOn = true
            }

        }
    }

    private fun checkCameraPermission(): Boolean {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, proceed
            cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            return true
        } else {
            // Permission not granted, request it
            requestPermissions(
                arrayOf(android.Manifest.permission.CAMERA),
                1
            )
            return false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize camera manager
                cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

            } else {
                // Permission denied, inform the user
                Toast.makeText(
                    this,
                    "Camera permission is required to use flashlight",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun turnOnFlashlight() {
        cameraManager?.let { cameraManager ->
            try {
                val cameraId = cameraManager.cameraIdList[0] // Assuming we only have one camera
                cameraManager.setTorchMode(cameraId, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun turnOffFlashlight() {
        cameraManager?.let { cameraManager ->
            try {
                val cameraId = cameraManager.cameraIdList[0] // Assuming we only have one camera
                cameraManager.setTorchMode(cameraId, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}