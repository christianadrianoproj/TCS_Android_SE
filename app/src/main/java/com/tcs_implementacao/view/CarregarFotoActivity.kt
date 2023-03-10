package com.tcs_implementacao.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.loader.content.CursorLoader
import com.tcs_implementacao.R
import com.tcs_implementacao.config.RetrofitInitializer
import com.tcs_implementacao.model.Foto
import com.tcs_implementacao.model.FotoRetorno
import kotlinx.android.synthetic.main.activity_carregar_foto.*
import kotlinx.android.synthetic.main.activity_carregar_foto.imageView
import kotlinx.android.synthetic.main.activity_tirar_foto.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CarregarFotoActivity : AppCompatActivity() {

    private val LOAD_IMAGE_RESULTS = 1
    private var objRetornoImage: FotoRetorno = FotoRetorno(null, null)
    private lateinit var botaoConfirmar: Button
    private val TAG_SISTEMA = "CHRISTIAN";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carregar_foto)
        imageView.visibility = View.INVISIBLE

        val btn = findViewById<Button>(R.id.buttonPick).setOnClickListener {
            pegaImagemGaleria()
        }

        botaoConfirmar = findViewById<Button>(R.id.buttonConfirm)
        botaoConfirmar.setOnClickListener {
            confirmar()
        }
        botaoConfirmar.visibility = View.INVISIBLE
    }

    private fun pegaImagemGaleria() {
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(i, LOAD_IMAGE_RESULTS)
    }

    private fun enviaImagem(data: Uri) {
        val file = File(this?.let { getRealPathFromURIAPI11to18(it, data) })
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val fileToUpload: MultipartBody.Part = MultipartBody.Part.createFormData("image_original", file.name, requestFile)
        val call = RetrofitInitializer().ServicePython().enviaImagem(fileToUpload, requestFile)
        call.enqueue(object : Callback<FotoRetorno> {
            override fun onFailure(call: Call<FotoRetorno>, t: Throwable) {
                Log.d(TAG_SISTEMA, "Falhou enviaImagem: $t.message")
                Toast.makeText(
                    this@CarregarFotoActivity,
                    "Ocorreu um erro tente novamente: $t.message",
                    Toast.LENGTH_LONG
                ).show()
            }
            override fun onResponse(call: Call<FotoRetorno>, response: Response<FotoRetorno>) {
                Log.d(TAG_SISTEMA, response.toString())
                if (response.isSuccessful) {
                    objRetornoImage = response.body()!!
                    botaoConfirmar.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun getFoto(_id: Int) {
        var call = RetrofitInitializer().Service().getFoto(_id)
        call.enqueue(object : Callback<Foto> {
            override fun onFailure(call: Call<Foto>, t: Throwable) {
                Log.d("CHRISTIAN", "Falhou getFoto: $t.message")
            }

            override fun onResponse(call: Call<Foto>, response: Response<Foto>) {
                Log.d(
                    "CHRISTIAN",
                    "********** Retornou getFoto *********:  $response.isSuccessful"
                )
                Log.d("CHRISTIAN", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    val foto = response.body()!!
                    objRetornoImage = FotoRetorno(foto.idFoto, "success")
                    botaoConfirmar.visibility = View.VISIBLE
                    Log.d("CHRISTIAN", "Retornou: $objRetornoImage")
                }
            }
        })
    }

    fun getRealPathFromURIAPI11to18(context: Context, contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var result: String? = null

        val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        objRetornoImage = FotoRetorno(null, null)
        if (resultCode == Activity.RESULT_OK) {
            val URL = data?.data
            val bitImage = MediaStore.Images.Media.getBitmap(this?.contentResolver, URL)
            imageView.setImageBitmap(bitImage)
            if (URL != null) {
                imageView.visibility = View.VISIBLE
                enviaImagem(URL)
                //getFoto(1)
            }
        }

    }

    private fun validate(): Boolean {
        var result = true
        if ((objRetornoImage.id_image != null) && (objRetornoImage.id_image!! > 0)) {
        }
        else {
            AlertDialog.Builder(this)
                .setTitle("Valida????o")
                .setMessage("Selecione uma foto!")
                .setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }.show()
            result = false
        }
        return result
    }

    fun confirmar() {
        if (validate()) {
            val i = Intent(this, ExecucaoActivity::class.java)
            i.putExtra("idImage", objRetornoImage.id_image.toString())
            startActivity(i)
            /*val intent = this.intent
            intent.putExtra("idFoto", objRetornoImage.id_image.toString())
            this.setResult(Activity.RESULT_OK, intent)
            this.finish()*/
        }
    }
}
