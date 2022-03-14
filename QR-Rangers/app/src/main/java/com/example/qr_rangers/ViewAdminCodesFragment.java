package com.example.qr_rangers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Fragment that display's a list of all QR codes registered
 * @author Alexander Salm
 * @version 1.0
 */
public class ViewAdminCodesFragment extends Fragment {
    GridView codeGrid;
    ArrayList<QRCode> codes;
    ArrayAdapter<QRCode> codesAdapter;
    public ViewAdminCodesFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_admin_codes, null);

        codeGrid = view.findViewById(R.id.admin_qr_list_grid);

        HashMap<QRCode, User> map = new HashMap<QRCode, User>();

        codes = new ArrayList<QRCode>();
        ArrayList<User> users = new DbCollection("users").searchSuggestions("");
        for (int i = 0; i < users.size(); i++) {
            ArrayList<QRCode> usersCodes = users.get(i).getQRList();
            for (int x = 0; x < usersCodes.size(); x++) {
                codes.add(usersCodes.get(x));
                map.put(usersCodes.get(x), users.get(i));
            }
        }

        codesAdapter = new QRListAdapter(getActivity(), codes);
        codeGrid.setAdapter(codesAdapter);

        ActivityResultLauncher<Intent> infoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        /*if (result.getData() != null) {
                            user = (User) result.getData().getSerializableExtra("user");
                            qrListAdapter = new QRListAdapter(QRListActivity.this, user.getQRList());
                            qrGrid.setAdapter(qrListAdapter);
                            qrListAdapter.notifyDataSetChanged();
                        }*/

                    }
                });

        codeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), QRInfoActivity.class);
                intent.putExtra("qr", codes.get(i));
                intent.putExtra("user", map.get(codesAdapter.getItem(i)));
                infoLauncher.launch(intent);
            }
        });

        /*codeGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l){
                Log.i("NOTE", "Long press on " + Integer.toString(i));
                DialogFragment deleteCodeFragment = new DeleteCodeConfirmationFragment(codesAdapter, i);
                deleteCodefragment.show(getActivity().getSupportFragmentManager(), "Delete_Code");
                return true;
            }
        });*/
        return view;
    }
}
