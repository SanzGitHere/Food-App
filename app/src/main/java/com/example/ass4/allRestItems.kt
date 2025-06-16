package com.example.ass4

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class allRestItems : AppCompatActivity() {
    lateinit var recyclerView2: RecyclerView
    lateinit var rName2: ArrayList<String>
    lateinit var rRate2: ArrayList<String>
    lateinit var rBra2: ArrayList<String>
    lateinit var rId2: ArrayList<Int>
    lateinit var db: foodSQLHelper
    lateinit var adapter2: restAdapter
    lateinit var username: String

    var cartId = 0
    var userId =0

    private fun displayData2() {
        val cursor: Cursor = db.getRestHomeData()
        if (cursor.count == 0) {
            Toast.makeText(this@allRestItems,"No entry exists", Toast.LENGTH_SHORT).show()
            return
        } else {
            while (cursor.moveToNext())
            {
                rName2.add(cursor.getString(0))
                rRate2.add(cursor.getString(1))
                rBra2.add(cursor.getString(2))
                rId2.add(cursor.getInt(3))
            }
        }
        adapter2.notifyDataSetChanged()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_rest_items)

        val intent2: Intent = intent
        username = intent2.getStringExtra("username").toString()
        cartId = intent2.getIntExtra("cartId",-1)
        userId = intent2.getIntExtra("userId", -1)

        val user: TextView = findViewById(R.id.lbl_username2)
        user.text = username
        val editTextSearch: TextView = findViewById(R.id.txt_allRestSea)

        db = foodSQLHelper(this)
        rName2 = ArrayList()
        rRate2 = ArrayList()
        rBra2 = ArrayList()
        rId2 = ArrayList()

        recyclerView2 = findViewById(R.id.rec_all_rest)
        adapter2 = restAdapter(this, rName2, rRate2, rBra2, rId2, userId, username, cartId)
        recyclerView2.adapter = adapter2
        recyclerView2.layoutManager = GridLayoutManager(this, 2) // Set number of columns to 2

        // Add space between the items in the RecyclerView
        val spaceBetweenItems = (16 * resources.displayMetrics.density).toInt() // 16dp to pixels
        //recyclerView.addItemDecoration(HorizontalSpaceItemDecoration(spaceBetweenItems))
        recyclerView2.addItemDecoration(
            allFoodItems.SpaceItemDecoration(
                spaceBetweenItems,
                spaceBetweenItems
            )
        )

        // editTextSearch = findViewById(R.id.editTextSearch)
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter data based on the text entered
                filterData(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed here

            }
        })
        displayData2()
    }
    private fun filterData(query: String) {
        // Clear current lists
        rName2.clear()
        rRate2.clear()
        rBra2.clear()
        rId2.clear()

        // Query the database with the filter criteria
        val cursor: Cursor = db.getSearchRestHomeData(query) // Modify this method to accept a filter
        if (cursor.count == 0) {
            Toast.makeText(this@allRestItems, "No results found", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor.moveToNext()) {
                rName2.add(cursor.getString(0))
                rRate2.add(cursor.getString(1))
                rBra2.add(cursor.getString(2))
                rId2.add(cursor.getInt(3))
            }
        }
        adapter2.notifyDataSetChanged()
    }
    class SpaceItemDecoration(val horizontalSpace: Int, val verticalSpace: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: android.graphics.Rect, view: android.view.View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.right = horizontalSpace
            outRect.bottom = verticalSpace

            // Add spacing to the top of the first row
            if (parent.getChildLayoutPosition(view) < 2) {
                outRect.top = verticalSpace
            }

            // Add spacing to the left of the first column
            if (parent.getChildLayoutPosition(view) % 2 == 0) {
                outRect.left = horizontalSpace
            }
        }
    }
    public fun onClickCart(view: View)
    {
        val intent = Intent(this, cus_my_cart::class.java)
        intent.putExtra("username", username)
        intent.putExtra("userId", userId)
        intent.putExtra("cartId", cartId)
        startActivity(intent)
    }
}