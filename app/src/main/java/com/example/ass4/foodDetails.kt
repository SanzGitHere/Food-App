package com.example.ass4

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class foodDetails : AppCompatActivity() {

    lateinit var dbb: SQLiteDatabase

    lateinit var fName2: TextView
    lateinit var fPrice2: TextView
    lateinit var fReName2: TextView
    lateinit var fTime2: TextView
    lateinit var fCal2: TextView
    lateinit var fStar2: TextView
    lateinit var fDes2: TextView
    var foodId: Int = 0
    var cartId: Int = 0
    var userId: Int = 0
    var user = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)
        val intent2: Intent = intent
        foodId = intent2.getIntExtra("foodId", -1)
        cartId = intent2.getIntExtra("cartId", -1)
        userId = intent2.getIntExtra("userId", -1)
        user = intent2.getStringExtra("username").toString()

        fName2 = findViewById(R.id.txt_fo_name2)
        fPrice2 = findViewById(R.id.txt_fo_price2)
        fReName2 = findViewById(R.id.txt_fo_reName2)
        fTime2 = findViewById(R.id.txt_fomin)
        fCal2 = findViewById(R.id.txt_cal)
        fStar2 = findViewById(R.id.txt_star)
        fDes2 = findViewById(R.id.lbl_f)

        val quanity: TextView = findViewById(R.id.lbl_qua2)
        val total: TextView = findViewById(R.id.txt_fototalp)
        val size: Spinner = findViewById(R.id.spi_size)
        val top: Spinner = findViewById(R.id.spi_top)
        val sqHelp: foodSQLHelper = foodSQLHelper(this)
        dbb = sqHelp.readableDatabase

        if (foodId != -1) {
            val cursor: Cursor = dbb.rawQuery(
                "SELECT fd.name, re.name, fd.price, fd.energy, fd.rate, fd.time, fd.des FROM FoodDish fd JOIN Restaurant re ON re.restaurant_id = fd.restaurant_id WHERE fd.dish_id = ?", arrayOf(foodId.toString())
            )

            if (cursor.moveToFirst()) {
                fName2.text = cursor.getString(0)
                fReName2.text = cursor.getString(1)
                fPrice2.text = cursor.getString(2)
                fCal2.text = cursor.getString(3)
                fStar2.text = cursor.getString(4)
                fTime2.text = cursor.getString(5)
                fDes2.text = cursor.getString(6)
                val unitPrice: Double = cursor.getDouble(2)
                calculateTotal(unitPrice, quanity, size, top, total)
            } else {
                Toast.makeText(this, "No food details found for the provided ID.", Toast.LENGTH_LONG).show()
            }
            cursor.close()
        } else {
            Toast.makeText(this, "Invalid Food ID.", Toast.LENGTH_LONG).show()
        }

        // Add a listener to update the total when the size is changed
        size.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Recalculate the total when the spinner selection changes
                val unitPr = fPrice2.text.toString().toDoubleOrNull() ?: 0.0
                calculateTotal(unitPr, quanity, size, top, total)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle the case where no item is selected
            }
        }
        top.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Recalculate the total when the spinner selection changes
                val unitPr = fPrice2.text.toString().toDoubleOrNull() ?: 0.0
                calculateTotal(unitPr, quanity, size, top, total)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle the case where no item is selected
            }
        }
    }


    fun onClickIncQua(view: View) {
        updateQuantity(view, 1)
    }

    fun onClickDecQua(view: View) {
        updateQuantity(view, -1)
    }

    private fun updateQuantity(view: View, change: Int) {
        val quanity: TextView = findViewById(R.id.lbl_qua2)
        val unitPrice: TextView = findViewById(R.id.txt_fo_price2)
        val total: TextView = findViewById(R.id.txt_fototalp)
        val size: Spinner = findViewById(R.id.spi_size)
        val top: Spinner = findViewById(R.id.spi_top)

        var qua = quanity.text.toString().toIntOrNull() ?: 1
        qua += change
        if (qua < 1) qua = 1 // Ensure quantity doesn't go below 1
        quanity.text = qua.toString()

        val unitPr = unitPrice.text.toString().toDoubleOrNull() ?: 0.0
        calculateTotal(unitPr, quanity, size, top, total)
    }

    public fun onClickAddCart(view: View)
    {
        var dish_id: Int = 0
        var unitPrice: Double = 0.00
        val quaa: TextView = findViewById(R.id.lbl_qua2)
        val quantity = quaa.text
        val size: Spinner = findViewById(R.id.spi_size)
        val top: Spinner = findViewById(R.id.spi_top)
        val sizeTxt = size.selectedItem.toString()
        val topTxt = top.selectedItem.toString()
        val qua = quantity.toString().toIntOrNull() ?: 1
        if (foodId != -1) {
            val cursor: Cursor = dbb.rawQuery(
                "SELECT dish_id, price FROM FoodDish WHERE dish_id = ?", arrayOf(foodId.toString())
            )

            if (cursor.moveToFirst()) {
                dish_id = cursor.getInt(0)
                val dishUnitPrice = cursor.getDouble(1)

                unitPrice = calculateUPrice( dishUnitPrice, size, top)
            } else {
                Toast.makeText(this, "No food details found for the provided ID.", Toast.LENGTH_LONG).show()
            }
            cursor.close()
            val values = ContentValues()
            values.put("cart_id", cartId)
            values.put("dish_id", dish_id)
            values.put("size", sizeTxt)
            values.put("topping", topTxt)
            values.put("unitPrice", unitPrice)
            values.put("quantity", qua)

            dbb.insert("cartFoodDish", null, values)

            val intent = Intent(this, cusMenuAct::class.java)
            intent.putExtra("username", user)
            intent.putExtra("userId", userId)
            intent.putExtra("cartId", cartId)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Invalid Food ID.", Toast.LENGTH_LONG).show()
        }
    }

    private fun calculateUPrice(unitPrice: Double, size: Spinner, top: Spinner): Double {
        val sizeType = size.selectedItem.toString()
        val topType = top.selectedItem.toString()
        val incPriceTop = when (topType) {
            "None" -> 0.0
            else -> unitPrice * 0.1
        }
        val incPrice = when (sizeType) {
            "Large" -> unitPrice * 0.6
            "Medium" -> unitPrice * 0.3
            else -> 0.0
        }
        val pri = unitPrice + incPrice + incPriceTop
        return pri
    }

    private fun calculateTotal(unitPrice: Double, quanity: TextView, size: Spinner, top: Spinner, total: TextView) {
        val qua = quanity.text.toString().toIntOrNull() ?: 1
        val sizeType = size.selectedItem.toString()
        val topType = top.selectedItem.toString()
        val incPriceTop = when (topType) {
            "None" -> 0.0
            else -> unitPrice * 0.1
        }
        val incPrice = when (sizeType) {
            "Large" -> unitPrice * 0.6
            "Medium" -> unitPrice * 0.3
            else -> 0.0
        }
        val tot = (unitPrice + incPrice + incPriceTop) * qua
        total.text = String.format("%.2f", tot)
    }
}
