package com.example.ass4

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date


class foodSQLHelper(context: Context) : SQLiteOpenHelper(context, dbName, null, dbVersion)  {
    companion object {
        const val dbName = "foodDeliveryDB"
        const val dbVersion = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(" CREATE TABLE Customer ( customer_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT, phone TEXT);")

        db.execSQL("CREATE TABLE CustomerAddress (customer_id INTEGER, city TEXT, district TEXT , streetNo TEXT, streetName TEXT, lat REAL, long REAL, address TEXT, PRIMARY KEY (customer_id), FOREIGN KEY (customer_id) REFERENCES Customer(customer_id))")

        db.execSQL("CREATE TABLE Admin ( admin_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT );")

        db.execSQL(" CREATE TABLE Restaurant ( restaurant_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT, rating REAL ,priceCat TEXT, des TEXT, email TEXT,time REAL);")

        db.execSQL(" CREATE TABLE Branch ( branch_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT, rating REAL, email TEXT,restaurant_id INTEGER,FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) );")

        db.execSQL("CREATE TABLE BranchAddress (branch_id INTEGER, city TEXT, district TEXT , streetNo TEXT, streetName TEXT, lat REAL, long REAL, address TEXT, PRIMARY KEY (branch_id), FOREIGN KEY (branch_id) REFERENCES Branch(branch_id))")

        db.execSQL("CREATE TABLE FoodDish ( dish_id INTEGER PRIMARY KEY AUTOINCREMENT, restaurant_id INTEGER, name TEXT, description TEXT, price REAL, time REAL, rate REAL, energy INTEGER, des TEXT, cate TEXT,FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) );")

        db.execSQL("CREATE TABLE Orders ( order_id INTEGER PRIMARY KEY AUTOINCREMENT, customer_id INTEGER, restaurant_id INTEGER, cart_id INTEGER, order_date TEXT, total_amount REAL, status TEXT, FOREIGN KEY (customer_id) REFERENCES Customer(customer_id), FOREIGN KEY (cart_id) REFERENCES Cart(cart_id), FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) );")

        db.execSQL("CREATE TABLE Cart ( cart_id INTEGER PRIMARY KEY AUTOINCREMENT, customer_id INTEGER, FOREIGN KEY (customer_id) REFERENCES Customer(customer_id));")

        // CartFoodDish table (Many-to-Many relationship between Cart and FoodDish)
        db.execSQL("CREATE TABLE CartFoodDish (cf_id INTEGER PRIMARY KEY AUTOINCREMENT,cart_id INTEGER, dish_id INTEGER, size TEXT, topping TEXT, unitPrice Real, quantity INTEGER NOT NULL, FOREIGN KEY (cart_id) REFERENCES Cart(cart_id), FOREIGN KEY (dish_id) REFERENCES FoodDish(dish_id) );")

        db.execSQL("CREATE TABLE Payment ( payment_id INTEGER PRIMARY KEY AUTOINCREMENT, order_id INTEGER, payment_method TEXT, payment_status TEXT, payment_amount REAL, FOREIGN KEY (order_id) REFERENCES Orders(order_id) );")

        db.execSQL("CREATE TABLE CardDetails ( card_id INTEGER PRIMARY KEY AUTOINCREMENT, customer_id INTEGER, card_number TEXT, card_expiry_date TEXT, card_cvv TEXT, FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) );")

        db.execSQL(" CREATE TABLE FoodRatings ( rating_id INTEGER PRIMARY KEY AUTOINCREMENT, customer_id INTEGER, dish_id INTEGER, restaurant_id INTEGER, rating REAL, review TEXT, FOREIGN KEY (customer_id) REFERENCES Customer(customer_id), FOREIGN KEY (dish_id) REFERENCES FoodDish(dish_id), FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) );")

        db.execSQL(" CREATE TABLE Delivery ( delivery_id INTEGER PRIMARY KEY AUTOINCREMENT, order_id INTEGER, delivery_address TEXT, delivery_time TEXT, delivery_status TEXT, FOREIGN KEY (order_id) REFERENCES Orders(order_id)); ")

        db.execSQL(" CREATE TABLE Coupons ( coupon_id INTEGER PRIMARY KEY AUTOINCREMENT,code TEXT UNIQUE NOT NULL, discount_amount REAL NOT NULL, discount_type TEXT CHECK(discount_type IN ('Percentage', 'Fixed')) NOT NULL,min_purchase_amount REAL DEFAULT 0, valid_from TEXT, valid_to TEXT, usage_limit INTEGER DEFAULT 1, usage_count INTEGER DEFAULT 0); ")

        db.execSQL(" CREATE TABLE RestaurantCoupons ( restaurant_coupon_id INTEGER PRIMARY KEY AUTOINCREMENT, restaurant_id INTEGER, coupon_id INTEGER, FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id), FOREIGN KEY (coupon_id) REFERENCES Coupons(coupon_id)); ")

        db.execSQL(" CREATE TABLE CustomerCoupons (customer_coupon_id INTEGER PRIMARY KEY AUTOINCREMENT, customer_id INTEGER, coupon_id INTEGER, claimed_date TEXT, FOREIGN KEY (customer_id) REFERENCES Customer(customer_id), FOREIGN KEY (coupon_id) REFERENCES Coupons(coupon_id)); ")

            db.execSQL("INSERT INTO Admin (username, password) VALUES ('admin', 'adminpassword');")

            // Inserting Restaurant data
            db.execSQL("INSERT INTO Restaurant (name, phone, rating, priceCat, des, email, time) VALUES ('Gourmet Pizza Place', '555-1111', 4.7, '$$$', 'Known for artisan pizzas', 'contact@gourmetpizza.com', 25.0);")
            db.execSQL("INSERT INTO Restaurant (name, phone, rating, priceCat, des, email, time) VALUES ('Sushi Palace', '555-2222', 4.8, '$$$', 'Premium sushi experience', 'contact@sushipalace.com', 35.0);")
            db.execSQL("INSERT INTO Restaurant (name, phone, rating, priceCat, des, email, time) VALUES ('Burger Shack', '555-3333', 4.5, '$$', 'Best burgers in town', 'contact@burgershack.com', 20.0);")

            // Inserting Branch data
            db.execSQL("INSERT INTO Branch (name, phone, rating, email, restaurant_id) VALUES ('Main Branch', '555-4444', 4.6, 'main@gourmetpizza.com', 1);")
            db.execSQL("INSERT INTO Branch (name, phone, rating, email, restaurant_id) VALUES ('Uptown Branch', '555-5555', 4.2, 'uptown@sushipalace.com', 2);")
            db.execSQL("INSERT INTO Branch (name, phone, rating, email, restaurant_id) VALUES ('Downtown Branch', '555-6666', 4.3, 'downtown@burgershack.com', 3);")

            // Inserting FoodDish data
            db.execSQL("INSERT INTO FoodDish (restaurant_id, name, description, price, time, rate, energy, des, cate) VALUES (1, 'Margherita Pizza', 'Classic pizza with mozzarella and tomato', 12.99, 25.0, 4.9, 800, 'Freshly baked', 'Pizza');")
            db.execSQL("INSERT INTO FoodDish (restaurant_id, name, description, price, time, rate, energy, des, cate) VALUES (1, 'Pepperoni Pizza', 'Spicy pepperoni with cheese', 14.99, 20.0, 4.7, 900, 'Customer favorite', 'Pizza');")
            db.execSQL("INSERT INTO FoodDish (restaurant_id, name, description, price, time, rate, energy, des, cate) VALUES (2, 'Salmon Sashimi', 'Fresh salmon slices', 18.99, 15.0, 4.8, 350, 'Premium quality', 'Sushi');")
            db.execSQL("INSERT INTO FoodDish (restaurant_id, name, description, price, time, rate, energy, des, cate) VALUES (2, 'Tuna Roll', 'Tuna roll with avocado', 12.99, 10.0, 4.6, 450, 'A light option', 'Sushi');")
            db.execSQL("INSERT INTO FoodDish (restaurant_id, name, description, price, time, rate, energy, des, cate) VALUES (3, 'Cheeseburger', 'Beef patty with cheese', 9.99, 15.0, 4.5, 700, 'Juicy and delicious', 'Burger');")
            db.execSQL("INSERT INTO FoodDish (restaurant_id, name, description, price, time, rate, energy, des, cate) VALUES (3, 'Veggie Burger', 'Grilled vegetable patty', 8.99, 15.0, 4.3, 600, 'Healthy choice', 'Burger');")

            // Inserting Customer data
            db.execSQL("INSERT INTO Customer (name, email, password, phone) VALUES ('John', 'alice@example.com', 'password123', '555-7777');")
            db.execSQL("INSERT INTO Customer (name, email, password, phone) VALUES ('Bob Johnson', 'bob@example.com', 'password456', '555-8888');")

            // Inserting CustomerAddress data
            db.execSQL("INSERT INTO CustomerAddress (customer_id, city, district, streetNo, streetName, lat, long, address) VALUES (1, 'New York', 'Manhattan', '123', 'Broadway', 40.7128, -74.0060, '123 Broadway, Manhattan, New York');")
            db.execSQL("INSERT INTO CustomerAddress (customer_id, city, district, streetNo, streetName, lat, long, address) VALUES (2, 'Los Angeles', 'Beverly Hills', '456', 'Rodeo Drive', 34.0736, -118.4004, '456 Rodeo Drive, Beverly Hills, Los Angeles');")

            // Inserting Cart data
            db.execSQL("INSERT INTO Cart (customer_id) VALUES (1);")
            db.execSQL("INSERT INTO Cart (customer_id) VALUES (2);")

            // Inserting CartFoodDish data (Many-to-Many relationship between Cart and FoodDish)
            db.execSQL("INSERT INTO CartFoodDish (cart_id, dish_id, size, topping, unitPrice, quantity) VALUES (1, 1, 'Medium', 'Extra Cheese', 12.99, 1);")
            db.execSQL("INSERT INTO CartFoodDish (cart_id, dish_id, size, topping, unitPrice, quantity) VALUES (1, 3, 'Large', 'None', 18.99, 2);")
            db.execSQL("INSERT INTO CartFoodDish (cart_id, dish_id, size, topping, unitPrice, quantity) VALUES (2, 6, 'Small', 'None', 8.99, 1);")

            // Inserting Orders data
            db.execSQL("INSERT INTO Orders (customer_id, restaurant_id, cart_id, order_date, total_amount, status) VALUES (1, 1, 1, '2024-08-14', 41.97, 'Pending');")
            db.execSQL("INSERT INTO Orders (customer_id, restaurant_id, cart_id, order_date, total_amount, status) VALUES (2, 3, 2, '2024-08-14', 8.99, 'Pending');")

            // Inserting Payment data
            db.execSQL("INSERT INTO Payment (order_id, payment_method, payment_status, payment_amount) VALUES (1, 'Credit Card', 'Paid', 41.97);")
            db.execSQL("INSERT INTO Payment (order_id, payment_method, payment_status, payment_amount) VALUES (2, 'Cash on Delivery', 'Pending', 8.99);")

            // Inserting CardDetails data
            db.execSQL("INSERT INTO CardDetails (customer_id, card_number, card_expiry_date, card_cvv) VALUES (1, '4111111111111111', '12/25', '123');")
            db.execSQL("INSERT INTO CardDetails (customer_id, card_number, card_expiry_date, card_cvv) VALUES (2, '4222222222222222', '01/26', '456');")

            // Inserting FoodRatings data
            db.execSQL("INSERT INTO FoodRatings (customer_id, dish_id, restaurant_id, rating, review) VALUES (1, 1, 1, 5.0, 'Absolutely loved it!');")
            db.execSQL("INSERT INTO FoodRatings (customer_id, dish_id, restaurant_id, rating, review) VALUES (2, 6, 3, 4.0, 'Tasty, but could be improved.');")

            // Inserting Delivery data
            db.execSQL("INSERT INTO Delivery (order_id, delivery_address, delivery_time, delivery_status) VALUES (1, '123 Broadway, Manhattan, New York', '2024-08-14 14:30', 'Delivered');")
            db.execSQL("INSERT INTO Delivery (order_id, delivery_address, delivery_time, delivery_status) VALUES (2, '456 Rodeo Drive, Beverly Hills, Los Angeles', '2024-08-14 16:00', 'Out for Delivery');")

            // Inserting Coupons data
            db.execSQL("INSERT INTO Coupons (code, discount_amount, discount_type, min_purchase_amount, valid_from, valid_to, usage_limit, usage_count) VALUES ('SUMMER20', 20.0, 'Percentage', 50.0, '2024-06-01', '2024-09-01', 100, 0);")
            db.execSQL("INSERT INTO Coupons (code, discount_amount, discount_type, min_purchase_amount, valid_from, valid_to, usage_limit, usage_count) VALUES ('FIXED5OFF', 5.0, 'Fixed', 30.0, '2024-01-01', '2024-12-31', 50, 10);")

            // Inserting RestaurantCoupons data
            db.execSQL("INSERT INTO RestaurantCoupons (restaurant_id, coupon_id) VALUES (1, 1);")
            db.execSQL("INSERT INTO RestaurantCoupons (restaurant_id, coupon_id) VALUES (2, 2);")

            // Inserting CustomerFavorites data
           // db.execSQL("INSERT INTO CustomerFavorites (customer_id, dish_id) VALUES (1, 1);")
           // db.execSQL("INSERT INTO CustomerFavorites (customer_id, dish_id) VALUES (2, 6);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop all tables
        db.execSQL("DROP TABLE IF EXISTS Customer")
        db.execSQL("DROP TABLE IF EXISTS Admin")
        db.execSQL("DROP TABLE IF EXISTS Restaurant")
        db.execSQL("DROP TABLE IF EXISTS Branch")
        db.execSQL("DROP TABLE IF EXISTS FoodDish")
        db.execSQL("DROP TABLE IF EXISTS Orders")
        db.execSQL("DROP TABLE IF EXISTS Cart")
        db.execSQL("DROP TABLE IF EXISTS CartFoodDish")
        db.execSQL("DROP TABLE IF EXISTS Payment")
        db.execSQL("DROP TABLE IF EXISTS CardDetails")
        db.execSQL("DROP TABLE IF EXISTS FoodRatings")
        db.execSQL("DROP TABLE IF EXISTS Delivery")
        db.execSQL("DROP TABLE IF EXISTS Coupons")
        db.execSQL("DROP TABLE IF EXISTS RestaurantCoupons")
        db.execSQL("DROP TABLE IF EXISTS CustomerCoupons")

        // Recreate tables
        onCreate(db)
    }

    fun getFoodHomeData(): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT fd.name, re.name, fd.price, fd.time, fd.dish_id ,fd.rate FROM FoodDish fd JOIN Restaurant re ON fd.restaurant_id = re.restaurant_id", null)
        return cursor
    }

    fun getFoodCateHomeData(query: String): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        val filter = "%$query%"
        val cursor: Cursor = db.rawQuery("SELECT fd.name, re.name, fd.price, fd.time, fd.dish_id ,fd.rate FROM FoodDish fd JOIN Restaurant re ON fd.restaurant_id = re.restaurant_id WHERE fd.cate = ?", arrayOf(query))
        return cursor
    }

    fun getSearchFoodHomeData(query: String): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        val filter = "%$query%"
        val cursor: Cursor = db.rawQuery("SELECT fd.name, re.name, fd.price, fd.time, fd.dish_id ,fd.rate FROM  FoodDish fd JOIN Restaurant re ON fd.restaurant_id = re.restaurant_id WHERE fd.name LIKE ?", arrayOf(filter))
        return cursor
    }


    fun getRestHomeData(): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT re.name, br.rating, br.name, br.branch_id FROM Restaurant re JOIN Branch br ON re.restaurant_id = br.restaurant_id", null)
        return cursor
    }

    fun getSearchRestHomeData(query: String): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        val filter = "%$query%"
        val cursor: Cursor = db.rawQuery("SELECT re.name, br.rating, br.name, br.branch_id FROM Restaurant re JOIN Branch br ON re.restaurant_id = br.restaurant_id WHERE re.name LIKE ?", arrayOf(filter))
        return cursor
    }

    fun getRestId(cartId: Int): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT fd.restaurant_id FROM FoodDish fd JOIN CartFoodDish cfd ON fd.dish_id = cfd.dish_id WHERE cfd.cart_id = ? LIMIT 1", arrayOf(cartId.toString()))
        return cursor
    }

    fun getRestFoodData(restId: Int): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT name, time, rate, price, dish_id FROM FoodDish WHERE restaurant_id = ?", arrayOf(restId.toString()))
        return cursor
    }


    fun getFoodCartData(cartId: Int): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT fd.name, cfd.unitPrice, re.name, cfd.quantity, cfd.dish_id ,cfd.cart_id, cfd.size, cfd.topping " +
                "FROM CartFoodDish cfd " +
                "JOIN FoodDish fd ON cfd.dish_id = fd.dish_id " +
                "JOIN Restaurant re ON fd.restaurant_id = re.restaurant_id " +
                "WHERE cfd.cart_id = ?", arrayOf(cartId.toString()))
        return cursor
    }

    fun getCartId(user: String): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        val cursor: Cursor = db.rawQuery( "SELECT ca.cart_id, cu.customer_id FROM cart AS ca JOIN customer AS cu ON ca.customer_id = cu.customer_id WHERE cu.name = ? ORDER BY ca.cart_id DESC LIMIT 1",arrayOf(user))
        return cursor
    }

    fun getOrderData(user: Int): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        val cursor: Cursor = db.rawQuery( "SELECT order_id, order_date, total_amount, status FROM Orders WHERE customer_id = ? ", arrayOf(user.toString()))
        return cursor
    }

    fun getCouponData(user: Double): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        val currentDate: Date = Date()
        val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateString: String = dateFormat.format(currentDate)
        val cursor: Cursor = db.rawQuery( "SELECT code, discount_amount , discount_type ,min_purchase_amount , valid_from , valid_to , usage_limit , usage_count FROM Coupons WHERE min_purchase_amount < ? AND usage_limit >= usage_count ", arrayOf(user.toString()))
        return cursor
    }

    fun updateCartItemQuantity(cartId: Int, foodDishId: Int, quantity: Int) {
        val db: SQLiteDatabase = this.writableDatabase
        db.execSQL(
            "UPDATE CartFoodDish SET quantity = ? WHERE cart_id = ? AND dish_id = ?",
            arrayOf(quantity.toString(), cartId.toString(), foodDishId.toString())
        )
    }

    fun deleteCartItem(cartId: Int, foodDishId: Int) {
        val db: SQLiteDatabase = this.writableDatabase
        db.delete(
            "CartFoodDish",
            "cart_id = ? AND dish_id = ?",
            arrayOf(cartId.toString(), foodDishId.toString())
        )
    }


    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON;")
    }
}