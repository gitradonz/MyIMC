package edu.pablorios.myimcv4.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(FragmentManager: FragmentManager) : FragmentStatePagerAdapter(FragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()


    // Obtenemos el ítem
    override fun getItem(position: Int): Fragment {return mFragmentList[position]}

    // Obtenemos el total de tabs
    override fun getCount(): Int {return mFragmentList.size}

    // Obtenemos el título del tab
    override fun getPageTitle(position: Int): CharSequence {return mFragmentTitleList[position]}

    // Nuestra funcion para añadir los fragments a nuestras listas
    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }


}