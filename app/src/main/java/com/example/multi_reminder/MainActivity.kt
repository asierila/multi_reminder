package com.example.multi_reminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        buttonDrink.setOnClickListener{
            val intent = Intent(applicationContext, Drink_daily::class.java)
            startActivity(intent)
        }

        buttonEat.setOnClickListener{
            val intent = Intent(applicationContext, Eat_daily::class.java)
            startActivity(intent)
        }

        buttonWashHands.setOnClickListener{
            val intent = Intent(applicationContext, Hands_daily::class.java)
            startActivity(intent)
        }

        buttonShower.setOnClickListener{
            val intent = Intent(applicationContext, Shower::class.java)
            startActivity(intent)
        }

        buttonClean.setOnClickListener{
            val intent = Intent(applicationContext, Cleaning::class.java)
            startActivity(intent)
        }

        buttonWorkout.setOnClickListener{
            val intent = Intent(applicationContext, Exercise::class.java)
            startActivity(intent)
        }

        buttonCustom.setOnClickListener{
            val intent = Intent(applicationContext, Custom::class.java)
            startActivity(intent)
        }

    }
}
