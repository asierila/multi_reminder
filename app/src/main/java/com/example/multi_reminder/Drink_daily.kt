package com.example.multi_reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
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
import kotlinx.android.synthetic.main.activity_drink_daily.*
import org.jetbrains.anko.toast

class Drink_daily : AppCompatActivity() {


    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_daily)



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

            //Ollin alempi




            //val buttonBG = view.findViewById<Button>(R.id.buttonPhoto)
            val buttonBG = view.findViewById<Button>(R.id.buttonBackground)
            val buttonDR = view.findViewById<Button>(R.id.buttonDelete)
            val buttonDrink = view.findViewById<Button>(R.id.buttonDrink)
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)


            //below code is from when trying to make a now scrapped feature with camera
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
                    val PERMISSION_CODE_CAMERA = 1000



                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ((checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                        ) {
                            val permission = arrayOf(android.Manifest.permission.CAMERA)
                            requestPermissions(permission, PERMISSION_CODE_CAMERA)


                        } else {

                            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                                takePictureIntent.resolveActivity(packageManager)?.also {
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                                }
                            }
                        }
                    }

                }
                //Old useless code above, would've appeared on all the reminder views.


                buttonDR.setOnClickListener {
                    popupWindow.dismiss()
                    val layoutInflater3: LayoutInflater =
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view3: View = layoutInflater3.inflate(R.layout.popup_deletereminders, null)
                    val width = LinearLayout.LayoutParams.WRAP_CONTENT
                    val height = LinearLayout.LayoutParams.WRAP_CONTENT
                    val focusable = true
                    val popupWindow3 = PopupWindow(
                        view3,
                        width,
                        height,
                        focusable
                    )
                    popupWindow3.showAtLocation(view, Gravity.CENTER, 0, 0)

                    val buttonYes = view3.findViewById<Button>(R.id.buttonChooseYes)
                    val buttonNo = view3.findViewById<Button>(R.id.buttonChooseNo)
                    buttonNo.setOnClickListener { popupWindow3.dismiss() }
                }


            }


        }



        fun setAlarm(time: Long, message: String) {
            val intent = Intent(this, ReminderReceiver::class.java)
            intent.putExtra("message", message)

            val pendingIntent =
                PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)

            val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            manager.setExact(AlarmManager.RTC, time, pendingIntent)

            runOnUiThread { toast("Reminder is created") }

        }
    }



    //We tried to make taking pictures with camera and then replacing button icons with then
    //app manages to open the camera, but after taking the picture the app crashes
    //the problem is most likely the code below, but we just cannot figure it out so
    //the feature was left out




}
