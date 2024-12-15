package com.example.consumerbasket

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var idET: EditText
    private lateinit var nameET: EditText
    private lateinit var weightET: EditText
    private lateinit var priceET: EditText

    private lateinit var saveBTN: Button
    private lateinit var editBTN: Button
    private lateinit var removeBTN: Button
    private lateinit var receiptLV: ListView

    private var adapter: ListAdapter? = null
    private var products: MutableList<Product> = mutableListOf()

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
            saveRecord()
        }
    }

    override fun onResume() {
        super.onResume()
        editBTN.setOnClickListener {
            updateRecord()
        }
        removeBTN.setOnClickListener {
            deleteRecord()
        }
    }

    private fun deleteRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)
        val choiceDeleteId = dialogView.findViewById<EditText>(R.id.deleteIdET)
        dialogBuilder.setTitle("Удалить запись")
        dialogBuilder.setMessage("Введите id")
        dialogBuilder.setPositiveButton("Удалить") { _, _ ->
            val deleteId = choiceDeleteId.text.toString()
            if (deleteId.trim() != "") {
                val product = Product(Integer.parseInt(deleteId), "", 0.0, 0.0)
                db.deleteProduct(product)
                viewDataAdapter()
                Toast.makeText(applicationContext, "Данные удалены", Toast.LENGTH_LONG).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена") { _, _ ->
        }
        dialogBuilder.create().show()
    }

    private fun updateRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)
        val editId = dialogView.findViewById<EditText>(R.id.updateIdET)
        val editName = dialogView.findViewById<EditText>(R.id.updateNameET)
        val editWeight = dialogView.findViewById<EditText>(R.id.updateWeightET)
        val editPrice = dialogView.findViewById<EditText>(R.id.updatePriceET)
        dialogBuilder.setTitle("Обновить запись")
        dialogBuilder.setMessage("Введите данные ниже")
        dialogBuilder.setPositiveButton("Обновить") { _, _ ->
            val updateID = editId.text.toString()
            val updateName = editName.text.toString()
            val updateWeight = editWeight.text.toString()
            val updatePrice = editPrice.text.toString()
            if (updateID.trim() != "" && updateName.trim() != "" && updateWeight.trim() != "" && updatePrice != "") {
                val product = Product(
                    Integer.parseInt(updateID),
                    updateName,
                    updateWeight.toDouble(),
                    updatePrice.toDouble()
                )
                db.updateProduct(product)
                viewDataAdapter()
                Toast.makeText(applicationContext, "Данные обновлены", Toast.LENGTH_LONG).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена") { _, _ ->

        }
        dialogBuilder.create().show()


    }

    private fun viewDataAdapter() {
        products = db.readProducts()
        adapter = ListAdapter(this, products)
        receiptLV.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    private fun saveRecord() {
        val id = idET.text.toString()
        val name = nameET.text.toString()
        val weight = weightET.text.toString()
        val price = priceET.text.toString()
        if (id.trim() != "" && name.trim() != "" && weight.trim() != "" && price.trim() != "") {
            val product = Product(
                Integer.parseInt(id),
                name,
                weight.toDouble(),
                price.toDouble()
            )
            products.add(product)
            db.addProduct(product)
            Toast.makeText(applicationContext, "Продукт добавлен", Toast.LENGTH_LONG).show()
            clearFields()
            viewDataAdapter()
        }
    }

    private fun clearFields() {
        nameET.text.clear()
        weightET.text.clear()
        priceET.text.clear()
    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
        idET = findViewById(R.id.idET)
        nameET = findViewById(R.id.nameET)
        weightET = findViewById(R.id.weightET)
        priceET = findViewById(R.id.priceET)
        saveBTN = findViewById(R.id.saveBTN)
        editBTN = findViewById(R.id.editBTN)
        removeBTN = findViewById(R.id.removeBTN)
        receiptLV = findViewById(R.id.receiptLV)
        viewDataAdapter()
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