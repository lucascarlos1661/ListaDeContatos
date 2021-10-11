package com.everis.listadecontatos.feature.contato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.everis.listadecontatos.R
import com.everis.listadecontatos.application.ContatoApplication
import com.everis.listadecontatos.bases.BaseActivity
import com.everis.listadecontatos.feature.listacontatos.model.ContatosVO
import com.everis.listadecontatos.singleton.ContatoSingleton
import kotlinx.android.synthetic.main.activity_contato.*
import kotlinx.android.synthetic.main.activity_contato.toolBar

class ContatoActivity : BaseActivity() {

    private var idContato: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato)
        setupToolBar(toolBar, "Contato", true)
        setupContato()
        btnSalvarConato.setOnClickListener { onClickSalvarContato() }

        btnExcluirContato.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Deseja excluir o contato?")
                .setCancelable(false)
                .setPositiveButton("Sim") { dialog, id ->
                    // Delete selected note from database
                    onClickExcluirContato()
                }
                .setNegativeButton("Não") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }

    private fun setupContato() {
        idContato = intent.getIntExtra("index", -1)
        if (idContato == -1) {
            btnExcluirContato.visibility = View.GONE
            return
        }
        Thread(Runnable {
            var lista =
                ContatoApplication.instance.helperDB?.buscarContatos("$idContato", true)
                    ?: return@Runnable
            var contato = lista.getOrNull(0) ?: return@Runnable
            runOnUiThread {
                etNome.setText(contato.nome)
                etTelefone.setText(contato.telefone)
            }
        }).start()

    }

    private fun onClickSalvarContato() {
        val nome = etNome.text.toString()
        val telefone = etTelefone.text.toString()
        val contato = ContatosVO(
            idContato,
            nome,
            telefone
        )
        Thread(Runnable {
            if (idContato == -1) {
                ContatoApplication.instance.helperDB?.salvarContato(contato)

            } else {
                ContatoApplication.instance.helperDB?.updateContato(contato)
            }
            runOnUiThread {
                finish()
            }
        }).start()


    }

    fun onClickExcluirContato() {
        Thread(Runnable {
            if (idContato > -1) {
                ContatoApplication.instance.helperDB?.deletarContato(idContato)

            }
            runOnUiThread {
                Toast.makeText(this, "Contato excluido", Toast.LENGTH_SHORT).show()
                finish()
            }
        }).start()

    }
}
