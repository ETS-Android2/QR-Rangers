package com.example.qr_rangers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Fragment to confirm user deletion QR code
 *
 * @author Alexander Salm, Ronan Sandoval
 * @version 1.0
 */
public class DeleteQRConfirmationFragment extends DialogFragment {
    private ScannedCode qr;
    private User user;
    public DeleteQRConfirmationFragment(ScannedCode qr, User user){
        super();
        this.qr = qr;
        this.user = user;
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
        text.setText("Are you sure you want to delete this QR Code? \n(This cannot be undone)");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Delete QR Code")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("Note", qr == null ? "null" : "notnull");
                        user.DeleteQR(qr);
                        Database.Users.update(user);
                        Intent resultUser = new Intent();
                        resultUser.putExtra("user", user);
                        getActivity().setResult(Activity.RESULT_OK, resultUser);
                        getActivity().finish();
                    }
                }).create();

    }
}
