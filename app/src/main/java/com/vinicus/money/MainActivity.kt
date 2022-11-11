package com.vinicus.money

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var result: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.textView)

        val button_converter = findViewById<Button>(R.id.btn_conversor)
        button_converter.setOnClickListener {
            converter()
            /*  result.text = value.toString()
                result.visibility= View.VISIBLE*/
        }
    }

    private fun converter() {
        val selected = findViewById<RadioGroup>(R.id.radioGroup)
        val checked = selected.checkedRadioButtonId
        val originValue = "BRL"
        val currency = when {
            checked == R.id.radio_usd -> "USD" //Doletas
            checked == R.id.radio_euro -> "EUR" //Eurecas
            else -> "CLP" //Pesos Chilenetos
        }

        val edit_field = findViewById<EditText>(R.id.editField)
        val value = edit_field.text.toString()

        if (value.isEmpty())
            return



        Thread {
            // PROCESSO PARALELO DO OPOSTO DA HIPOTENUSA DO CATETO

            val url =
                URL("https://free.currconv.com/api/v7/convert?q=${currency}_${originValue}&compact=ultra&apiKey=temChaveNaoRapa")
            val con = url.openConnection() as HttpsURLConnection
            try {
                val data = con.inputStream.bufferedReader().readText()
                // data recebe o par de chave valor do json
                val obj = JSONObject(data)

                runOnUiThread {
                    val res = obj.getDouble("${currency}_${originValue}")
                    result.text = "R$${"%.4f".format(res * value.toDouble())}"
                    result.visibility = TextView.VISIBLE
                }
            } finally {
                con.disconnect()
            }
        }.start()
    }
}