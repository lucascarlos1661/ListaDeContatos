package com.everis.listadecontatos.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.everis.listadecontatos.feature.listacontatos.model.ContatosVO

class HelperDB(
    context: Context
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL) {

    companion object {
        private val NOME_BANCO = "contato.db"
        private val VERSAO_ATUAL = 1
    }

    val TABLE_NAME = "contato"
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "id INTEGER NOT NULL," +
            "nome TEXT NOT NULL," +
            "telefone TEXT NOT NULL," +
            "" +
            "PRIMARY KEY(id AUTOINCREMENT)" +
            ")"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db?.execSQL(DROP_TABLE)
        }
        onCreate(db)
    }

    fun buscarContatos(busca: String, isBuscaPorID: Boolean = false): List<ContatosVO> {
        val db = readableDatabase ?: return mutableListOf()
        var lista = mutableListOf<ContatosVO>()
        var where: String? = null
        var args: Array<String> = arrayOf()

        if (isBuscaPorID) {
            where = "id = ?"
            args = arrayOf("$busca")
        } else {
            where = "nome LIKE ?"
            args = arrayOf("%$busca%")
        }

        var cursor = db.query(TABLE_NAME, null, where, args, null, null, null)
        if (cursor == null) {
            db.close()
            return mutableListOf()
        }
        while (cursor.moveToNext()) {
            var contato = ContatosVO(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("nome")),
                cursor.getString(cursor.getColumnIndex("telefone"))
            )
            lista.add(contato)
        }
        db.close()
        return lista
    }

    fun salvarContato(contato: ContatosVO) {
        val db = writableDatabase ?: return
        var content = ContentValues()
        content.put("nome", contato.nome)
        content.put("telefone", contato.telefone)
        db.insert(TABLE_NAME, null, content)
        db.close()
    }

    fun deletarContato(id: Int) {
        val db = writableDatabase ?: return
        var where = "id = ?"
        var args = arrayOf("$id")
        db.delete(TABLE_NAME, where, args)
        db.close()
    }

    fun updateContato(contato: ContatosVO) {
        val db = writableDatabase ?: return
        val sql = "UPDATE $TABLE_NAME SET nome = ?, telefone = ? WHERE id = ?"
        val args = arrayOf(contato.nome, contato.telefone, contato.id)
        db.execSQL(sql, args)
    }
}