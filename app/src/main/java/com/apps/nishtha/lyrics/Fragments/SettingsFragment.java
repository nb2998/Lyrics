package com.apps.nishtha.lyrics.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.apps.nishtha.lyrics.Service.MyService;
import com.apps.nishtha.lyrics.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    SwitchCompat switchCompat;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Intent serviceIntent;

    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_settings, container, false);
        switchCompat= (SwitchCompat) view.findViewById(R.id.enableSwitch);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
            editor = sharedPreferences.edit();

            serviceIntent = new Intent(getContext(), MyService.class);
            if (sharedPreferences.getBoolean("switchIsChecked", true)) {
                switchCompat.setChecked(true);
                getContext().startService(serviceIntent);
            } else {
                switchCompat.setChecked(false);
            }

            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (switchCompat.isChecked()) {
                        editor.putBoolean("switchIsChecked", true);
                        editor.apply();
                        getContext().startService(serviceIntent);
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                .setTitle("Are you sure you want to disable the service")
                                .setMessage("By doing so, you would stop receiving notifications to know the lyrics while listening to songs.")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor.putBoolean("switchIsChecked", false);
                                        editor.apply();
                                        getContext().stopService(serviceIntent);
                                        Toast.makeText(getContext(), "Service disabled! Please enable the services to enjoy the lyrics while listening to music!", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switchCompat.setChecked(true);
                                        editor.putBoolean("switchIsChecked", true);
                                        editor.apply();
                                        getContext().startService(serviceIntent);
                                        dialog.dismiss();
                                    }
                                })
                                .setCancelable(false)
                                .show();

                    }
                }
            });
        }catch (Exception e){

        }
    }

}
