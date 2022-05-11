package com.htetarkarzaw.coroutinesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.htetarkarzaw.coroutinesample.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
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
        CoroutineScope(IO).launch {
            val executionTime = measureTimeMillis {
                val result1 : Deferred<String> = async {
                    println("debug: launching job1 ${Thread.currentThread().name}")
                    getResult1FromApi()
                }
                val result2 : Deferred<String> = async {
                    println("debug: launching job2 ${Thread.currentThread().name}")
                    getResult2FromApi()
                }
                var result = ""
                val job = launch {
                    result = getResult1FromApi()
                }
                job.join()
                println("debug: job: $result")
                setTextOnMainThread("Got ${result1.await()}")
                setTextOnMainThread("Got ${result2.await()}")
            }
            println("debug: total time elapsed: $executionTime")
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