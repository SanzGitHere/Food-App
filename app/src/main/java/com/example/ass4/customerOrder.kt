package com.example.ass4

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class customerOrder : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var oId: ArrayList<String>
    lateinit var oStatus: ArrayList<String>
    lateinit var oTotal: ArrayList<String>
    lateinit var oDate: ArrayList<String>
    lateinit var db: foodSQLHelper
    lateinit var adapter: orderAdapter
    lateinit var username: String

    var cartId = 0
    var userId = 0

    private fun displayData1() {
        val cursor: Cursor = db.getOrderData(userId)
        if (cursor.count == 0) {
            Toast.makeText(this@customerOrder,"No entry exists", Toast.LENGTH_SHORT).show()
            return
        } else {
            while (cursor.moveToNext())
            {
                oId.add(cursor.getString(0))
                oStatus.add(cursor.getString(3))
                oTotal.add(cursor.getString(2))
                oDate.add(cursor.getString(1))
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_order)

        val intent2: Intent = intent
        username = intent2.getStringExtra("username").toString()
        cartId = intent2.getIntExtra("cartId",-1)
        userId = intent2.getIntExtra("userId", -1)
        val user: TextView = findViewById(R.id.lbl_uName)
        user.text = username

       // var spiSearch: Spinner = findViewById(R.id.spi_ord)

        db = foodSQLHelper(this)
        oId= ArrayList()
        oStatus= ArrayList()
        oTotal= ArrayList()
        oDate= ArrayList()

        recyclerView = findViewById(R.id.rec_all_Corders)
        adapter = orderAdapter(this, oStatus, oDate, oTotal, oId, cartId, userId, username)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add space between the items in the RecyclerView
        val spaceBetweenItems = (16 * resources.displayMetrics.density).toInt() // 16dp to pixels
        recyclerView.addItemDecoration(VerticalSpaceItemDecoration(spaceBetweenItems))

        displayData1()
    }
    class VerticalSpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: android.graphics.Rect,
            view: android.view.View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = space
        }
    }
}