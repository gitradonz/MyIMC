package edu.pablorios.myimcv4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.pablorios.myimcv4.adapters.ViewPagerAdapter
import edu.pablorios.myimcv4.databinding.ActivityMainBinding
import edu.pablorios.myimcv4.model.FragmentHistorico
import edu.pablorios.myimcv4.model.FragmentImc

class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {

        // Utilizamos nuestro estilo creado quitando el SplashStyle
        setTheme(R.style.Theme_MyIMCv4)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

            // Se carga la toolbar
            setSupportActionBar(binding.toolbar)

            // Se crea el adapter
            val adapter = ViewPagerAdapter(supportFragmentManager)

            // Se añaden los fragments y los títulos de pestañas
            adapter.addFragment(FragmentImc(this), "Cálculo")
            adapter.addFragment(FragmentHistorico(this), "Histórico")

            // Se asocia el adapter
            binding.viewPager.adapter = adapter

            // Se cargan las tabs
            binding.tabs.setupWithViewPager(binding.viewPager)

        }

}