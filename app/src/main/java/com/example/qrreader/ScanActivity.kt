package com.example.qrreader

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode

private const val CAMERA_REQUEST_CODE =101          //to request camera access

class ScanActivity : AppCompatActivity() {

    private lateinit var QRScanner: CodeScanner       // A CodeScanner variable to use codescanner library features

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val QRText: TextView = findViewById(R.id.QRText)


        setupPermissions()          //A function to check for camera access permissions

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)      //Bringing in Scanner View to scan QR/Barcode
        QRScanner = CodeScanner(this, scannerView)    //Initializing


        // Passing Default Values to Parameters here
        QRScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        QRScanner.camera = CodeScanner.CAMERA_BACK //Choosing which camera to use either Front of Back
        QRScanner.scanMode = ScanMode.CONTINUOUS //Setting scan mode to Continuous to not Scan SINGLE but CONTINUOUS
        QRScanner.autoFocusMode = AutoFocusMode.SAFE // Providing Auto Focus Mode
        QRScanner.isAutoFocusEnabled = true // Enabling Auto Focus Mode


        QRScanner.decodeCallback = DecodeCallback {         //To Configure what to do when scanner decodes the code
            runOnUiThread {

                if (it.text.contains("https://") || it.text.contains("http://")) {

                    //if the translated QR text is a website URL then Go to the URL using Implicit Intent
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.text))
                    startActivity(intent)

                }
                else{  //else Show translated text
                    QRText.text= "\t\t\t\t Translated Text \n\n ${it.text}"
                }
            }
        }

        //Only necessary if the ScanMode is not set to CONTINUOUS
        scannerView.setOnClickListener {        //Tells the QRScanner to start scanning for the code
            QRScanner.startPreview()
        }

    }

    //This only needs to be done for API>=23
    private fun setupPermissions(){         //Function to Ask for Camera Permission from the User
        val permission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.CAMERA)

        if(permission!= PackageManager.PERMISSION_GRANTED){          //if permission not granted then ask for camera access
            makeRequest()
        }
    }

    private fun  makeRequest(){             //used to make request for camera access
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        //overidding request permissions result function
        //the purpose was to check if permission is granted or not if not granted then make a toast

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE->{
                if(grantResults.isEmpty() || grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Camera Permission not granted, Relaunch or Provide Access",
                        Toast.LENGTH_SHORT).show()
                }
                else{
                    //no need for code because camera access was granted
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()                //if apps in minimized and reopened then tell the QRScanner to start the scan
        QRScanner.startPreview()
    }

    override fun onPause() {            //if we exit the app then release Resources
        QRScanner.releaseResources()
        super.onPause()
    }
}