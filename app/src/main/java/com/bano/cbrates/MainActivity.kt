package com.bano.cbrates

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bano.cbrates.retrofit.DailyApi
import kotlinx.coroutines.CoroutineExceptionHandler
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
    lateinit var topTextView: TextView
    lateinit var coroutineExceptionHandler: CoroutineExceptionHandler
    lateinit var progressBar: ProgressBar

    @OptIn(DelicateCoroutinesApi::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        topTextView = findViewById(R.id.topTextView)
        progressBar = findViewById(R.id.progress_bar)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.cbr-xml-daily.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        dailyApi = retrofit.create(DailyApi::class.java)

        currencyRecyclerView = findViewById(R.id.currencyRecyclerView)
        currencyRecyclerView.layoutManager = LinearLayoutManager(this)

        coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            throwable.printStackTrace()
            runOnUiThread {
                topTextView.text = "Ошибка при обновлении"
                progressBar.visibility = View.VISIBLE
            }
        }

        GlobalScope.launch {
            while (true) {
                try {
                    updateCurr()
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        topTextView.text = "Ошибка при обновлении"
                        progressBar.visibility = View.VISIBLE
                    }
                }
                delay(30000)
            }
        }
    }

    private suspend fun updateCurr(){
        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO + coroutineExceptionHandler) {
            val daily = dailyApi.getDaily()
            currencies = listOf(
                Currency(daily.Valute.AED.Name, daily.Valute.AED.Value),
                Currency(daily.Valute.AMD.Name, daily.Valute.AMD.Value),
                Currency(daily.Valute.AUD.Name, daily.Valute.AUD.Value),
                Currency(daily.Valute.AZN.Name, daily.Valute.AZN.Value),
                Currency(daily.Valute.BGN.Name, daily.Valute.BGN.Value),
                Currency(daily.Valute.BRL.Name, daily.Valute.BRL.Value),
                Currency(daily.Valute.BYN.Name, daily.Valute.BYN.Value),
                Currency(daily.Valute.CAD.Name, daily.Valute.CAD.Value),
                Currency(daily.Valute.CHF.Name, daily.Valute.CHF.Value),
                Currency(daily.Valute.CNY.Name, daily.Valute.CNY.Value),
                Currency(daily.Valute.CZK.Name, daily.Valute.CZK.Value),
                Currency(daily.Valute.DKK.Name, daily.Valute.DKK.Value),
                Currency(daily.Valute.EGP.Name, daily.Valute.EGP.Value),
                Currency(daily.Valute.EUR.Name, daily.Valute.EUR.Value),
                Currency(daily.Valute.GBP.Name, daily.Valute.GBP.Value),
                Currency(daily.Valute.GEL.Name, daily.Valute.GEL.Value),
                Currency(daily.Valute.HKD.Name, daily.Valute.HKD.Value),
                Currency(daily.Valute.HUF.Name, daily.Valute.HUF.Value),
                Currency(daily.Valute.IDR.Name, daily.Valute.IDR.Value),
                Currency(daily.Valute.INR.Name, daily.Valute.INR.Value),
                Currency(daily.Valute.JPY.Name, daily.Valute.JPY.Value),
                Currency(daily.Valute.KGS.Name, daily.Valute.KGS.Value),
                Currency(daily.Valute.KRW.Name, daily.Valute.KRW.Value),
                Currency(daily.Valute.KZT.Name, daily.Valute.KZT.Value),
                Currency(daily.Valute.MDL.Name, daily.Valute.MDL.Value),
                Currency(daily.Valute.NOK.Name, daily.Valute.NOK.Value),
                Currency(daily.Valute.NZD.Name, daily.Valute.NZD.Value),
                Currency(daily.Valute.PLN.Name, daily.Valute.PLN.Value),
                Currency(daily.Valute.QAR.Name, daily.Valute.QAR.Value),
                Currency(daily.Valute.RON.Name, daily.Valute.RON.Value),
                Currency(daily.Valute.RSD.Name, daily.Valute.RSD.Value),
                Currency(daily.Valute.SEK.Name, daily.Valute.SEK.Value),
                Currency(daily.Valute.SGD.Name, daily.Valute.SGD.Value),
                Currency(daily.Valute.THB.Name, daily.Valute.THB.Value),
                Currency(daily.Valute.TJS.Name, daily.Valute.TJS.Value),
                Currency(daily.Valute.TMT.Name, daily.Valute.TMT.Value),
                Currency(daily.Valute.TRY.Name, daily.Valute.TRY.Value),
                Currency(daily.Valute.UAH.Name, daily.Valute.UAH.Value),
                Currency(daily.Valute.USD.Name, daily.Valute.USD.Value),
                Currency(daily.Valute.UZS.Name, daily.Valute.UZS.Value),
                Currency(daily.Valute.VND.Name, daily.Valute.VND.Value),
                Currency(daily.Valute.XDR.Name, daily.Valute.XDR.Value),
                Currency(daily.Valute.ZAR.Name, daily.Valute.ZAR.Value),
            )

            runOnUiThread{
                currencyAdapter = CurrencyAdapter(currencies)
                currencyRecyclerView.adapter = currencyAdapter
                val currentDateTime = LocalDateTime.now()
                topTextView.text = "Дата и время последней корректной загрузки данных: " +  currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                progressBar.visibility = View.GONE
            }
        }
    }
}