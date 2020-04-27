package com.example.multi_reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_drink_daily.et_message
import kotlinx.android.synthetic.main.activity_drink_daily.floatingSettings
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup_deletereminders.*
import kotlinx.android.synthetic.main.popup_deletereminders.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
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

                val reminder = Reminder(
                    uid = null,
                    time = calendar.timeInMillis,
                    location = null,
                    message = et_message.text.toString()
                )

                // Only place the reminder to the exercise screen

                val sdf = SimpleDateFormat("HH:mm dd.MM.yyyy")
                sdf.timeZone = TimeZone.getDefault()

                itemMessageEX.text = reminder.message
                val timeEX = sdf.format(reminder.time)
                itemTriggerEX.text =  timeEX
                //
                //toast("reminder set")

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

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

            //val buttonBG = view.findViewById<Button>(R.id.buttonBackground)
            //buttonBG.setOnClickListener{
              //  popupWindow.dismiss()
               // val layoutInflater2 : LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
               // val view2: View = layoutInflater2.inflate(R.layout.popup_background, null)
               // val width = LinearLayout.LayoutParams.WRAP_CONTENT
               // val height = LinearLayout.LayoutParams.WRAP_CONTENT
               // val focusable = true
               // val popupWindow2 = PopupWindow(view2,
               //     width,
               //     height,
               //     focusable)
               // popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)

                //val buttonCancel = view2.findViewById<Button>(R.id.buttonCancelBackground)
                //buttonCancel.setOnClickListener{popupWindow2.dismiss()}
            //}

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
                    //pitäisi vielä poistaa databasesta jos se on edes siellä

                    //niin poistaminen tapahtuu varmaan:
                    //dp.reminderDao().delete(reminder) //tän tabin remandereiden Uid pitäs vain kaivaa jotenkin
                    //dp.close()
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

        runOnUiThread{toast("Reminder is created")}

    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }



    private fun refreshList() {
        doAsync {

            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "reminders")
                .build()
            val reminders = db.reminderDao().getReminders()
            db.close()

            uiThread {
                if (reminders.isNotEmpty()) {
                    val adapter = ReminderAdapter(applicationContext, reminders)
                    list.adapter = adapter
                } else {

                    toast("No reminders yet")
                }

            }

        }
    }
}
