package com.sevin.gameapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.sevin.gameapp.databinding.ActivityScreenBinding
import java.lang.Math.floor
import java.util.Timer
import kotlin.concurrent.schedule

class ScreenActivity : AppCompatActivity() {

    //Positions
    private var MainCharacterX = 0.0f
    private var MainCharacterY = 0.0f
    private var blackSquareX = 0.0f
    private var blackSquareY = 0.0f
    private var yellowCircleX = 0.0f
    private var yellowCircleY = 0.0f
    private var pinkTriangleX = 0.0f
    private var pinkTriangeY = 0.0f


    //Sizes
    private var screenWeigth = 0
    private var screenHigh = 0
    private var chterWeigth = 0
    private var chterHigh = 0

    //Controls
    private var touchControl = false
    private var startupControl = false

    private val timer = Timer()
    private var skor = 0

    private lateinit var binding: ActivityScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.blackSquare.x = -80.0f
        binding.blackSquare.y = -80.0f
        binding.yellowCircle.x = -80.0f
        binding.yellowCircle.y = -80.0f
        binding.pinkTriangle.x = -80.0f
        binding.pinkTriangle.y = -80.0f


        binding.cl.setOnTouchListener (object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if(startupControl){ //True
                    if(event?.action == MotionEvent.ACTION_DOWN){
                        Log.e("MotionEvent","ACTION_DOWN : Touch The Screen")
                        touchControl = true
                    }
                    if(event?.action == MotionEvent.ACTION_UP){
                        Log.e("MotionEvent","ACTION_UP : Leave The Screen")
                        touchControl = false
                    }
                }else{ //False

                    startupControl = true

                    binding.textViewGameStart.visibility = View.INVISIBLE

                    MainCharacterX = binding.character.x
                    MainCharacterY = binding.character.y

                    chterWeigth = binding.character.width
                    chterHigh = binding.character.height
                    screenWeigth = binding.cl.width
                    screenHigh = binding.cl.height

                    timer.schedule(0,20){
                        Handler(Looper.getMainLooper()).post {
                            MainCharacterMovement()
                            Moved()
                            collisionControl()
                        }
                    }
                }
                return true
            }
        })

    }

    fun MainCharacterMovement() {

        val mainCharacterSpeed = screenHigh/60.0f

        if(touchControl){
            MainCharacterY-=mainCharacterSpeed //UP -> touchControl:True
        }else{
            MainCharacterY+=mainCharacterSpeed  //DOWN -> touchControl:True
        }

        if(MainCharacterY <= 0.0f){
            MainCharacterY = 0.0f
        }
        if(MainCharacterY >= screenHigh - chterHigh) {
            MainCharacterY = (screenHigh - chterHigh).toFloat()
        }
        binding.character.y = MainCharacterY
    }

    fun Moved(){

        blackSquareX-= screenWeigth/44.0f
        yellowCircleX-= screenWeigth/54.0f
        pinkTriangleX-= screenWeigth/36.0f

        if(blackSquareX < 0.0f){
            blackSquareX = screenWeigth + 20.0f
            blackSquareY = floor(Math.random() * screenHigh).toFloat()
        }
        binding.blackSquare.x = blackSquareX
        binding.blackSquare.y = blackSquareY

        if(yellowCircleX < 0.0f){
            yellowCircleX = screenWeigth + 20.0f
            yellowCircleY = floor(Math.random() * screenHigh).toFloat()
        }
        binding.yellowCircle.x = yellowCircleX
        binding.yellowCircle.y = yellowCircleY

        if(pinkTriangleX < 0.0f){
            pinkTriangleX = screenWeigth + 20.0f
            pinkTriangeY = floor(Math.random() * screenHigh).toFloat()
        }
        binding.pinkTriangle.x = pinkTriangleX
        binding.pinkTriangle.y = pinkTriangeY

    }

    fun collisionControl() {
        val yellowCircleCenterX = yellowCircleX + binding.yellowCircle.width/2.0f
        val yellowCircleCenterY = yellowCircleY + binding.yellowCircle.height/2.0f
        if(0.0f <= yellowCircleCenterX  && yellowCircleCenterX <= chterWeigth
            && MainCharacterY <= yellowCircleCenterY && yellowCircleCenterY <= MainCharacterY + chterHigh){
            skor+=20
            yellowCircleX = -10.0f
        }

        val pinkTriangleCenterX = pinkTriangleX + binding.pinkTriangle.width/2.0f
        val pinkTriangleCenterY = pinkTriangeY+ binding.pinkTriangle.height/2.0f
        if (0.0f <= pinkTriangleCenterX && pinkTriangleCenterX <= chterWeigth
            && MainCharacterY <= pinkTriangleCenterY && pinkTriangleCenterY <= MainCharacterY + chterHigh) {
            skor += 50
            pinkTriangleX = -10.0f
        }

        val blackSquareCenterX = blackSquareX + binding.blackSquare.width/2.0f
        val blackSquareCenterY = blackSquareY + binding.blackSquare.height/2.0f
        if(0.0f <= blackSquareCenterX && blackSquareCenterX <= chterWeigth
            && MainCharacterY <= blackSquareCenterY && blackSquareCenterY <= MainCharacterY + chterHigh) {
            blackSquareX = -10.0f

            timer.cancel()

            val intent = Intent(this@ScreenActivity, ResultActivity::class.java)
            intent.putExtra("skor", skor)
            startActivity(intent)

            binding.textViewScore.text = skor.toString()

        }

    }

}