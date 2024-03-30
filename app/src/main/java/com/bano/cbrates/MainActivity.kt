package com.bano.cbrates

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bano.cbrates.retrofit.DailyApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    lateinit var dailyApi: DailyApi
    var currencies = listOf<Currency>()
    lateinit var currencyAdapter:CurrencyAdapter
    lateinit var currencyRecyclerView: RecyclerView
    lateinit var test: TextView

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test = findViewById(R.id.test)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.cbr-xml-daily.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        dailyApi = retrofit.create(DailyApi::class.java)

        currencyRecyclerView = findViewById(R.id.currencyRecyclerView)
        currencyRecyclerView.layoutManager = LinearLayoutManager(this)


        GlobalScope.launch {
            while (true) {
                try {
                    updateCurr()
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        test.text = "Ошибка при обновлении"
                    }
                }
                delay(30000)
            }
        }
    }

    private suspend fun updateCurr(){
        CoroutineScope(Dispatchers.IO).launch {
            val daily = dailyApi.getDaily()
            currencies = listOf(
                Currency("AED", daily.Valute.AED.Value),
                Currency("AMD", daily.Valute.AMD.Value),
                Currency("AUD", daily.Valute.AUD.Value),
                Currency("AZN", daily.Valute.AZN.Value),
                Currency("BGN", daily.Valute.BGN.Value),
                Currency("BRL", daily.Valute.BRL.Value),
                Currency("BYN", daily.Valute.BYN.Value),
                Currency("CAD", daily.Valute.CAD.Value),
                Currency("CHF", daily.Valute.CHF.Value),
                Currency("CNY", daily.Valute.CNY.Value),
                Currency("CZK", daily.Valute.CZK.Value),
                Currency("DKK", daily.Valute.DKK.Value),
                Currency("EGP", daily.Valute.EGP.Value),
                Currency("EUR", daily.Valute.EUR.Value),
                Currency("GBP", daily.Valute.GBP.Value),
                Currency("GEL", daily.Valute.GEL.Value),
                Currency("HKD", daily.Valute.HKD.Value),
                Currency("HUF", daily.Valute.HUF.Value),
                Currency("IDR", daily.Valute.IDR.Value),
                Currency("INR", daily.Valute.INR.Value),
                Currency("JPY", daily.Valute.JPY.Value),
                Currency("KGS", daily.Valute.KGS.Value),
                Currency("KRW", daily.Valute.KRW.Value),
                Currency("KZT", daily.Valute.KZT.Value),
                Currency("MDL", daily.Valute.MDL.Value),
                Currency("NOK", daily.Valute.NOK.Value),
                Currency("NZD", daily.Valute.NZD.Value),
                Currency("PLN", daily.Valute.PLN.Value),
                Currency("QAR", daily.Valute.QAR.Value),
                Currency("RON", daily.Valute.RON.Value),
                Currency("RSD", daily.Valute.RSD.Value),
                Currency("SEK", daily.Valute.SEK.Value),
                Currency("SGD", daily.Valute.SGD.Value),
                Currency("THB", daily.Valute.THB.Value),
                Currency("TJS", daily.Valute.TJS.Value),
                Currency("TMT", daily.Valute.TMT.Value),
                Currency("TRY", daily.Valute.TRY.Value),
                Currency("UAH", daily.Valute.UAH.Value),
                Currency("USD", daily.Valute.USD.Value),
                Currency("UZS", daily.Valute.UZS.Value),
                Currency("VND", daily.Valute.VND.Value),
                Currency("XDR", daily.Valute.XDR.Value),
                Currency("ZAR", daily.Valute.ZAR.Value),
            )

            runOnUiThread{
                currencyAdapter = CurrencyAdapter(currencies)
                currencyRecyclerView.adapter = currencyAdapter
                val currentDateTime = LocalDateTime.now()
                test.text = currentDateTime.format(DateTimeFormatter.ISO_TIME)
            }
        }
    }
}