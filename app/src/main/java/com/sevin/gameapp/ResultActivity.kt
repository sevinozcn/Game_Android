package com.sevin.gameapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import com.sevin.gameapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding : ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val skor = intent.getIntExtra("skor",0)
        binding.textViewSumScore.text = skor.toString()

        val sp = getSharedPreferences("Sonuc", Context.MODE_PRIVATE)
        val heighScore = sp.getInt("heighScore",0)

        if(skor > heighScore){

            val editor = sp.edit()
            editor.putInt("heighScore",skor)
            editor.commit()

            binding.textViewHighScore.text = skor.toString()
        }else{
        binding.textViewHighScore.text = heighScore.toString()
        }

        binding.buttonrepeat.setOnClickListener {
            startActivity(Intent(this@ResultActivity,MainActivity::class.java))
            finish()
        }

    }
}