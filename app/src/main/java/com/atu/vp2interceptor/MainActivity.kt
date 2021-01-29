package com.atu.vp2interceptor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var fragments = ArrayList<Fragment>()
    private val titles = arrayListOf("第一个","第二个")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vp2.adapter = object : FragmentStateAdapter(this){

            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment {
                return when(position){
                    0 -> FirstFragment()
                    1 -> SecondFragment.newInstance()
                    else -> FirstFragment()
                }
            }
        }
        TabLayoutMediator(tab,vp2,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = titles[position]
            }).attach()
    }
}