package com.example.multi_reminder

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.activity_drink_daily.*

class Cleaning : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cleaning)

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

            val buttonDR = view.findViewById<Button>(R.id.buttonDelete)

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

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
}
