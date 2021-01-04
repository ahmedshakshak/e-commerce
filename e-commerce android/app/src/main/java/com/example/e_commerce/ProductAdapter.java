package com.example.e_commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    Context context;
    JSONArray response;
    LayoutInflater inflter;
    boolean enable = true;

    public ProductAdapter(Context applicationContext, JSONArray response) {
        this.context = context;
        this.response = response;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public ProductAdapter(Context applicationContext, JSONArray response, boolean enable ) {
        this.context = context;
        this.response = response;
        inflter = (LayoutInflater.from(applicationContext));
        this.enable = enable;
    }

    @Override
    public int getCount() {
        return response.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return response.getJSONObject( i );
        } catch (Exception e) {
            return  null;
        }
    }

    @Override
    public long getItemId(int i) {
        try {
            return ((JSONObject)getItem( i )).getLong( "id" );
        } catch (Exception e) {
            return  -1;
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {
            view = inflter.inflate(R.layout.product_listview, null);
            JSONObject item = ((JSONObject)getItem( i ));

            TextView textViewProductName = (TextView) view.findViewById(R.id.textView_name);
            textViewProductName.setText( item.getString( "name" ) );

            TextView textViewProductPrice = (TextView) view.findViewById(R.id.textView_price);
            textViewProductPrice.setText( item.getString( "price" ) );

            final TextView textViewProductQuantity = (TextView) view.findViewById(R.id.textView_quantity);
            final List<Long> quantityListy =  new ArrayList<Long>(  );
            final int idx = i;
            quantityListy.add( item.getLong( "quantity" ) );
            textViewProductQuantity.setText( String.valueOf( quantityListy.get(0) ));

            TextView textViewProductCategoryName = (TextView) view.findViewById(R.id.textView_category_name);
            textViewProductCategoryName.setText( item.getString( "category_name" ) );

            final TextView textView_curtrent_quantity = view.findViewById( R.id.textView_current_quantity );
            textView_curtrent_quantity.setText( String.valueOf( Cart.count((JSONObject) response.get( idx ))) );


            Button btnAddToCart = (Button) view.findViewById( R.id.button_add_to_cart );
            btnAddToCart.setEnabled( enable );
            btnAddToCart.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Cart.add( (JSONObject) response.get( idx ) );
                        textView_curtrent_quantity.setText( String.valueOf( Cart.count((JSONObject) response.get( idx ))) );
                    }catch (Exception e) {

                    }

                }
            } );

            Button btnRemoveFromCart = (Button) view.findViewById( R.id.button_remove_from_cart );
            btnRemoveFromCart.setEnabled( enable );
            btnRemoveFromCart.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Cart.remove( (JSONObject) response.get( idx ) );
                        textView_curtrent_quantity.setText( String.valueOf( Cart.count((JSONObject) response.get( idx ))) );
                    }catch (Exception e) {

                    }
                }
            } );


        } catch (Exception e) {

        }

        return view;
    }
}
