package com.htetarkarzaw.coroutinesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.htetarkarzaw.coroutinesample.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHitMe.setOnClickListener {
            binding.btnHitMe.text = "Clicked!"
            fakeApiRequest()
        }

        binding.btnHitMe.setOnLongClickListener {
            binding.tvText.text = ""
            return@setOnLongClickListener true
        }
    }

    private fun fakeApiRequest(){
        val startTime = System.currentTimeMillis()
        val parentJob = CoroutineScope(IO).launch {
            val job1 = launch {
                val time1 = measureTimeMillis {
                    println("debug: launching job1 in thread: ${Thread.currentThread().name}")
                    val result1 = getResult1FromApi()
                    setTextOnMainThread(result1)
                }
                println("debug: complete job1 in $time1 ms.")
            }

            val job2 = launch {
                val time2 = measureTimeMillis {
                    println("debug: launching job2 in thread: ${Thread.currentThread().name}")
                    val result2 = getResult2FromApi()
                    setTextOnMainThread(result2)
                }
                println("debug: complete job1 in $time2 ms.")
            }
        }
        parentJob.invokeOnCompletion {
            println("debug: total elapsed time: ${System.currentTimeMillis()-startTime}")
        }
    }

    private fun setNewText(input :String){
        val newText = binding.tvText.text.toString()+"\n$input"
        binding.tvText.text = newText
    }

    private suspend fun setTextOnMainThread(input: String){
        withContext(Main){
            setNewText(input)
        }
    }
    private suspend fun getResult1FromApi(): String{
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String{
        delay(1700)
        return RESULT_2
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}