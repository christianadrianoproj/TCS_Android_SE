package com.tcs_implementacao.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tcs_implementacao.R
import com.tcs_implementacao.adapter.AdapterRegra
import com.tcs_implementacao.config.RetrofitInitializer
import com.tcs_implementacao.model.Regra
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaRegrasActivity : AppCompatActivity() {

    private lateinit var adapter: AdapterRegra
    private lateinit var recyclerViewRegras: RecyclerView
    private var listaRegras: ArrayList<Regra> = mutableListOf<Regra>() as ArrayList<Regra>

    val RETORNO_REGRA = 210

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_regras)

        initcomponents()
    }

    override fun onStart() {
        super.onStart()
        carregaDadosApiRegra()
        atualizaRecyclerViewRegras()
    }

    private fun atualizaRecyclerViewRegras() {
        var call = RetrofitInitializer().variavelService().findAllRegra()
        call.enqueue(object : Callback<List<Regra>> {
            override fun onFailure(call: Call<List<Regra>>, t: Throwable) {  }
            override fun onResponse(
                call: Call<List<Regra>>,
                response: Response<List<Regra>>
            ) {

                if (response.isSuccessful) {
                    var regra: List<Regra> = response.body()!!
                    configuraAdapter(regra)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun carregaDadosApiRegra() {
        var call: Call<List<Regra>> = RetrofitInitializer().Service().findAllRegra()
        call.enqueue(object : Callback<List<Regra>> {
            override fun onResponse(
                call: Call<List<Regra>>,
                response: Response<List<Regra>>
            ) {
                if (response.isSuccessful) {
                    listaRegras = response.body()!! as ArrayList<Regra>
                }
            }

            override fun onFailure(call: Call<List<Regra>>, t: Throwable) {
                Log.d("Resultado", "Falhou")
            }
        })
    }

    private fun configuraRecyclerViewRegras(adapter: AdapterRegra) {
        val layout = LinearLayoutManager(this)
        recyclerViewRegras.adapter = adapter
        recyclerViewRegras.layoutManager = layout
        recyclerViewRegras.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        recyclerViewRegras.setHasFixedSize(true)

        adapter.onItemClick = { regra ->
            startActivityForResult(
                Intent(this, RegraActivity::class.java)
                    .putExtra("regra", regra)
                    .putExtra("novo", false) , RETORNO_REGRA
            )
        }
    }

    fun eventoClickFloatActionButtonAddRegra(view: View) {
        startActivityForResult(
            Intent(this, RegraActivity::class.java)
                .putExtra("regra", Regra())
                .putExtra("novo", true), RETORNO_REGRA)
    }

    private fun configuraAdapter(list: List<Regra>) {
        adapter = AdapterRegra(list as MutableList<Regra>)
        configuraRecyclerViewRegras(adapter)
    }

    private fun initcomponents() {
        recyclerViewRegras = findViewById<RecyclerView>(R.id.recyclerViewRegras)
        atualizaRecyclerViewRegras()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == RETORNO_REGRA) && resultCode == RESULT_OK) {
            atualizaRecyclerViewRegras()
        }
    }
}