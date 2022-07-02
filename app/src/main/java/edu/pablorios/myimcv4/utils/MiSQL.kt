package edu.pablorios.myimcv4.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MiSQL(context: Context): SQLiteOpenHelper(context, "registros.db",null,1) {

    companion object{
        val NOMBRE_TABLA = "registros"
        val CAMPO_ID = "_id"
        val CAMPO_FECHA = "fecha"
        val CAMPO_SEXO = "sexo"
        val CAMPO_IMC = "imc"
        val CAMPO_PESO = "peso"
        val CAMPO_ALTURA = "altura"
        val CAMPO_ESTADO = "estado"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val create = "CREATE TABLE $NOMBRE_TABLA "+
                "($CAMPO_ID INTEGER PRIMARY KEY AUTOINCREMENT, $CAMPO_FECHA TEXT, $CAMPO_SEXO TEXT, " +
                "$CAMPO_IMC TEXT, $CAMPO_PESO INTEGER, $CAMPO_ALTURA INTEGER, $CAMPO_ESTADO TEXT)"

        p0!!.execSQL(create)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA")
        onCreate(p0)
    }

    fun addRegistro(fecha: String, sexo: String, imc: String, peso: Int, altura: Int, estado: String){
        val datos = ContentValues()
        datos.put(CAMPO_FECHA,fecha)
        datos.put(CAMPO_SEXO,sexo)
        datos.put(CAMPO_IMC,imc)
        datos.put(CAMPO_PESO,peso)
        datos.put(CAMPO_ALTURA,altura)
        datos.put(CAMPO_ESTADO,estado)
        
        val db = this.writableDatabase
        db.insert(NOMBRE_TABLA,null,datos)
        db.close()
    }

    fun delRegistro(id: Int): Int{
        val args = arrayOf(id.toString())
        val db = this.writableDatabase
        val borrado = db.delete(NOMBRE_TABLA,"$CAMPO_ID = ?",args)
        db.close()
        return borrado
    }

}