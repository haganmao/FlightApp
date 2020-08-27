package com.example.flightapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.flightapp.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME= "FlightAppDB.db";
    private static final int DB_VER = 1;


    public Database(Context context) {
        super(context, DB_NAME,null, DB_VER);
    }

    public List<Order> getCarts(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ID","ProductId","ProductName","Quantity","Price","Discount"};
        String sqlTable = "BookingDetail";

        qb.setTables(sqlTable);
        Cursor cursor = qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                result.add(new Order(
                        cursor.getInt(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("ProductId")),
                        cursor.getString(cursor.getColumnIndex("ProductName")),
                        cursor.getString(cursor.getColumnIndex("Quantity")),
                        cursor.getString(cursor.getColumnIndex("Price")),
                        cursor.getString(cursor.getColumnIndex("Discount"))
                        ));
            }while(cursor.moveToNext());
        }

        return result;
    }

    public void addToCart(Order order)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO BookingDetail(ProductId,ProductName,Quantity,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());

        db.execSQL(query);
    }

    public void cleanCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM BookingDetail");
        db.execSQL(query);
    }

    public void updateCart(Order order) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE BookingDetail SET Quantity = %s WHERE ID = %d",order.getQuantity(),order.getID());
        db.execSQL(query);
    }

    //Add Favorite
    public void addToFav(String airlineId){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(AirlineId) VALUES('%s');",airlineId);
        db.execSQL(query);
    }

    public void removeFromFav(String airlineId){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE AirlineId = '%s';",airlineId);
        db.execSQL(query);
    }


    public boolean isFav(String airlineId){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE AirlineId = '%s';",airlineId);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount() <= 0 )
        {
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }



}
