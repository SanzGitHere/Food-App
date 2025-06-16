package com.example.ass4
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class MenuViewModel(private val db: foodSQLHelper) : ViewModel() {

    private val _foodData = MutableLiveData<List<FoodItem>>()
    val foodData: LiveData<List<FoodItem>> get() = _foodData

    // Load data using coroutines
    fun loadFoodData() {
        GlobalScope.launch(Dispatchers.IO) {
            val foodList = mutableListOf<FoodItem>()
            val cursor = db.getFoodHomeData()
            if (cursor.count != 0) {
                while (cursor.moveToNext()) {
                    foodList.add(
                        FoodItem(
                            name = cursor.getString(0),
                            restaurant = cursor.getString(1),
                            rate = cursor.getString(2),
                            time = cursor.getString(3),
                            id = cursor.getInt(4)
                        )
                    )
                }
            }
            withContext(Dispatchers.Main) {
                _foodData.value = foodList
            }
        }
    }
}

data class FoodItem(val name: String, val restaurant: String, val rate: String, val time: String, val id: Int)
