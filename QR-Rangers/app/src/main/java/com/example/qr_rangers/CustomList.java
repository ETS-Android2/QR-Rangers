package com.example.qr_rangers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Code adapted for the ListViews of Users taken from the Lab CustomList sample code
 * and altered to match the User classes better.
 */

public class CustomList extends ArrayAdapter<User> {

    private ArrayList<User> profiles;
    private Context context;

    public CustomList(Context context, ArrayList<User> profiles){
        super(context,0, profiles);
        this.profiles = profiles;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        User user = profiles.get(position);

        TextView username = view.findViewById(R.id.user_text);
//        TextView provinceName = view.findViewById(R.id.province_text);

        username.setText(user.getUsername());
//        provinceName.setText(city.getProvinceName());

        return view;
    }
}
