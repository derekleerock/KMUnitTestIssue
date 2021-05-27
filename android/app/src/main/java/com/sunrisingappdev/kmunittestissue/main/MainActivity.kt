package com.sunrisingappdev.kmunittestissue.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunrisingappdev.kmunittestissue.R
import com.sunrisingappdev.kmunittestissue.createApplicationScreenMessage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        versionInfo.text = createApplicationScreenMessage()
    }
}
