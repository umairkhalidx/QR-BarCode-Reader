package com.example.qrreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn : Button = findViewById(R.id.Scan_btn)
        val image : ImageView = findViewById(R.id.Logo)
        val txt : TextView= findViewById(R.id.textView2)

        btn.setOnClickListener(){   //To perform action when button is pressed

            var i = Intent(this, ScanActivity::class.java)      //To move to next activity
            startActivity(i)
        }


    }

}