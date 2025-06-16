package com.example.ass4

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RestDetails : AppCompatActivity() {

    lateinit var dbb: SQLiteDatabase

    lateinit var reName2: TextView
    lateinit var reCat2: TextView
    lateinit var reBra2: TextView
    lateinit var reTime2: TextView
    lateinit var reFoodCou2: TextView
    lateinit var reStar2: TextView
    lateinit var reDes2: TextView

    lateinit var recyclerView: RecyclerView
    lateinit var fId: ArrayList<Int>
    lateinit var fName: ArrayList<String>
    lateinit var fTime: ArrayList<String>
    lateinit var fRate: ArrayList<String>
    lateinit var fPrice: ArrayList<String>
    lateinit var db: foodSQLHelper
    lateinit var adapter: restFoodAdapter

    var username = ""
    var cartId: Int = 0
    var userId: Int = 0
    private fun displayData() {
        val intent2: Intent = intent
        val restId: Int = intent2.getIntExtra("restId", -1)
        val cartId: Int = intent2.getIntExtra("cartId", -1)
        userId = intent2.getIntExtra("userId", -1)
        username = intent2.getStringExtra("username").toString()
        val cursor: Cursor = db.getRestFoodData(restId)
        if (cursor.count == 0) {
            Toast.makeText(this@RestDetails,"No entry exists", Toast.LENGTH_SHORT).show()
            return
        } else {
            while (cursor.moveToNext())
            {
                fName.add(cursor.getString(0))
                fTime.add(cursor.getString(1))
                fRate.add(cursor.getString(2))
                fPrice.add(cursor.getString(3))
                fId.add(cursor.getInt(4))
            }
        }
        adapter.notifyDataSetChanged()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_details)

        reName2 = findViewById(R.id.txt_re_name2)
        reCat2 = findViewById(R.id.txt_re_cat2)
        reBra2 = findViewById(R.id.lbl_rest_bra)
        reTime2 = findViewById(R.id.txt_fominRest)
        reFoodCou2 = findViewById(R.id.txt_fCountRest)
        reStar2 = findViewById(R.id.txt_starRest)
        reDes2 = findViewById(R.id.lbl_fRest)

        val intent2: Intent = intent
        val restId: Int = intent2.getIntExtra("restId", -1)
        username = intent2.getStringExtra("username").toString()
        userId = intent2.getIntExtra("userId", -1)

        cartId = intent2.getIntExtra("cartId", -1)

        val sqHelp: foodSQLHelper = foodSQLHelper(this)
        dbb = sqHelp.readableDatabase

        if (restId != -1) {
            val cursor: Cursor = dbb.rawQuery(
                "SELECT re.name, re.priceCat, br.name, re.time, COUNT(re.name), re.rating, re.des FROM Restaurant re JOIN Branch br ON re.restaurant_id = br.restaurant_id WHERE re.restaurant_id = ?", arrayOf(restId.toString())
            )

            if (cursor.moveToFirst()) {
                reName2.text = cursor.getString(0)
                reCat2.text = cursor.getString(1)
                reBra2.text = cursor.getString(2)
                reTime2.text = cursor.getString(3)
                reFoodCou2.text = cursor.getString(4)
                reStar2.text = cursor.getString(5)
                reDes2.text = cursor.getString(6)

            } else {
                Toast.makeText(this, "No restaurant details found for the provided ID.", Toast.LENGTH_LONG).show()
            }
            cursor.close()
        } else {
            Toast.makeText(this, "Invalid Rest ID.", Toast.LENGTH_LONG).show()
        }

        db = foodSQLHelper(this)
        fName = ArrayList()
        fTime = ArrayList()
        fRate = ArrayList()
        fPrice = ArrayList()
        fId = ArrayList()

        recyclerView = findViewById(R.id.rec_foodsrest)
        adapter = restFoodAdapter(this, fName, fTime, fRate, fPrice, fId, userId, username, cartId)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Add space between the items in the RecyclerView
        val spaceBetweenItems = (16 * resources.displayMetrics.density).toInt() // 16dp to pixels
        recyclerView.addItemDecoration(RestDetails.VerticalSpaceItemDecoration(spaceBetweenItems))

        displayData()
    }
    class VerticalSpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: android.graphics.Rect, view: android.view.View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.bottom = space
        }
    }
}