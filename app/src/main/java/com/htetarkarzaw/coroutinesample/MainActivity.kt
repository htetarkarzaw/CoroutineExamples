package com.htetarkarzaw.coroutinesample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.htetarkarzaw.coroutinesample.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val TAG = "AppDebug"
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        timeDif("12:30pm-12:00am")
        solution("abbaacc")
        main()
        binding.btnHitMe.setOnClickListener {
//            binding.tvText.text = (count++).toString()
//            solution("abbaacc")
            val doctorList = ArrayList<Doctor>()
            val doc1 = Doctor("Smith", 4)
            val doc2 = Doctor("Waee", 3)
            val doc3 = Doctor("Bellie", 2)
            doctorList.add(doc1)
            doctorList.add(doc2)
            doctorList.add(doc3)
            calculateTime(doctorList, 12)
        }

        binding.btnHitMe.setOnLongClickListener {
            binding.tvText.text = ""
            return@setOnLongClickListener true
        }

    }

    private fun calculateTime(doctorList: java.util.ArrayList<Doctor>, position: Int): Int {
        var times = 0
        var total = 0
        var answer = 0
        var count: Int = position
        for (i in doctorList) {
            total += i.waitingTime
        }
        times = total / doctorList.size * position / doctorList.size
        for (i in 0 until times) {
            for (d in doctorList) {
                if (i % d.waitingTime == 0) {
                    count -= 1
                    if (count < 1) {
                        break
                    }
                    answer = i
                    println("doctor time ->" + i + "---" + d.docName + "--" + answer)

                }
                if (count < 1) {
                    break
                }
            }
        }

        println("doctor time for calculate ->$times")
        println("doctor time ->$answer")
        return 0
    }


    private fun solution(s: String) {
        var list = s.trim().toList()
        var charList = ArrayList<Char>()
        var countList = ArrayList<Int>()
        for (i in list) {
            var count = 0
            var isAlreadyIn = false
            for (char in charList) {
                if (char == i) {
                    isAlreadyIn = true
                }
            }
            if (!isAlreadyIn) {
                charList.add(i)
                for (j in list) {
                    if (i == j) {
                        count += 1
                    }
                }
                countList.add(count)
            }
        }
        var finalString = ""
        for (i in 0 until charList.size) {
            finalString += countList[i].toString() + charList[i].toString()
        }
        println("Test----$finalString")
    }

    private fun main() {
        CoroutineScope(Main).launch {
            printLn("Current Thread: ${Thread.currentThread().name}")
//            for(i in 1..100_000){
//                launch {
//                    doNetworkRequest()
//                }
//            }
            doNetworkRequest()
        }
    }

    private suspend fun doNetworkRequest() {
        println("Starting network request.....")
        delay(5000)
        println("finished network request!")
    }

    private fun printLn(text: String) {
        Log.e(TAG, text)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun timeDif(time: String): String {
        var time1 = time.split("-")[0]
        var time2 = time.split("-")[1]
        var hour1 = time1.split(":")[0].toInt()
        var hour2 = time1.split(":")[0].toInt()
        var min1_zone = time1.split(":")[1]
        var min2_zone = time2.split(":")[1]
        var min1 = min1_zone.subSequence(0, 2).toString().toInt()
        var min2 = min2_zone.subSequence(0, 2).toString().toInt()
        var zone1 = min1_zone.subSequence(2, 4)
        var zone2 = min2_zone.subSequence(2, 4)
        var formatHour1 = 0
        var formatHour2 = 0
        formatHour1 = if (zone1 != "am") {
            if (hour1 == 12) {
                hour1
            } else {
                hour1 + 12
            }
        } else {
            if (hour1 == 12) {
                0
            } else {
                hour1
            }
        }
        formatHour2 = if (zone2 != "am") {
            if (hour1 == 12) {
                hour1
            } else {
                hour1 + 12
            }
        } else {
            if (hour2 == 12) {
                0
            } else {
                hour2
            }
        }
        var hourDif = formatHour1 - formatHour2
        var minDif = 0
        if (hourDif < 0) {
            hourDif *= (-1)
            if (min2 < min1) {
                min2 + 60
                hourDif -= 1
            }
            minDif = min2 - min1
        } else {
            if (min1 < min2) {
                min1 + 60
                hourDif -= 1
            }
            minDif = min1 - min2
        }
        var dif = (hourDif * 60) + minDif
        printLn("Time-------$formatHour1&$formatHour2---$dif")
        return "$dif"
    }

    fun getMillisecondFromTime(time: String?): Long {
        return try {
            val myDate = time
            val sdf = SimpleDateFormat("h:mma")
            val date = sdf.parse(myDate)
            date.time
        } catch (e: Exception) {
            0
        }
    }
}