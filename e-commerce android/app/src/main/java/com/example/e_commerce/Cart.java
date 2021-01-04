package com.example.e_commerce;

import android.util.JsonReader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static final List<JSONObject> cart = new ArrayList<JSONObject>(  );
    public static boolean contains(long id) {
        for(JSONObject item : cart) {
            try {
                if(id == item.getLong( "id" )) {
                    return true;
                };
            }catch (Exception e) {

            }
        }

        return false;
    }

    private static JSONObject getItem(long id) {
        try {
            for(JSONObject item : cart) {
                if(id == item.getLong( "id" )) {
                    return item;
                }
            }
        }catch (Exception e) {

        }

        return null;

    }

    private static void remove(long id) {
        if(contains( id )) {
            JSONObject item = getItem( id );
            cart.remove( item );
        }
    }

    private static void increase(long id) {
        try {
            JSONObject item = getItem( id );
            long quantity = item.getLong( "quantity" );
            item.put( "quantity", quantity + 1 );
        }catch (Exception e) {

        }
    }

    private static void decrease(long id) {
        try {
            JSONObject item = getItem( id );
            long quantity = item.getLong( "quantity" );
            item.put( "quantity", quantity - 1 );
            if(quantity == 1) {
                remove( id );
            }
        }catch (Exception e) {

        }
    }

    public static List<JSONObject> getCart() {
        return cart;
    }

    public static void add(JSONObject item) {
        try {
            long id = item.getLong( "id" );
            if(!contains( id )) {
                item.put( "quantity", 0 );
                cart.add( item );
            }
            increase(id);
        } catch (Exception e) {

        }
    }

    public static void remove(JSONObject item) {
        try {
            long id = item.getLong( "id" );
            if(contains( id )) {
                decrease(id);
            }
        } catch (Exception e) {

        }
    }

    private static long getQuantity(long id) {
        try {
            JSONObject item =  getItem( id );
            return item.getLong( "quantity" );
        } catch (Exception e) {
            return 0;
        }
    }

    public static long count(JSONObject item) {
        try {
            long id = item.getLong( "id" );
            return getQuantity(id);
        } catch (Exception e){
        }

        return 0;
    }
}

