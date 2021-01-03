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

    public ProductAdapter(Context applicationContext, JSONArray response) {
        this.context = context;
        this.response = response;
        inflter = (LayoutInflater.from(applicationContext));
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
            quantityListy.add( item.getLong( "quantity" ) );
            textViewProductQuantity.setText( String.valueOf( quantityListy.get(0) ));

            TextView textViewProductCategoryName = (TextView) view.findViewById(R.id.textView_category_name);
            textViewProductCategoryName.setText( item.getString( "category_name" ) );

            Button btnAddToCart = (Button) view.findViewById( R.id.button_add_to_cart );
            btnAddToCart.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long quantity = quantityListy.get( 0 );
                    if(quantity == 0) {
                        return;
                    }
                    quantityListy.clear();
                    quantity--;
                    quantityListy.add( quantity );
                    textViewProductQuantity.setText( String.valueOf( quantityListy.get(0) ));

                }
            } );

        } catch (Exception e) {

        }

        return view;
    }
}
