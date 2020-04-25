package com.example.multi_reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_drink_daily.*
import kotlinx.android.synthetic.main.activity_drink_daily.et_message
import kotlinx.android.synthetic.main.activity_drink_daily.floatingSettings
import kotlinx.android.synthetic.main.activity_exercise.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.util.*

class Exercise : AppCompatActivity() {

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
                toast("jee")
                val reminder = Reminder(
                    uid = null,
                    time = calendar.timeInMillis,
                    location = null,
                    message = et_message.text.toString()
                )

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
        
        
        
        
        
        
        floatingSettings.setOnClickListener{
            val layoutInflater : LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view: View = layoutInflater.inflate(R.layout.popup_settings, null)

            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true

            val popupWindow = PopupWindow(view,
                width,
                height,
                focusable
            )
            val buttonBG = view.findViewById<Button>(R.id.buttonBackground)
            val buttonDR = view.findViewById<Button>(R.id.buttonDelete)

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

            buttonBG.setOnClickListener{
                popupWindow.dismiss()
                val layoutInflater2 : LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view2: View = layoutInflater2.inflate(R.layout.popup_background, null)
                val width = LinearLayout.LayoutParams.WRAP_CONTENT
                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                val focusable = true
                val popupWindow2 = PopupWindow(view2,
                    width,
                    height,
                    focusable)
                popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)

                val buttonCancel = view2.findViewById<Button>(R.id.buttonCancelBackground)
                buttonCancel.setOnClickListener{popupWindow2.dismiss()}
            }

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
            }



        }
    }

    private fun setAlarm(time: Long, message: String) {
        val intent = Intent(this, ReminderReceiver::class.java)
        intent.putExtra("message", message)

        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)

        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setExact(AlarmManager.RTC, time, pendingIntent)

        runOnUiThread{toast("Reminder is created")}

    }
}
