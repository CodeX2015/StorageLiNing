package com.vipcartlining.vipcardlining;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.Spinner;
import com.vipcartlining.vipcardlining.cpb.CircularProgressButton;
import com.vipcartlining.vipcardlining.utils.DateDialogFrament;
import com.vipcartlining.vipcardlining.utils.NetworkHelper;
import com.vipcartlining.vipcardlining.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by CodeX on 05.07.2015.
 */
public class ActivityVipCard extends AppCompatActivity {
    private static Logger log = LoggerFactory.getLogger(ActivityVipCard.class);
    public final static int REQUEST_CODE_PHOTO = 1;
    public final static int REQUEST_CODE_CARD_BARCODE = 2;

    private MaterialEditText etFName;
    private MaterialEditText etLName;
    private MaterialEditText etMName;
    private MaterialSpinner spGender;
    private MaterialSpinner spSizeWear;
    private MaterialSpinner spSizeShoes;
    private MaterialEditText etPhone;
    private MaterialEditText etDiscountCode;
    private MaterialEditText etEmail;
    private MaterialEditText etBirthday;
    private ImageButton btnScanCartCode;
    private ImageButton btnAddPhoto;
    private Button btnRefresh;
    private Button btnSettings;
    private ImageView ivPhoto;
    private CircularProgressButton btnAddCard;
    private String valiBirthdDate;
    private boolean isDialogAlreadyShowed;

    SpinnerAdapterWithHint sizesWearAdapter;
    SpinnerAdapterWithHint genderAdapter;
    SpinnerAdapterWithHint sizesShoesAdapter;
    Map<String, String> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vipcard);

        btnAddCard = (CircularProgressButton) findViewById(R.id.btn_AddCard);
        btnAddCard.setIndeterminateProgressMode(true);
        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    btnAddCard.setProgress(1);
                   addVipCard();

                }
            }
        });

        etFName = (MaterialEditText) findViewById(R.id.etFirstName);
        etLName = (MaterialEditText) findViewById(R.id.etLastName);
        etMName = (MaterialEditText) findViewById(R.id.etPatronymic);
        spGender = (MaterialSpinner) findViewById(R.id.sp_gender);
        etPhone = (MaterialEditText) findViewById(R.id.etPhone);
        etDiscountCode = (MaterialEditText) findViewById(R.id.etDiscountCode);
        etEmail = (MaterialEditText) findViewById(R.id.etEmail);
        etBirthday = (MaterialEditText) findViewById(R.id.etBirthday);
        spSizeWear = (MaterialSpinner) findViewById(R.id.sp_size_wear);
        spSizeShoes = (MaterialSpinner) findViewById(R.id.sp_size_shoes);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        btnAddPhoto = (ImageButton) findViewById(R.id.btn_addPhoto);
        btnScanCartCode = (ImageButton) findViewById(R.id.btn_scanCartCode);
        btnRefresh = (Button) findViewById(R.id.btn_clearFields);
        btnSettings = (Button) findViewById(R.id.btn_settings);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFields();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityVipCard.this, ActivitySettings.class);
                intent.putExtra(ActivitySettings.OPEN_SETTINGS, true);
                startActivity(intent);
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_PHOTO);
            }
        });

        btnScanCartCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(ActivityVipCard.this, ActivityCodeScanner.class);
                startActivityForResult(intent, REQUEST_CODE_CARD_BARCODE);

            }
        });

        etBirthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!isDialogAlreadyShowed)
                    showDatePicker();
                return false;
            }
        });

        setSpinners();
    }

    //ToDo optimize adapter filling
    private void setSpinners(){

        map =  Utils.parseStringArray(this, R.array.gender);

        ArrayList<String> gender = new ArrayList<String>();
        for(int i = 0; i < map.size(); i++) {
            String key = (String) map.keySet().toArray()[i];
            // get the object by the key.
            gender.add(map.get(key));
        }

        String[] sizesWear = getResources().getStringArray(R.array.sizes_wear);
        String[] sizesShoes = getResources().getStringArray(R.array.sizes_shoes);

        genderAdapter = new SpinnerAdapterWithHint(this, android.R.layout.simple_spinner_item, gender);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(genderAdapter);

        sizesWearAdapter = new SpinnerAdapterWithHint(this,android.R.layout.simple_spinner_item, sizesWear);
        sizesWearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSizeWear.setAdapter(sizesWearAdapter);

        sizesShoesAdapter = new SpinnerAdapterWithHint(this, android.R.layout.simple_spinner_item, sizesShoes) ;
        sizesShoesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSizeShoes.setAdapter(sizesShoesAdapter);
        //spSizeShoes.getChildAt(0).findViewById(android.R.id.text1).setPadding(-5,0,0,0);
    }

    private class SpinnerAdapterWithHint extends ArrayAdapter<String> {

        public SpinnerAdapterWithHint(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        public SpinnerAdapterWithHint(Context context, int resource, List objects){
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = super.getView(position, convertView, parent);
            TextView qwe = ((TextView)v.findViewById(android.R.id.text1));
            return v;
        }
    }

    private void showDatePicker() {
        isDialogAlreadyShowed = true;
        DateDialogFrament date = new DateDialogFrament();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                final String month = monthOfYear + 1 < 10 ? "0" + String.valueOf(monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                final String day = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                etBirthday.setText(day + "."
                        + month + "."
                        + String.valueOf(year));

                valiBirthdDate = String.valueOf(year) + "-" + month + "-" + day;
            }
        });
        date.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                isDialogAlreadyShowed = false;
            }
        });
        date.show(getSupportFragmentManager(), "Date Picker");
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        switch (requestCode) {
            case REQUEST_CODE_PHOTO: {
                if (resultCode == RESULT_OK) {
                    if (intent == null) {
                    } else {
                        Bundle bndl = intent.getExtras();
                        if (bndl != null) {
                            Object obj = intent.getExtras().get("data");
                            if (obj instanceof Bitmap) {
                                Bitmap bitmap = (Bitmap) obj;
                                ivPhoto.setImageBitmap(bitmap);
                                ivPhoto.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else if (resultCode == RESULT_CANCELED) {

                }
                break;
            }
            case REQUEST_CODE_CARD_BARCODE: {
                if (resultCode == RESULT_OK) {
                    String cardBarcode = intent.getStringExtra(ActivityCodeScanner.RESULT_BARCODE);
                    etDiscountCode.setText(cardBarcode);
                }
                break;
            }
        }
    }

    private boolean checkFields() {
        String error = getString(R.string.s_required_field);

        if (TextUtils.isEmpty(etFName.getText().toString())) {
            etFName.setError(error);
            return false;
        }

        if (TextUtils.isEmpty(etLName.getText().toString())) {
            etLName.setError(error);
            return false;
        }

        if (spGender.getSelectedItemPosition() == 0) {
            spGender.setError(error);
            return false;
        }

        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            etPhone.setError(error);
            return false;
        }

        if (TextUtils.isEmpty(etDiscountCode.getText().toString())) {
            etDiscountCode.setError(error);
            return false;
        }

        if (TextUtils.isEmpty(etBirthday.getText().toString())) {
            etBirthday.setError(error);
            return false;
        }

        if (spSizeWear.getSelectedItemPosition()==0) {
            spSizeWear.setError(error);
            return false;
        }

        if (spSizeShoes.getSelectedItemPosition()==0) {
            spSizeShoes.setError(error);
            return false;
        }

        return true;
    }

    private void clearFields() {
        etFName.setText("");
        etLName.setText("");
        etMName.setText("");
        etMName.setText("");
        etPhone.setText("");
        etDiscountCode.setText("");
        etEmail.setText("");
        etBirthday.setText("");

        spGender.setSelection(0);
        spSizeWear.setSelection(0);
        spSizeShoes.setSelection(0);

        //ivPhoto.setImageResource(android.R.color.transparent);
        ivPhoto.setImageDrawable(null);
        ivPhoto.setImageDrawable(null);
    }

    private void addVipCard() {

        SparseArray values = new SparseArray();

        values.put(0, etFName.getText().toString());
        values.put(1, etLName.getText().toString());
        values.put(2, etMName.getText().toString());
        values.put(3, etPhone.getText().toString());
        values.put(4, etDiscountCode.getText().toString());
        values.put(5, etEmail.getText().toString());
        values.put(6, valiBirthdDate);
        values.put(7, spSizeWear.getSelectedItem().toString());
        values.put(8, spSizeShoes.getSelectedItem().toString());

        log.debug("photo is {} ", ivPhoto.getDrawable());

        String photo = ivPhoto.getDrawable() == null ? "" : Utils.bytesToHex(
                Utils.getBytes(
                        ((BitmapDrawable) ivPhoto.getDrawable()).getBitmap()));
        values.put(9, photo);
        values.put(10, spGender.getSelectedItem().toString());

       // if (NetworkHelper.hasWIFIConnection(this)) {

            NetworkHelper.addVipCard(values, this, new NetworkHelper.RequestListener() {
                @Override
                public void OnRequestComplete(final Object result) {

                    final Response response = Utils.convertJSONtoProduct(Utils.convertXmltoJSON((String) result));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.getStatus() == NetworkHelper.REQUEST_STATUS_OK) {
                                log.debug("addVipCard sucess {}", (String) result);
                                btnAddCard.setProgress(100);
                                btnAddCard.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        clearFields();
                                        btnAddCard.setProgress(0);
                                    }
                                }, 1500);

                            } else {
                                btnAddCard.setProgress(-1);
                                btnAddCard.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.showErrorDialog(ActivityVipCard.this, response.getDescription());
                                        btnAddCard.setProgress(0);
                                    }
                                }, 1500);
                            }
                        }
                    });
                }

                @Override
                public void OnRequestError(final Exception error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnAddCard.setProgress(-1);
                            btnAddCard.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showErrorDialog(ActivityVipCard.this, error.getMessage());
                                    btnAddCard.setProgress(0);
                                }
                            }, 1500);
                        }
                    });
                }
            });
        //} else {
       //     btnAddCard.setProgress(0);
          //  Utils.showErrorDialog(ActivityVipCard.this, getString(R.string.s_dialog_message_wifi));
       // }
    }
}
