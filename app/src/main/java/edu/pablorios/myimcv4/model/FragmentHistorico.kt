package edu.pablorios.myimcv4.model

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.pablorios.myimcv4.utils.Datos
import edu.pablorios.myimcv4.R
import edu.pablorios.myimcv4.adapters.AdapterRegistros
import edu.pablorios.myimcv4.utils.MiSQL
import java.util.ArrayList

class FragmentHistorico(context: Context) : Fragment(),AdapterRegistros.ItemLongClickListener {

        private val adaptador : AdapterRegistros = AdapterRegistros()
        private var contexto = context
        lateinit var rvDatos: RecyclerView
        lateinit var accesoDB: MiSQL

    // Creamos la vista del fragmento
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_historico, container, false)
    }


    // Cargamos los registros en el RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Seteamos el click largo al adapter
        adaptador.setLongClickListener(this)

        // Configuramos el adaptador
        rvDatos = view.findViewById(R.id.rvDatos)
        rvDatos.setHasFixedSize(true)
        rvDatos.layoutManager = LinearLayoutManager(this.context)

        // Cargamos los registros al adapter
        adaptador.AdapterRegistros(crearListaDatos(),this.requireContext())
        rvDatos.adapter = adaptador
    }


    // Actualizamos los datos al cambiar pestaña
    override fun onResume() {
        super.onResume()
        adaptador.AdapterRegistros(crearListaDatos(),this.requireContext())
        rvDatos.adapter = adaptador
    }

    // Leemos los datos de nuestro archivo y los asignamos a nuestra variable datos para poder
    // posteriormente manejarlos
    @SuppressLint("Recycle")
    fun crearListaDatos(): MutableList<Datos> {
        val datos: MutableList<Datos> = arrayListOf()
        accesoDB = MiSQL(contexto)
        val db: SQLiteDatabase = accesoDB.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${MiSQL.NOMBRE_TABLA}",null)

        if (cursor.moveToFirst()){
            do{
                val fecha = cursor.getString(1).split("-") as ArrayList<String>
                datos.add(Datos(
                cursor.getInt(0),
                fecha[0],
                resources.getStringArray(R.array.meses)[fecha[1].toInt()-1].toString(),
                fecha[2],
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(6),
                cursor.getInt(4).toString()+"kg",
                cursor.getInt(5).toString()+"cm"))
            }while (cursor.moveToNext())
        }
        return datos
    }


    // Cerramos la conexion a la bbdd al destruir el fragment
    override fun onDestroy() {
        super.onDestroy()
        accesoDB.close()
    }


    // Funcion de borrado y actualizado del RV al click largo
    override fun onItemLongClick(view: View?, position: Int) {
        // Obtenemos los registros
        val todosRegistros = crearListaDatos()
        val registroBorrar = todosRegistros.get(position)

        val builder = AlertDialog.Builder(context)
        builder.setMessage("¿Desea eliminar el registro IMC ${registroBorrar.imc} del " +
                "${registroBorrar.dia}/${registroBorrar.mes}/${registroBorrar.ano}?")

            //Si pulsamos Ok -->
            .setPositiveButton(R.string.ok)
            { _, _ ->
                Toast.makeText(context,
                    R.string.msg_correct2,
                    Toast.LENGTH_LONG).show()

                // Actualizamos tanto la lista de registros como la bbdd borrando la posicion
                // seleccionada
                accesoDB.delRegistro(registroBorrar.id)
                todosRegistros.removeAt(position)
                adaptador.AdapterRegistros(todosRegistros,this.requireContext())
                rvDatos.adapter!!.notifyItemRemoved(position)
            }

            //Si pulsamos cancelar -->
            .setNegativeButton(R.string.cancelar)
            { _, _ ->
                Toast.makeText(context,R.string.msg_cancel2, Toast.LENGTH_LONG).show()
            }


        builder.show()


    }
}
