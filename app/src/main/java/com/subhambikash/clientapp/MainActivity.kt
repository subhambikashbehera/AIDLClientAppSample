package com.subhambikash.clientapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import com.subhambikash.serverapp.IMyAidlInterface

class MainActivity : AppCompatActivity() {


    companion object{
        const val TAG = "mainActivity"
    }

    private var mService: IMyAidlInterface?=null
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "service connected: ${Thread.currentThread().name}")
            mService = IMyAidlInterface.Stub.asInterface(service)
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "service disconnected: ${Thread.currentThread().name}")
            mService = null
        }
    }


    private lateinit var startService: Button
    private lateinit var stopService: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myPid = android.os.Process.myPid()
        startService = findViewById(R.id.startService)
        stopService = findViewById(R.id.stopService)
        Thread.currentThread().name = "MainActivity"

        // Bind to the service
        val intent = Intent(IMyAidlInterface::class.java.name)
        intent.setPackage("com.subhambikash.serverapp")
        Log.d(TAG, "Binding to service...")
        val bindingResult = bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        Log.d(TAG, "Binding result: $bindingResult")
        Log.d(TAG, "Binding result: ${IMyAidlInterface::class.java.name}")





        startService.setOnClickListener {
            Log.d(TAG, "main activity thread id :  ${Thread.currentThread().id}")
            Log.d(TAG, "main activity thread name: ${Thread.currentThread().name}")
            Log.d(TAG, "main activity process: $myPid")
            try {
                mService?.sendMessage("Hello from process $myPid")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        stopService.setOnClickListener {
            Log.d(TAG, "main activity thread id :  ${Thread.currentThread().id}")
        }

    }
}