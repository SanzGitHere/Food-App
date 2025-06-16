package com.example.ass4

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class couponPopup : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var db: foodSQLHelper
    lateinit var adapter: couponAdapter

    lateinit var cCodes:ArrayList<String>
    lateinit var cDisAmo: ArrayList<String>
   lateinit var disAmo: ArrayList<Double>
    lateinit var disType: ArrayList<String>

    lateinit var username: String
    var cartId: Int = 1
    var userId: Int = 1
    var bill: Double = 0.00

    fun displayCoupon() {
        val cursor: Cursor = db.getCouponData(bill)
        if (cursor.count == 0) {
            Toast.makeText(this@couponPopup,"No entry exists", Toast.LENGTH_SHORT).show()
            return
        } else {
            while (cursor.moveToNext())
            {
                cCodes.add(cursor.getString(0))
                disAmo.add(cursor.getDouble(1))
                disType.add(cursor.getString(2))
            }
        }
        adapter.notifyDataSetChanged()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_popup)
        db = foodSQLHelper(this)
        recyclerView = findViewById(R.id.rec_coupons)

        val intent2: Intent = intent
        username = intent2.getStringExtra("username").toString()
        cartId = intent2.getIntExtra("cartId",-1)
        userId = intent2.getIntExtra("userId", -1)
        bill = intent2.getDoubleExtra("sTot",-1.00)
        cCodes = ArrayList()
        cDisAmo = ArrayList()
        disAmo = ArrayList()
        disType = ArrayList()
        //bill = 100.0 // example bill value

        adapter = couponAdapter(this, db, cCodes, cDisAmo, disAmo, disType, bill, userId, username, cartId)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        displayCoupon()
    }
    fun returnResult(couponCode: String?, couponAmount: String?) {
        val resultIntent = Intent()
        resultIntent.putExtra("couponCode", couponCode)
        resultIntent.putExtra("couponAmount", couponAmount)
        resultIntent.putExtra("username", username)
        resultIntent.putExtra("userId", userId)
        resultIntent.putExtra("cartId", cartId)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}