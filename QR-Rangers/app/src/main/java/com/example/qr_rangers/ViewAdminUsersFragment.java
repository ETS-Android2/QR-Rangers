package com.example.qr_rangers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * Fragment to display the admin users list.
 *
 * @author Alexander Salm
 * @version 1.0
 */
public class ViewAdminUsersFragment extends Fragment {
    ListView users_list;
    ArrayList<User> users = new ArrayList<>();
    ArrayAdapter<User> users_adapter;
    public ViewAdminUsersFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_admin_users, null);

        users_list = view.findViewById(R.id.admin_users_list);

        users = new DbCollection("users").searchSuggestions("");

        users_adapter = new CustomList(getActivity(), users);

        users_list.setAdapter(users_adapter);

        users_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
                profileIntent.putExtra("user", users.get(i));
                startActivity(profileIntent);
            }
        });

        users_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l){
                Log.i("NOTE", "Long press on " + Integer.toString(i));
                DialogFragment delete_user_fragment = new DeleteUserConfirmationFragment(users_adapter, i);
                delete_user_fragment.show(getActivity().getSupportFragmentManager(), "Delete_User");
                return true;
            }
        });
        return view;
    }
}
