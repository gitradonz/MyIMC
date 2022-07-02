package edu.pablorios.myimcv4.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.pablorios.myimcv4.utils.Datos
import edu.pablorios.myimcv4.R
import com.google.android.material.snackbar.Snackbar

class AdapterRegistros: RecyclerView.Adapter<AdapterRegistros.ViewHolder>() {

    var datos: MutableList<Datos> = ArrayList()
    lateinit var contexto: Context
    private lateinit var mLongClickListener: ItemLongClickListener

    interface ItemLongClickListener {
        fun onItemLongClick(view: View?, position: Int)
    }

    fun setLongClickListener(itemLongClickListener: ItemLongClickListener){
        mLongClickListener = itemLongClickListener
    }
    //Constructor de nuestro adaptador
    fun AdapterRegistros (lista: MutableList<Datos>, contexto: Context){
        this.datos = lista
        this.contexto = contexto
    }

    //Función para obtener el total de tamaño de nuestra lista de datos
    override fun getItemCount(): Int {return datos.size}

    //Función para bindear los datos a la vista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dato = datos[position]
        holder.bind(dato,contexto)
    }

    //Función para inflar la vista con los datos obtenidos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(
            R.layout.elementos_registros,parent,false))
    }

    //Vinculamos nuestras vistas a nuestros datos
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val mes = view.findViewById(R.id.tvMes) as TextView
        private val dia = view.findViewById(R.id.tvDia) as TextView
        private val ano = view.findViewById(R.id.tvAno) as TextView
        private val sexo = view.findViewById(R.id.tvSexo) as TextView
        private val imc = view.findViewById(R.id.tvIMC) as TextView
        private val tipo = view.findViewById(R.id.tvTipo) as TextView
        private val peso = view.findViewById(R.id.tvPeso) as TextView
        private val altura = view.findViewById(R.id.tvAltura) as TextView

        fun bind(dato: Datos, context: Context){
            mes.text = dato.mes
            dia.text = dato.dia
            ano.text = dato.ano
            sexo.text = dato.sexo
            imc.text = dato.imc
            tipo.text = dato.tipo
            peso.text = dato.peso
            altura.text = dato.altura

            //Funcion para mostrar snackbar en el click de item
            itemView.setOnClickListener{
                Snackbar.make(itemView,"${tipo.text} (${imc.text})", Snackbar.LENGTH_SHORT).show()
            }

            // Funcion para el click largo
            itemView.setOnLongClickListener {
                mLongClickListener.onItemLongClick(it, adapterPosition)
                true }
        }
    }

}
