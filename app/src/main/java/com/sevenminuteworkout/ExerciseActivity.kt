package com.sevenminuteworkout

import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_exercise.*
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener  {

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_exercise_activity.setNavigationOnClickListener {
            onBackPressed()
        }
        tts =TextToSpeech(this,this)

        exerciseList = Constants.defaultExerciseList()
        setupRestView()
    }

    public override fun onDestroy() {
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }




    private fun setupRestView() {
        llExerciseView.visibility = View.GONE
        llRestView.visibility = View.VISIBLE

        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }
        tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition+1].getName()
        setRestProgressBar()
    }

    private fun setRestProgressBar() {

        progressBar.progress = restProgress

        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 10 - restProgress
                tvTimer.text =
                    (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                setupExerciseView()
            }
        }.start()
    }



    private fun setupExerciseView() {
        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE
        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()
        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
    }

    private fun setExerciseProgressBar() {

        progressBarExercise.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressBarExercise.progress = 30 - exerciseProgress
                tvExerciseTimer.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList!!.size!! -1){
                    setupRestView()
                }else{
                    Toast.makeText(this@ExerciseActivity, "Congratulations! you've completed the seven minute workout challenge.", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }


    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.ENGLISH)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The Language specifies in not supported!")
            }
        }else Log.e("TTS","Initialization Failed!")
    }

    private fun speakOut(text: String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,   null,"")
    }
}