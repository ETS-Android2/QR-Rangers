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

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ViewUniqueQrFragment extends Fragment {
    private ListView usersList;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayAdapter<User> usersAdapter;
    public ViewUniqueQrFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_unique_qr, null);

        usersList = view.findViewById(R.id.unique_qr_list);

        //users = Database.Users.getAll();

        usersAdapter = new CustomList(getActivity(), users);

        usersList.setAdapter(usersAdapter);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
                profileIntent.putExtra("user", users.get(i));
                startActivity(profileIntent);
            }
        });

        usersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l){
                Log.i("NOTE", "Long press on " + Integer.toString(i));
                DialogFragment deleteUserFragment = new DeleteUserConfirmationFragment(usersAdapter, i);
                deleteUserFragment.show(getActivity().getSupportFragmentManager(), "Delete_User");
                return true;
            }
        });
        return view;
    }
}
