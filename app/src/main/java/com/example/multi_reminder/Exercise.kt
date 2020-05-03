package com.example.multi_reminder

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_exercise.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class Exercise : AppCompatActivity() {


    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)



        time_createEx.setOnClickListener {


            val calendar = GregorianCalendar(
                datePickerEx.year,
                datePickerEx.month,
                datePickerEx.dayOfMonth,
                timePickerEx.currentHour,
                timePickerEx.currentMinute

            )


            if ((et_message.text.toString() != "" ) && (calendar.timeInMillis > System.currentTimeMillis())){

                val reminder = Reminder(
                    uid = null,
                    time = calendar.timeInMillis,
                    location = null,
                    message = et_message.text.toString()
                )

                val sdf = SimpleDateFormat("HH:mm dd.MM.yyyy")
                sdf.timeZone = TimeZone.getDefault()

                itemMessageEX.text = reminder.message
                val timeEX = sdf.format(reminder.time)
                itemTriggerEX.text =  timeEX


                doAsync {
                    val dp = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java,
                        "reminders"
                    ).build()

                    dp.reminderDao().insert(reminder)
                    dp.close()

                    setAlarm(reminder.time!!, reminder.message)

                    finish()
                }
            }else{
                toast("Wrong data")
            }

        }


        floatingSettings.setOnClickListener {
            val layoutInflater: LayoutInflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view: View = layoutInflater.inflate(R.layout.popup_settings, null)

            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true

            val popupWindow = PopupWindow(
                view,
                width,
                height,
                focusable
            )


            val buttonBG = view.findViewById<Button>(R.id.buttonBackground)

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)



            val buttonDR = view.findViewById<Button>(R.id.buttonDelete)
            buttonDR.setOnClickListener{
                popupWindow.dismiss()
                val layoutInflater3 : LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view3: View = layoutInflater3.inflate(R.layout.popup_deletereminders, null)
                val width = LinearLayout.LayoutParams.WRAP_CONTENT
                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                val focusable = true
                val popupWindow3 = PopupWindow(view3,
                    width,
                    height,
                    focusable)
                popupWindow3.showAtLocation(view, Gravity.CENTER, 0, 0)

                val buttonYes = view3.findViewById<Button>(R.id.buttonChooseYes)
                val buttonNo = view3.findViewById<Button>(R.id.buttonChooseNo)



                buttonNo.setOnClickListener{popupWindow3.dismiss()}
                buttonYes.setOnClickListener {
                    itemMessageEX.text = null
                    itemTriggerEX.text = null
                    popupWindow3.dismiss()
                }
            }



            buttonBG.setOnClickListener {
                popupWindow.dismiss()
                val layoutInflater2: LayoutInflater =
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view2: View = layoutInflater2.inflate(R.layout.popup_background, null)
                val width = LinearLayout.LayoutParams.WRAP_CONTENT
                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                val focusable = true
                val popupWindow2 = PopupWindow(
                    view2,
                    width,
                    height,
                    focusable
                )
                popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)

                val buttonCancel = view2.findViewById<Button>(R.id.buttonCancelBackground)
                buttonCancel.setOnClickListener { popupWindow2.dismiss() }

                val buttonCamera = view2.findViewById<Button>(R.id.buttonPhoto)
                val buttonGallery = view2.findViewById<Button>(R.id.buttonGallery)

                buttonCamera.setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(android.Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED
                        ) {
                            val permission = arrayOf(
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                            requestPermissions(permission, PERMISSION_CODE)
                        } else {

                            openCamera()
                        }
                    } else {
                        openCamera()
                    }
                }

                buttonGallery.setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(android.Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED
                        ) {
                            val permission = arrayOf(
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                            requestPermissions(permission, PERMISSION_CODE)
                        } else {

                            pickImageFromGallery()
                        }
                    } else {
                        pickImageFromGallery()
                    }
                }
            }
        }
    }

    private fun setAlarm(time: Long, message: String) {

        val intent = Intent(this, ReminderReceiver::class.java)
        intent.putExtra("message", message)

        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)

        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setExact(AlarmManager.RTC, time, pendingIntent)

        runOnUiThread { toast("Reminder is created") }


    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New picturer")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the gamera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)


    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    toast("Jee")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            image_view_ex.setImageURI(image_uri)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            image_view_ex.setImageURI(data?.data)
        }
    }

}