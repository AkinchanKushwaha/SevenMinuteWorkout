package com.sevenminuteworkout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bmi.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"

    var currentVisibleView : String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)
        setSupportActionBar(toolbar_bmi_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "CALCULATE BMI"
        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        btnCalculateUnits.setOnClickListener {
            if(currentVisibleView == METRIC_UNITS_VIEW){
                if(validateMetricUnits()){
                    val heightValue: Float = etMetricUnitHeight.text.toString().toFloat()/100
                    val weightValue: Float = etMetricUnitWeight.text.toString().toFloat()

                    val bmi = weightValue/(heightValue*heightValue)

                    displayBMIResult(bmi)
                }else{
                    Toast.makeText(this@BMIActivity, "Please Enter the valid values!", Toast.LENGTH_SHORT).show()
                }
            }else{
                if(validateUSUnits()){
                    val heightValueFeet: String = etUSUnitHeightFeet.text.toString()
                    val heightValueInch: String = etUSUnitHeightInch.text.toString()
                    val weightValue: Float = etUSUnitWeight.text.toString().toFloat()

                    val heightValue = heightValueInch.toFloat() + heightValueFeet.toFloat()*12

                    val bmi = 703*(weightValue/ (heightValue*heightValue))
                    displayBMIResult(bmi)
                }else{
                    Toast.makeText(this@BMIActivity, "Please Enter the valid values!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        makeVisibleMetricUnitsView()
        rgUnits.setOnCheckedChangeListener { radioGroup, checkedId ->
            if(checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else
                makeVisibleUSUnitsView()
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        tilMetricUnitWeight.visibility = View.VISIBLE
        tilMetricUnitHeight.visibility = View.VISIBLE

        etMetricUnitHeight.text!!.clear()
        etMetricUnitWeight.text!!.clear()

        tilUSUnitWeight.visibility = View.GONE
        llUsUnitHeight.visibility = View.GONE

        llDisplayBMIResult.visibility = View.GONE

    }
    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        tilMetricUnitWeight.visibility = View.GONE
        tilMetricUnitHeight.visibility = View.GONE

        etUSUnitHeightFeet.text!!.clear()
        etUSUnitHeightInch.text!!.clear()
        etUSUnitWeight.text!!.clear()

        tilUSUnitWeight.visibility = View.VISIBLE
        llUsUnitHeight.visibility = View.VISIBLE

        llDisplayBMIResult.visibility = View.GONE


    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel : String
        val bmiDescription: String
        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        llDisplayBMIResult.visibility = View.VISIBLE

        val bmivalue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tvBMIValue.text = bmivalue
        tvBMIType.text = bmiLabel
        tvBMIDescription.text = bmiDescription

    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true
        if(etMetricUnitWeight.text.toString().isEmpty() || etMetricUnitHeight.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun validateUSUnits(): Boolean{
        var isValid = true
        if(etUSUnitWeight.text.toString().isEmpty() || etUSUnitHeightFeet.text.toString().isEmpty() || etUSUnitHeightInch.text.toString().isEmpty() || etUSUnitHeightInch.text.toString().toFloat() > 11){
            isValid = false
        }
        return isValid
    }

}
