package com.example.drinkmate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.drinkmate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Scan())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.scan -> replaceFragment(Scan())
                R.id.drinks -> replaceFragment(Drinks())
                R.id.account -> replaceFragment(Account())
                R.id.social -> replaceFragment(Social())
                R.id.more -> replaceFragment(More())
                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, fragment)
        fragmentTransaction.commit()
    }
}