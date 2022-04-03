package com.example.qr_rangers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Fragment to represent a qr code.
 *
 * @author Alexander Salm
 * @version 1.0
 */
public class ViewQrCodeFragment extends DialogFragment {
    private Bitmap qrCode;

    public ViewQrCodeFragment(Bitmap qrCode){
        super();
        this.qrCode = qrCode;
    }

    /**
     * Performs actions for when attaching this fragment to an activity
     * @param context - Context to attach to
     */
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_qr_view, null);

        ImageView image = view.findViewById(R.id.qr_view_image);
        image.setImageBitmap(qrCode);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Shareable QR Code")
                .setNegativeButton("Close", null)
                .create();

    }
}