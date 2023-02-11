package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

// Class for the Blood Alcohol Content calculator.
class Calculator : Fragment() {

    // Inflates the fragment view so that it shows the fragment on the screen
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calculator, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Creates the values of the Spinner that lets the user choose between male and female
        val genders = arrayOf("Male", "Female")
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        var genderCoeff : Double = 0.0
        if (spinner != null) {
            // Sets the spinner's values to the previously created values.
            val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, genders)
            spinner.adapter = arrayAdapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    // Notifies the user through a toast what they selected as their gender
                    Toast.makeText(requireContext(), "Selected gender is: " + genders[p2], Toast.LENGTH_SHORT).show()
                    if(genders[p2].equals("Male")) {
                        genderCoeff = 0.68
                    } else {
                        genderCoeff = 0.55
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
        var totalGrams: Double = 0.0
        super.onViewCreated(view, savedInstanceState)
        // References the buttons that were created in the design XML file
        val calcButton : Button = view.findViewById<Button>(R.id.calculateButton)
        val addButton : Button = view.findViewById<Button>(R.id.addDrinkButton)
        // Adds functionality to the Add Drink button.
        addButton.setOnClickListener() {
            // Creates values of the user inputs of ml and abv
            val ouncesField : EditText = view.findViewById<EditText>(R.id.ozText)
            val abvField : EditText = view.findViewById<EditText>(R.id.abvText)
            val ml = ouncesField.text.toString().toInt()
            val abv = abvField.text.toString().toInt()
            // Calculation of grams of alochol in the drink
            if ((ml > 0) && (abv > 0)) {
                Toast.makeText(requireContext(), "Added $ml ml drink with $abv% alcohol by volume" , Toast.LENGTH_SHORT).show()
                totalGrams += (((abv.toDouble() * ml.toDouble()) / 1000) * 8)
                ouncesField.setText("")
                abvField.setText("")
            }
        }
        // Adds functionality to the calculate button.
        calcButton.setOnClickListener() {
            if (totalGrams > 0) {
                val weight : Int = view.findViewById<EditText>(R.id.editWeight).text.toString().toInt()
                val hours : Int = view.findViewById<EditText>(R.id.editHours).text.toString().toInt()
                val total = ((((totalGrams.toDouble()) / (weight.toDouble() * 454.toDouble() * genderCoeff)) * 100.toDouble()) - (hours.toDouble() * .015))
                view.findViewById<TextView>(R.id.bacResult).setText("Your BAC is: $total")
                totalGrams = 0.0
            }
        }
    }
}