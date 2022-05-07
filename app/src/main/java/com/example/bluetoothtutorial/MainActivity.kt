package com.example.bluetoothtutorial

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.bluetoothtutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ENABLE_BT:Int = 1;
    private val REQUEST_CODE_DISCOVERABLE_BT:Int = 1;
    private val BLUETOOTH_PERMISSION_REQUEST_CODE = 9999

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding



    //    bluetooth adapter
    lateinit var bAdapter: BluetoothAdapter

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initializeBluetoothOrRequestPermission() {
        val requiredPermissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            listOf(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            listOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
        }

        val missingPermissions = requiredPermissions.filter { permission ->
            checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
        }
        if (!missingPermissions.isEmpty()) {


            requestPermissions(missingPermissions.toTypedArray(), BLUETOOTH_PERMISSION_REQUEST_CODE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        initializeBluetoothOrRequestPermission()
        setContentView(R.layout.activity_main)

        bAdapter = BluetoothAdapter.getDefaultAdapter()

        val bluetoothStatusTv: TextView = findViewById(R.id.bluetoothStatusTv) as TextView
        val pairedTv: TextView = findViewById(R.id.pairedTv) as TextView

        val bluetoothStatusIv: ImageView = findViewById(R.id.bluetoothStatusIv) as ImageView

        val turnOnBtn: Button = findViewById(R.id.turnOnBtn) as Button
        val turnOffBtn: Button = findViewById(R.id.turnOffBtn) as Button
        val discoverableBtn: Button = findViewById(R.id.discoverableBtn) as Button

        if (bAdapter == null) {
            bluetoothStatusTv.text = "not available"
        }
        else{
            bluetoothStatusTv.text = "bluetooth is available"
        }

        if(bAdapter.isEnabled){
            bluetoothStatusIv.setImageResource(R.drawable.ic_bluetooth_on)
        }
        else{
            bluetoothStatusIv.setImageResource(R.drawable.ic_bluetooth_off)
        }

        /*turn on bluetooth*/
        turnOnBtn.setOnClickListener{
            if(bAdapter.isEnabled){
                // bluetooth already enabled
                Toast.makeText(this, "Already on", Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BT);
            }
        }

        /*turn off bluetooth*/
        turnOffBtn.setOnClickListener{
            if(!bAdapter.isEnabled){
                // bluetooth already enabled
                Toast.makeText(this, "Already off", Toast.LENGTH_LONG).show()
            }
            else{
                bAdapter.disable()
                bluetoothStatusIv.setImageResource(R.drawable.ic_bluetooth_off)
                Toast.makeText(this, "Bluetooth turned off", Toast.LENGTH_LONG).show()
            }
        }

        discoverableBtn.setOnClickListener{
            if(!bAdapter.isDiscovering){
                // bluetooth already enabled
                Toast.makeText(this, "Make your device discoverable", Toast.LENGTH_LONG).show()
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                startActivityForResult(intent, REQUEST_CODE_DISCOVERABLE_BT);
            }
        }

        // get list of paired devices
        if(bAdapter.isEnabled){
            pairedTv.text = "Paired Devices"
            // get list of paired devices
            val devices = bAdapter.bondedDevices
            for(device in devices){
                val deviceName = device.name
                val deviceAddress = device.address
                pairedTv.append("\nDevice: $deviceName, $deviceAddress ")
            }
        }
        else{
            Toast.makeText(this, "Turn on bluetooth first", Toast.LENGTH_LONG).show()
        }

    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        when(requestCode){
//            REQUEST_CODE_ENABLE_BT ->
//                if(requestCode == Activity.RESULT_OK){
//                    bluetoothStatusIv.setImageResource(R.drawable.ic_bluetooth_on)
//                    Toast.makeText(this, "Bluetooth is on", Toast.LENGTH_LONG).show()
//                }
//                else{
//                    Toast.makeText(this, "Could not turn on bluetooth", Toast.LENGTH_LONG).show()
//                }
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }

}

