package com.htetarkarzaw.coroutinesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import com.htetarkarzaw.coroutinesample.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val PROGRESS_MAX = 100
    private val PROGRESS_START = 0
    private val JOB_TIME = 4000
    private lateinit var job : CompletableJob
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHitMe.setOnClickListener {
            if(!::job.isInitialized){
                initJob()
            }
            binding.pbJob.startJobOrCancel(job)
        }

        binding.btnHitMe.setOnLongClickListener {
            binding.tvText.text = ""
            return@setOnLongClickListener true
        }
    }

    fun ProgressBar.startJobOrCancel(job: Job){
        if(this.progress>0){
            println("$job is already active.cancelling.....")
            resetJob()
        }else{
            binding.btnHitMe.text = "Cancel job 1"
            CoroutineScope(IO + job).launch {
                println("coroutine $this is activated with job: $job")
                for (i in PROGRESS_START..PROGRESS_MAX){
                    delay((JOB_TIME/PROGRESS_MAX).toLong())
                    this@startJobOrCancel.progress = i
                }
                updateJobCompleteTextView("Job is complete.")
            }
        }
    }

    private fun  updateJobCompleteTextView(text : String){
        GlobalScope.launch(Main) {
            binding.tvText.text = text
        }
    }

    private fun resetJob() {
        if(job.isActive || job.isCompleted){
            job.cancel(CancellationException("Resetting job."))
        }
        initJob()
    }

    fun initJob(){
        binding.btnHitMe.text = "Start Job 1"
        updateJobCompleteTextView("")
        job = Job()
        job.invokeOnCompletion {
            it?.message.let {
                var message = it
                if(message.isNullOrBlank()){
                    message = "Unknown cancellation error."
                }
                println("$job was cancelled. Reason: $message")
                showToast(message)
            }
            binding.pbJob.max = PROGRESS_MAX
            binding.pbJob.progress = PROGRESS_START
        }
    }

    fun showToast(text : String){
        GlobalScope.launch(Main) {
            Toast.makeText(this@MainActivity,text,Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}