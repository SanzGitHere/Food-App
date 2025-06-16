package com.example.ass4

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class allFoodItems : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var fName: ArrayList<String>
    lateinit var rName: ArrayList<String>
    lateinit var fRate: ArrayList<String>
    lateinit var fTime: ArrayList<String>
    lateinit var fPrice: ArrayList<String>
    lateinit var fId4: ArrayList<Int>
    lateinit var db: foodSQLHelper
    lateinit var adapter: foodAdapter
    lateinit var username: String
    lateinit var fCate: String

    var cartId = 0
    var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_food_items)

        val intent2: Intent = intent
        username = intent2.getStringExtra("username").toString()
        cartId = intent2.getIntExtra("cartId",-1)
        userId = intent2.getIntExtra("userId", -1)
        fCate = intent2.getStringExtra("fCate").toString()
        var user: TextView = findViewById(R.id.lbl_username2)
        user.text = username
        var editTextSearch: TextView = findViewById(R.id.txt_allFoodSea)

        db = foodSQLHelper(this)
        fName = ArrayList()
        rName = ArrayList()
        fRate = ArrayList()
        fTime = ArrayList()
        fId4 = ArrayList()
        fPrice = ArrayList()

        recyclerView = findViewById(R.id.rec_all_food)
        adapter = foodAdapter(this, fName, rName, fRate, fTime, fId4, fPrice, userId, username, cartId)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2) // Set number of columns to 2

        // Add space between the items in the RecyclerView
        val spaceBetweenItems = (16 * resources.displayMetrics.density).toInt() // 16dp to pixels
        //recyclerView.addItemDecoration(HorizontalSpaceItemDecoration(spaceBetweenItems))
        recyclerView.addItemDecoration(SpaceItemDecoration(spaceBetweenItems, spaceBetweenItems))

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
        if (fCate == "Null")
        {
            displayData1()
        }
        else
        {
            displayDataCate()
        }

    }

    private fun displayDataCate() {
        val cursor: Cursor = db.getFoodCateHomeData(fCate)
        if (cursor.count == 0) {
            Toast.makeText(this@allFoodItems,"No entry exists", Toast.LENGTH_SHORT).show()
            return
        } else {
            while (cursor.moveToNext())
            {
                fName.add(cursor.getString(0))
                rName.add(cursor.getString(1))
                fRate.add(cursor.getString(3))
                fTime.add(cursor.getString(5))
                fId4.add(cursor.getInt(4))
                fPrice.add(cursor.getString(2))
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun displayData1() {
        val cursor: Cursor = db.getFoodHomeData()
        if (cursor.count == 0) {
            Toast.makeText(this@allFoodItems,"No entry exists", Toast.LENGTH_SHORT).show()
            return
        } else {
            while (cursor.moveToNext())
            {
                fName.add(cursor.getString(0))
                rName.add(cursor.getString(1))
                fRate.add(cursor.getString(3))
                fTime.add(cursor.getString(5))
                fId4.add(cursor.getInt(4))
                fPrice.add(cursor.getString(2))
            }
        }
        adapter.notifyDataSetChanged()
    }
    private fun filterData(query: String) {
        // Clear current lists
        fName.clear()
        rName.clear()
        fRate.clear()
        fTime.clear()
        fId4.clear()
        fPrice.clear()

        // Query the database with the filter criteria
        val cursor: Cursor = db.getSearchFoodHomeData(query) // Modify this method to accept a filter
        if (cursor.count == 0) {
            Toast.makeText(this@allFoodItems, "No results found", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor.moveToNext()) {
                fName.add(cursor.getString(0))
                rName.add(cursor.getString(1))
                fRate.add(cursor.getString(3))
                fTime.add(cursor.getString(5))
                fId4.add(cursor.getInt(4))
                fPrice.add(cursor.getString(2))
            }
        }
        adapter.notifyDataSetChanged()
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