package com.example.qr_rangers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * Fragment to confirm user deletion in the admin pane
 *
 * @author Alexander Salm
 * @version 1.0
 */
public class DeleteUserConfirmationFragment extends DialogFragment {
    private ArrayAdapter<User> users;
    private int index;
    public DeleteUserConfirmationFragment(ArrayAdapter<User> users, int i){
        super();
        this.users = users;
        this.index = i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_delete_user, null);

        TextView text = view.findViewById(R.id.delete_user_confirmation);
        text.setText("Are you sure you want to delete user \"" + users.getItem(index).getUsername() + "\"? (This cannot be undone)");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Delete User")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DbCollection users_collection = new DbCollection("users");
                        users_collection.delete(users.getItem(index).getId());
                        users.remove(users.getItem(index));


                    }
                }).create();

    }
}
