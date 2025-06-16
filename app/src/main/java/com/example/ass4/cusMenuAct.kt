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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class cusMenuAct : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var fName: ArrayList<String>
    lateinit var rName: ArrayList<String>
    lateinit var fRate: ArrayList<String>
    lateinit var fTime: ArrayList<String>
    lateinit var fPrice: ArrayList<String>
    lateinit var fId4: ArrayList<Int>
    lateinit var recyclerView2: RecyclerView
    lateinit var rName2: ArrayList<String>
    lateinit var rRate2: ArrayList<String>
    lateinit var rBra2: ArrayList<String>
    lateinit var rId2: ArrayList<Int>
    lateinit var db: foodSQLHelper
    lateinit var adapter: foodAdapter
    lateinit var adapter2: restAdapter

    lateinit var username: String
    var cartId: Int = 1
    var userId: Int = 1


    val user = "john";

    private fun fCateg()
    {
        val button1: ConstraintLayout = findViewById(R.id.fRice)
        val button2: ConstraintLayout = findViewById(R.id.kottu)
        val button3: ConstraintLayout = findViewById(R.id.nood)
        val button4: ConstraintLayout = findViewById(R.id.biri)
        val button5: ConstraintLayout = findViewById(R.id.burger)
        val button6: ConstraintLayout = findViewById(R.id.pizza)
        val button7: ConstraintLayout = findViewById(R.id.drinks)
        val button8: ConstraintLayout = findViewById(R.id.more)


        button1.setOnClickListener { openNewForm("Fried Rice") }
        button2.setOnClickListener { openNewForm("Kottu") }
        button3.setOnClickListener { openNewForm("Noodles") }
        button4.setOnClickListener { openNewForm("Biryani") }
        button5.setOnClickListener { openNewForm("Burger") }
        button6.setOnClickListener { openNewForm("Pizza") }
        button7.setOnClickListener { openNewForm("Drink") }
        button8.setOnClickListener { openNewForm("More") }

    }

    private fun openNewForm(buttonName: String) {
        val intent = Intent(this, allFoodItems::class.java)
        intent.putExtra("username", user)
        intent.putExtra("userId", userId)
        intent.putExtra("cartId", cartId)
        intent.putExtra("fCate", buttonName)
        startActivity(intent)
    }
    private fun findCart()
    {
        val cursor: Cursor = db.getCartId(user)
        if (cursor.count == 0) {
            Toast.makeText(this@cusMenuAct,"No entry exists",Toast.LENGTH_SHORT).show()
            return
        } else {
            while (cursor.moveToNext())
            {
                cartId = cursor.getInt(0)
                userId = cursor.getInt(1)
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun displayData1() {
        val cursor: Cursor = db.getFoodHomeData()
        if (cursor.count == 0) {
            Toast.makeText(this@cusMenuAct,"No entry exists",Toast.LENGTH_SHORT).show()
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

    private fun displayData2() {
        val cursor: Cursor = db.getRestHomeData()
        if (cursor.count == 0) {
            Toast.makeText(this@cusMenuAct,"No entry exists",Toast.LENGTH_SHORT).show()
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
        setContentView(R.layout.activity_cus_menu)

        val intent2: Intent = intent
        username = intent2.getStringExtra("username").toString()
        username = "john"
        var userr: TextView = findViewById(R.id.lbl_username)
        userr.text = username
        val editTextSearch: TextView = findViewById(R.id.txt_search)


        db = foodSQLHelper(this)
        fName = ArrayList()
        rName = ArrayList()
        fRate = ArrayList()
        fTime = ArrayList()
        fId4 = ArrayList()
        fPrice = ArrayList()

        findCart()

        recyclerView = findViewById(R.id.view_food)
        adapter = foodAdapter(this, fName, rName, fRate, fTime, fId4, fPrice, userId, username, cartId)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Add space between the items in the RecyclerView
        val spaceBetweenItems = (16 * resources.displayMetrics.density).toInt() // 16dp to pixels
        recyclerView.addItemDecoration(HorizontalSpaceItemDecoration(spaceBetweenItems))


        rName2 = ArrayList()
        rRate2 = ArrayList()
        rBra2 = ArrayList()
        rId2 = ArrayList()

        recyclerView2 = findViewById(R.id.view_rest)
        adapter2 = restAdapter(this, rName2, rRate2, rBra2, rId2, userId, username, cartId)

        recyclerView2.adapter = adapter2
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView2.addItemDecoration(HorizontalSpaceItemDecoration(spaceBetweenItems))

        // editTextSearch = findViewById(R.id.editTextSearch)
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter data based on the text entered
                filterData2(s.toString())
                filterData(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed here

            }
        })
        displayData2()
        displayData1()
        fCateg()
    }

    private fun filterData2(query: String)
    {   // Clear current lists
        rName2.clear()
        rRate2.clear()
        rBra2.clear()
        rId2.clear()

        // Query the database with the filter criteria
        val cursorr: Cursor = db.getSearchRestHomeData(query) // Modify this method to accept a filter
        if (cursorr.count == 0) {
            Toast.makeText(this@cusMenuAct, "No results found", Toast.LENGTH_SHORT).show()
        } else {
            while (cursorr.moveToNext()) {
                rName2.add(cursorr.getString(0))
                rRate2.add(cursorr.getString(1))
                rBra2.add(cursorr.getString(2))
                rId2.add(cursorr.getInt(3))
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun filterData(query: String) {

        fName.clear()
        rName.clear()
        fRate.clear()
        fTime.clear()
        fId4.clear()
        fPrice.clear()

        // Query the database with the filter criteria
        val cursor: Cursor = db.getSearchFoodHomeData(query) // Modify this method to accept a filter
        if (cursor.count == 0) {
            Toast.makeText(this@cusMenuAct, "No results found", Toast.LENGTH_SHORT).show()
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

    class HorizontalSpaceItemDecoration(val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: android.graphics.Rect, view: android.view.View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.right = space
        }
    }

    public fun onClickMoreRest(view: View)
    {
        val intent = Intent(this, allRestItems::class.java)
        intent.putExtra("username", user)
        intent.putExtra("cartId", cartId)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }

    public fun onClickOrderDet(view: View)
    {
        val intent = Intent(this, customerOrder::class.java)
        intent.putExtra("username", user)
        intent.putExtra("cartId", cartId)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }

    public fun onClickMoreFood(view: View)
    {
        val intent = Intent(this, allFoodItems::class.java)
        intent.putExtra("username", user)
        intent.putExtra("userId", userId)
        intent.putExtra("cartId", cartId)
        intent.putExtra("fCate", "Null")
        startActivity(intent)
    }

    public fun onClickCart(view: View)
    {
        val intent = Intent(this, cus_my_cart::class.java)
        intent.putExtra("username", user)
        intent.putExtra("userId", userId)
        intent.putExtra("cartId", cartId)
        startActivity(intent)
    }
}