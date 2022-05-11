package com.htetarkarzaw.coroutinesample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.htetarkarzaw.coroutinesample.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHitMe.setOnClickListener {
            setNewText("Clicked!")
            fakeApiCall()
        }

        binding.btnHitMe.setOnLongClickListener {
            binding.tvText.text = ""
            return@setOnLongClickListener true
        }
    }

    private fun setNewText(input: String) {
        val newText = binding.tvText.text.toString() + "\n$input"
        binding.tvText.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Main) {
            setNewText(input)
        }
    }

    private fun fakeApiCall() {
        CoroutineScope(IO).launch {
            val executionTime = measureTimeMillis {
                val result1 = async {
                    println("debug: launching job1 ${Thread.currentThread().name}")
                    getResult1FromApi()
                }.await()

                val result2 = async {
                    println("debug: launching job1 ${Thread.currentThread().name}")
                    getResult2FromApi("asdfasdfasdg")
                }.await()
                println("debug: got result2: $result2")
            }
            println("debug: total elapsed time: $executionTime ms.")
        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(result1: String): String {
        delay(1000)
        if (result1 == RESULT_1) {
            return RESULT_2
        }
        return "Result #1 was incorrect..."
    }

    private fun logThread(methodName: String) {
        println("debug: $methodName ${Thread.currentThread().name}")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}