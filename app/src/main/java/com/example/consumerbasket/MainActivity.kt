package com.example.consumerbasket

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var nameET: EditText
    private lateinit var weightET: EditText
    private lateinit var priceET: EditText

    private lateinit var saveBTN: Button
    private lateinit var receiptLV: ListView

    private var adapter: ListAdapter? = null

    private val db = DBHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        setSupportActionBar(toolbar)

        saveBTN.setOnClickListener {
            if (nameET.text.isEmpty()
                || weightET.text.isEmpty()
                || priceET.text.isEmpty()
            )
                return@setOnClickListener
            val name = nameET.text.toString()
            val weight = weightET.text.toString().toDoubleOrNull() ?: 0.0
            val price = priceET.text.toString().toDoubleOrNull() ?: 0.0

            val product = Product(name, weight, price)
            db.addProduct(product)
            adapter = ListAdapter(this, db.readProducts())
            receiptLV.adapter = adapter
            clearFields()
        }
    }

    private fun clearFields() {
        nameET.text.clear()
        weightET.text.clear()
        priceET.text.clear()
    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
        nameET = findViewById(R.id.nameET)
        weightET = findViewById(R.id.weightET)
        priceET = findViewById(R.id.priceET)
        saveBTN = findViewById(R.id.saveBTN)
        receiptLV = findViewById(R.id.receiptLV)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exitMenu) finishAffinity()
        return super.onOptionsItemSelected(item)
    }
}