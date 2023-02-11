package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button

class Addiction : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addiction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Creates values to reference for all the buttons on the app addiction page
        val samshaButton: Button = view.findViewById<Button>(R.id.samsha)
        val nihButton: Button = view.findViewById<Button>(R.id.nih)
        val cdcButton: Button = view.findViewById<Button>(R.id.cdc)
        val adcButton: Button = view.findViewById<Button>(R.id.adc)
        val cdcrButton: Button = view.findViewById<Button>(R.id.cdcr)
        val myWebView = view.findViewById<WebView>(R.id.wbview)

        // Checks to see if a button is clicked, then it sends you to the following embedded web-page
        samshaButton.setOnClickListener() {
            myWebView.loadUrl("https://www.samhsa.gov/find-help/national-helpline")
            myWebView.bringToFront()
        }
        nihButton.setOnClickListener() {
            myWebView.loadUrl("https://www.niaaa.nih.gov/publications/brochures-and-fact-sheets/treatment-alcohol-problems-finding-and-getting-help")
            myWebView.bringToFront()
        }
        cdcButton.setOnClickListener() {
            myWebView.loadUrl("https://www.cdc.gov/alcohol/fact-sheets/prevention.htm")
            myWebView.bringToFront()
        }
        adcButton.setOnClickListener() {
            myWebView.loadUrl("https://americanaddictioncenters.org/alcoholism-treatment")
            myWebView.bringToFront()
        }
        cdcrButton.setOnClickListener() {
            myWebView.loadUrl("https://www.cdcr.ca.gov/wellness/health-and-wellbeing-employee-family-resource-guide/")
            myWebView.bringToFront()
        }
    }
}