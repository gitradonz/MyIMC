package edu.pablorios.myimcv4.model

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import edu.pablorios.myimcv4.R
import edu.pablorios.myimcv4.databinding.FragmentImcBinding
import com.google.android.material.snackbar.Snackbar
import edu.pablorios.myimcv4.utils.MiSQL
import java.util.*

class FragmentImc(context: Context) : Fragment() {

    lateinit var accesoDB: MiSQL
    private var _binding: FragmentImcBinding? = null
    private val binding get() = _binding!!
    private val contexto = context

    // Creamos la vista del fragmento
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentImcBinding.inflate(inflater, container, false)
        return binding.root
    }


    // Funcion para realizar despues de la creacion de la vista (Funciones de boton)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inicializamos la conexion a la BBDD
        accesoDB = MiSQL(contexto)
        //Funcion del boton de Calcular (Guarda en el registro el cálculo)
        binding.btCalc.setOnClickListener {
            if (binding.n1.text.isNotBlank() && binding.n2.text.isNotBlank())
            {miDialog()}
            else
            {Snackbar.make(binding.root, getString(R.string.alertaNoDatos), Snackbar.LENGTH_LONG).show()}
        }
    }


    //Funcion para nuestro dialog al guardar
    fun miDialog(){
        val builder = AlertDialog.Builder(binding.root.context)

        builder.setMessage(R.string.dialog).setPositiveButton(R.string.ok)
            //Si pulsamos Ok -->
            { dialog, which ->
                Toast.makeText(this.context,getString(R.string.msg_correct), Toast.LENGTH_LONG).show()
                calculoPeso(true)}

            //Si pulsamos cancelar -->
            .setNegativeButton(R.string.cancelar)
            { dialog, which ->
                Snackbar.make(binding.root,getString(R.string.msg_cancel), Snackbar.LENGTH_LONG).show()
                calculoPeso(false)}
        builder.show()
    }



    //Funcion para realizar el cálculo del IMC y enviarlo a "checkeo()"
    fun calculoPeso(c: Boolean) {
        val n1 = binding.n1.text.toString().toInt()
        val n2 = binding.n2.text.toString().toInt()
        val total: Double = n1.toDouble() / (n2.toDouble() * n2.toDouble() / 10000)
        total.toString()
        binding.tTot.text = String.format("%.2f", total)
        if (binding.radioMasculino.isChecked)
            {checkeo(true, c, total, n1, n2)}
        else {checkeo(false, c, total, n1, n2)}
    }



    //Funcion para asignar texto dependiendo el IMC y guardarlo en la base de datos
    //Recibe 1 booleano para comprobar si hombre o mujer , otro para saber si guardar datos y los datos

    fun checkeo(b: Boolean, c: Boolean, total: Double, n1: Int, n2: Int){

        // Obtenemos la fecha actual
        val hoy= Calendar.getInstance()
        val fecha:String = hoy.get(Calendar.DAY_OF_MONTH).toString()+"-"+
                hoy.get(Calendar.MONTH)+1.toString()+"-"+
                hoy.get(Calendar.YEAR).toString()

        if (b){
            if (total<18.5)binding.tRes.text = getString(R.string.delgado)
            if (total in 18.5..24.99)binding.tRes.text = getString(R.string.normal)
            if (total in 25.0..29.99)binding.tRes.text = getString(R.string.sobrepeso)
            if (total>=30)binding.tRes.text = getString(R.string.obesidad)

            // Guardamos el registro en la BBDD en caso de seleccionar guardar
            if (c) accesoDB.addRegistro(fecha,"Hombre",
                String.format("%.2f", total),n1, n2,
                binding.tRes.text.toString())

        }else {
            if (total<18.5)binding.tRes.text = getString(R.string.delgada)
            if (total in 18.5..23.99)binding.tRes.text = getString(R.string.normal)
            if (total in 24.0..28.99)binding.tRes.text = getString(R.string.sobrepeso)
            if (total>=29)binding.tRes.text = getString(R.string.obesidad)

            // Guardamos el registro en la BBDD en caso de seleccionar guardar
            if (c) accesoDB.addRegistro(fecha,"Mujer",
                String.format("%.2f", total),n1, n2,
                binding.tRes.text.toString())
        }
        view?.hideKeyboard()
    }

    // Función para ocultar el teclado
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}


