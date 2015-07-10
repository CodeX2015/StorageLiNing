package com.vipcartlining.vipcardlining;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;
import com.rey.material.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Eugene on 07.07.2015.
 */

public class ActivitySettings extends Activity {
    public static final String APP_PREFERENCES = "LiNingPrefs";
    public static final String OPEN_SETTINGS = "OPEN_SETTINGS";
    public static final String API_ADDRESS = "address of api";
    public static final String USER_LOGIN = "Login";
    public static final String USER_PASS = "Password";
    public static final String SHOP = "Shop";

    private SharedPreferences mSettings;
    private MaterialEditText edtLogin;
    private MaterialEditText edtPass;
    private MaterialEditText edtApiAdr;
    private Spinner spShops;
    private Button btnSave;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        edtLogin = (MaterialEditText) findViewById(R.id.edt_edt_login);
        edtPass = (MaterialEditText) findViewById(R.id.edt_password);
        edtApiAdr = (MaterialEditText) findViewById(R.id.edt_api_address);
        spShops = (Spinner) findViewById(R.id.sp_shops);
        btnSave = (Button) findViewById(R.id.btn_save);

        Resources res = getResources();
        String[] shops = res.getStringArray(R.array.shops);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.row_spinner_shops, shops);

        spShops.setAdapter(spinnerArrayAdapter);

        mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        edtLogin.setText(mSettings.getString(USER_LOGIN, ""));
        edtPass.setText(mSettings.getString(USER_PASS, ""));
        edtApiAdr.setText(mSettings.getString(API_ADDRESS, ""));

        String spinnerSelectedItem = mSettings.getString(SHOP, shops[0]);
        int spinnerSelectedItemIndex = Arrays.asList(shops).indexOf(spinnerSelectedItem);
        spShops.setSelection(spinnerSelectedItemIndex);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields()) {
                    saveToPrefs();

                    if (!getIntent().getBooleanExtra(OPEN_SETTINGS, false)) {
                        startActivityVipCard();
                    }
                    ActivitySettings.this.finish();
                }
            }
        });

    }

    public void startActivityVipCard() {
        Intent intent = new Intent(ActivitySettings.this, ActivityVipCard.class);
        startActivity(intent);
    }

    public boolean checkFields() {

        String errorLogin = getString(R.string.s_alert_login);
        String errorPassword = getString(R.string.s_alert_pass);
        String errorApiAdr = getString(R.string.s_alert_api);

        if (TextUtils.isEmpty(edtLogin.getText().toString())) {
            edtLogin.setError(errorLogin);
            return false;
        }

        if (TextUtils.isEmpty(edtPass.getText().toString())) {
            edtPass.setError(errorPassword);
            return false;
        }

        if (TextUtils.isEmpty(edtApiAdr.getText().toString())) {
            edtApiAdr.setError(errorApiAdr);
            return false;
        }

        return true;
    }

    public void saveToPrefs() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(USER_LOGIN, edtLogin.getText().toString());
        editor.putString(USER_PASS, edtPass.getText().toString());
        editor.putString(API_ADDRESS, edtApiAdr.getText().toString());
        editor.putString(SHOP, spShops.getSelectedItem().toString());
        editor.apply();
    }

}
