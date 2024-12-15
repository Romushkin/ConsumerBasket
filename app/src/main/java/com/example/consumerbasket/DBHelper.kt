package com.example.consumerbasket

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "CONSUMER_BASKET_DATABASE"
        private val DATABASE_VERSION = 1
        private val TABLE_NAME = "consumer_basket_table"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_WEIGHT = "weight"
        private val KEY_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val USER_TABLE = "CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_WEIGHT REAL, $KEY_PRICE REAL)"
        db?.execSQL(USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.id)
        contentValues.put(KEY_NAME, product.name)
        contentValues.put(KEY_WEIGHT, product.weight)
        contentValues.put(KEY_PRICE, product.price)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    @SuppressLint("Range")
    fun readProducts(): MutableList<Product> {
        val productList = mutableListOf<Product>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return productList
        }
        var id: Int
        var name: String
        var weight: Double
        var price: Double
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                weight = cursor.getDouble(cursor.getColumnIndex(KEY_WEIGHT))
                price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE))
                val product = Product(id, name, weight, price)
                productList.add(product)
            } while (cursor.moveToNext())
        }
        return productList
    }

    fun updateProduct(product: Product) {
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.id)
        contentValues.put(KEY_NAME, product.name)
        contentValues.put(KEY_WEIGHT, product.weight)
        contentValues.put(KEY_PRICE, product.price)
        val db = this.writableDatabase
        db.update(TABLE_NAME, contentValues, "id=" + product.id, null)
        db.close()
    }

    fun deleteProduct(product: Product) {
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.id)
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "id=" + product.id, null)
        db.close()
    }

}