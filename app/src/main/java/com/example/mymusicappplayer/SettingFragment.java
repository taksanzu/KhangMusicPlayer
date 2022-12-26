package com.example.mymusicappplayer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Locale;


public class SettingFragment extends Fragment {
    Button btnChangeLanguage;
    public SettingFragment() {

    }

    public static SettingFragment newInstance(String param1, String param2) {
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
        btnChangeLanguage = view.findViewById(R.id.btnLanguage);

        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });
    }

    private void showChangeLanguageDialog() {
        final  String[] listItems ={"Tiếng Việt","日本","English"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
        builder.setTitle("Choose Language...");
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    //Tiếng Việt
                    setLocale("vi");
                    Relauch();
                }
                else if(i == 1)
                {
                    //Tiếng Nhật
                    setLocale("ja");
                    Relauch();
                }
                else if(i == 2)
                {
                    //Tiếng Anh
                    setLocale("en");
                    Relauch();
                }

                //tắt alert dialog khi đã chọn ngôn ngữ
                dialogInterface.dismiss();

            }
        });
        AlertDialog mDialog = builder.create();
        //Hiện AlterDialog
        mDialog.show();
    }
    private void Relauch() {
        getActivity().recreate();
    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(
                config, getActivity().getBaseContext().getResources().getDisplayMetrics());

    }
}