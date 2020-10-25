package com.example.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animatedValue
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import com.example.timer.ui.TimerTheme
import java.sql.Time

const val twoHoursInMs: Long = 7200000

class TimerViewModel : ViewModel(){
    var value by mutableStateOf<Long>(twoHoursInMs)
    var cdt: CountDownTimer? = null

    fun countdown(){
        if(cdt != null) return

        cdt = object : CountDownTimer(value, 1000){
            override fun onTick(milisRemaining: Long) {  value = milisRemaining }
            override fun onFinish() {}
        }.start()
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TimerTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent(){
    val timerViewModel: TimerViewModel = viewModel()
    val aniValue = animatedValue(initVal = 0, converter = Int.VectorConverter)

    onActive {
        timerViewModel.countdown()
    }

    Scaffold() {
        Column(Modifier.padding(10.dp)){
            SomeContent(timerViewModel, aniValue)
        }
    }
}

@Composable
fun SomeContent(timerViewModel: TimerViewModel, aniValue: AnimatedValue<Int, AnimationVector1D>){
    onCommit(timerViewModel.value){
        aniValue.snapTo(0)
        aniValue.animateTo(targetValue = 360, anim = TweenSpec(durationMillis = 900, easing = LinearEasing))
    }

    SomeContent(aniValue.value, timerViewModel.value)
}

@Composable
fun SomeContent(animatedValue: Int, viewModelValue: Long){
    Text("$animatedValue")
    Text("$viewModelValue")
    Text("Canvas with drawArc")
    Canvas(modifier = Modifier.padding(10.dp).preferredSize(200.dp)){
        drawArc(
            Color.Blue,
            270.0f, // 0 -> 3'o clock
            animatedValue.toFloat(),
            useCenter = false,
            topLeft = Offset(0.0f, 0.0f), //ozek, širok
            style = Stroke(10.0f)
        )
    }
    Text("Build in circular progress indicator")
    CircularProgressIndicator(
        progress = animatedValue / 360.0f,
        modifier = Modifier.preferredSize(100.dp)
    )
}



