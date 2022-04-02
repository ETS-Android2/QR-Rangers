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
    private String fragment;

    public CustomList(Context context, ArrayList<User> profiles){
        super(context,0, profiles);
        this.profiles = profiles;
        this.context = context;
    }

    public CustomList(Context context, ArrayList<User> profiles, String fragment){
        super(context,0, profiles);
        this.profiles = profiles;
        this.context = context;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        User user = profiles.get(position);

        TextView username = view.findViewById(R.id.user_text);

        if(context.getClass() == LeaderboardActivity.class) {
            String print = (position + 1) + ". " + user.getUsername();
            switch(fragment){
                case("HighScore"):
                    print += "     " + user.getTotalScore() + " pts.";
                    break;
                case("NumCodes"):
                    print += "     " + user.getQRNum();
                    break;
                case("UniqueQr"):
                    print += "     " + user.getMaxScore() + " pts.";
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            username.setText(print);
        }
        else
        {
            username.setText(user.getUsername());
        }

        return view;
    }
}
