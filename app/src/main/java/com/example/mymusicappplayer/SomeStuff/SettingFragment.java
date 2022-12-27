package com.example.mymusicappplayer.SomeStuff;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.mymusicappplayer.R;


public class SettingFragment extends Fragment {
    RadioButton rbtnDark, rbtnLight;
    public SettingFragment() {
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rbtnLight = view.findViewById(R.id.btnLight);
        rbtnDark = view.findViewById(R.id.btnDark);
        int nightModeFlags =  view.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                rbtnDark.setChecked(true);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                rbtnLight.setChecked(true);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                rbtnLight.setChecked(true);
                break;
        }

        rbtnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                rbtnDark.setChecked(false);
                rbtnLight.setChecked(true);
            }
        });
        rbtnDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                rbtnDark.setChecked(true);
                rbtnLight.setChecked(false);
            }
        });

    }
}