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
            CoroutineScope(IO).launch {
                fakeApiCall()
            }
//            setNewText("Jip too lay!")
        }

        binding.btnHitMe.setOnLongClickListener {
            binding.tvText.text = ""
            return@setOnLongClickListener true
        }
    }

//    private suspend fun fakeApiRequest(){
//
//    }

    private fun setNewText(input :String){
        val newText = binding.tvText.text.toString()+"\n$input"
        binding.tvText.text = newText
    }

    private suspend fun setTextOnMainThread(input: String){
        withContext(Main){
            setNewText(input)
        }
    }
    private suspend fun fakeApiCall(){
        val result1 = getResult1FromApi()
        println("debug $result1")
        setTextOnMainThread(result1)
        setTextOnMainThread(getResult2FromApi())
    }
    private suspend fun getResult1FromApi(): String{
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String{
        logThread("getResult2FromApi")
        delay(1000)
        return RESULT_2
    }

    private fun logThread(methodName:String){
        println("debug: $methodName ${Thread.currentThread().name}")
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}