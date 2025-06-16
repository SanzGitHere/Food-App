package com.example.ass4

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class cus_my_cart : AppCompatActivity() {
    lateinit var dbb: SQLiteDatabase

    lateinit var recyclerView: RecyclerView
    lateinit var fName2: ArrayList<String>
    lateinit var rName2: ArrayList<String>
    lateinit var fPrice2: ArrayList<String>
    lateinit var fTot2: ArrayList<String>
    lateinit var fSize2: ArrayList<String>
    lateinit var fTop2: ArrayList<String>
    lateinit var fQua: ArrayList<Int>  // Changed to ArrayList<Int>
    lateinit var fId2: ArrayList<Int>  // Changed to ArrayList<Int>
    lateinit var fCid2: ArrayList<Int>  // Changed to ArrayList<Int>
    lateinit var db: foodSQLHelper
    lateinit var adapter: cartAdapter
    var fTotPri: Double = 0.00
    var sTot: Double = 0.00
    var tax: Double = 0.00
    var delivery: Double = 10.00
    var fiTot: Double = 0.00
    var prom: Double = 0.00
    lateinit var couponCode: String
    var couponAmount: Double = 0.00
    lateinit var username: String
    var cartId: Int = 1
    var userId: Int = 1
    var couponAmountS = 0.00

    private fun displayData() {
        val cursor: Cursor = db.getFoodCartData(cartId)
        if (cursor.count == 0) {
            Toast.makeText(this@cus_my_cart, "No entry exists", Toast.LENGTH_SHORT).show()
            return
        } else {
            while (cursor.moveToNext()) {
                fName2.add(cursor.getString(0))
                rName2.add(cursor.getString(2))
                fPrice2.add(cursor.getString(1))
                fQua.add(cursor.getInt(3))  // Use getInt() here
                fId2.add(cursor.getInt(4))  // Use getInt() here
                fCid2.add(cursor.getInt(5))  // Use getInt() here
                fSize2.add(cursor.getString(6))
                fTop2.add(cursor.getString(7))
                val price = cursor.getDouble(1)
                val quantity = cursor.getInt(3)
                fTotPri = price * quantity
                sTot += fTotPri
            }
        }
    }

    private fun getRestId(): Int
    {
        var rest_id: Int = 0
        val cursor: Cursor = db.getRestId(cartId)
        if (cursor.count == 0) {
            Toast.makeText(this@cus_my_cart, "No entry exists", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor.moveToNext()) {
                rest_id = cursor.getInt(0)

            }
        }
        return rest_id
    }

    private fun openCouponPopup() {

        // Create an intent to start the CouponPopupActivity
        val intent: Intent = Intent(this@cus_my_cart,couponPopup::class.java)
        intent.putExtra("username", username)
        intent.putExtra("userId", userId)
        intent.putExtra("cartId", cartId)
        intent.putExtra("sTot", sTot)
        startActivityForResult(intent, 1)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cus_my_cart)

        val intent2: Intent = intent
        username = intent2.getStringExtra("username").toString()
        cartId = intent2.getIntExtra("cartId",-1)
        userId = intent2.getIntExtra("userId", -1)

        couponCode = intent2.getStringExtra("couponCode").toString()
        couponAmountS = intent2.getDoubleExtra("couponAmount",0.00)

        val promm: TextView = findViewById(R.id.lbl_fPro)
        val couNamee:EditText = findViewById(R.id.txt_cou)
        //promm.text = couponAmount.toString()
        promm.text = String.format("%.2f", couponAmountS)
        couNamee.setText(couponCode)

        db = foodSQLHelper(this)
        fName2 = ArrayList()
        rName2 = ArrayList()
        fPrice2 = ArrayList()
        fTot2 = ArrayList()  // Initialize fTot2
        fSize2 = ArrayList()
        fTop2 = ArrayList()
        fQua = ArrayList()
        fId2 = ArrayList()
        fCid2 = ArrayList()

        recyclerView = findViewById(R.id.rec_cartItem)
        adapter = cartAdapter(this, fName2, rName2, fPrice2, fTot2,fSize2,fTop2, fQua,fId2,fCid2,db)  // Pass fTot2 to adapter

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add space between the items in the RecyclerView
        val spaceBetweenItems = (16 * resources.displayMetrics.density).toInt() // 16dp to pixels
        recyclerView.addItemDecoration(VerticalSpaceItemDecoration(spaceBetweenItems))
       // displayData()
        //updateTotals()  // Method to update totals
        val applyButton: AppCompatButton = findViewById(R.id.btn_appCou)

        applyButton.setOnClickListener { v -> openCouponPopup() }
        displayData()
        updateTotals()  // Method to update totals
    }

    private fun updateTotals() {
        val sTott: TextView = findViewById(R.id.lbl_sTot)
        val deliveryy: TextView = findViewById(R.id.lbl_fDeli)
        val taxx: TextView = findViewById(R.id.lbl_tax)
        val fiTott: TextView = findViewById(R.id.lbl_fiTot)
        val promm: TextView = findViewById(R.id.lbl_fPro)

        if (sTot > 100) {
            tax = sTot * 0.2
        } else {
            tax = 0.0
        }

        fiTot = sTot + delivery + tax - couponAmountS
        sTott.text = String.format("%.2f", sTot)
        deliveryy.text = String.format("%.2f", delivery)
       // promm.text = String.format("%.2f", prom)
        taxx.text = String.format("%.2f", tax)
        fiTott.text = String.format("%.2f", fiTot)
    }

    public fun refreshCart() {
        // Clear existing data
        fName2.clear()
        rName2.clear()
        fPrice2.clear()
        fTot2.clear()
        fQua.clear()
        fId2.clear()
        fSize2.clear()
        fTop2.clear()
        fCid2.clear()
        sTot = 0.00;

        // Reload data from the database
        displayData()

        // Notify the adapter
        adapter.notifyDataSetChanged()

        // Update totals
        updateTotals()
    }
    class VerticalSpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: android.graphics.Rect, view: android.view.View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.bottom = space
        }
    }

    fun onClickOrder(view: View)
    {
        val payMet: Spinner = findViewById(R.id.spi_pay)
        val pMet = payMet.selectedItem.toString()
        if(pMet == "Card")
        {
            val intent = Intent(this, cardPayment::class.java)
            intent.putExtra("username", username)
            intent.putExtra("cartId", cartId)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        else
        {
            val currentDate: Date = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateString: String = dateFormat.format(currentDate)

            val restId = getRestId()

            val values = ContentValues()
            values.put("customer_id", userId)
            values.put("restaurant_id", restId)
            values.put("cart_id", cartId)
            values.put("order_date", dateString)
            values.put("total_amount", fiTot)
            values.put("status", "Ongoing")

            val valuess = ContentValues()
            valuess.put("customer_id", userId)

            val sqHelp: foodSQLHelper = foodSQLHelper(this)
            dbb = sqHelp.readableDatabase
            val newRowId = dbb.insert("Orders", null, values)

            if (newRowId != -1L) {
                dbb.insert("Cart", null, valuess)
                Toast.makeText(this, "Order inserted successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Order insertion failed.", Toast.LENGTH_SHORT).show()
            }

            dbb.close()
        }

    }
}
