package com.everis.listadecontatos.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
            "nome TEXT NOT NULL" +
            "telefone TEXT NOT NULL" +
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
}