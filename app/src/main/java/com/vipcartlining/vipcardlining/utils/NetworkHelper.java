package com.vipcartlining.vipcardlining.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.SparseArray;

import com.vipcartlining.vipcardlining.R;
import com.vipcartlining.vipcardlining.VipCardLiNingApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by CodeX on 24.06.2015.
 *
 */

public class NetworkHelper {
    public static final int REQUEST_STATUS_OK = 2;
    private static final String API_URL = "http://vvmarket.cloudapp.net/pos_client/api/";
    private static Logger log = LoggerFactory.getLogger(NetworkHelper.class);
    private static ExecutorService mExecService = Executors.newCachedThreadPool();


    public static void addVipCard(SparseArray values, Context context, RequestListener listener){
        SharedPreferences mSettings = context.getSharedPreferences(VipCardLiNingApp.APP_PREFERENCES, Context.MODE_PRIVATE);

        String date = Utils.getCurrentTimeStamp();
        String login = mSettings.getString(VipCardLiNingApp.USER_LOGIN, "__Said__");
        String password =  mSettings.getString(VipCardLiNingApp.USER_PASS, "qwerty");
        String api = mSettings.getString(VipCardLiNingApp.API_ADDRESS, API_URL);
        String shop = mSettings.getString(VipCardLiNingApp.SHOP, "Худжанд");

        HashMap<String, String> parseStringArray = Utils.parseStringArray(context, R.array.shops);

        shop = Utils.getKeyByValue(parseStringArray, shop);

        String gender = Utils.getKeyByValue(Utils.parseStringArray(context, R.array.gender), (String) values.get(10));

        log.debug("gender {}", gender);

        String checksum = "";
        try {
            checksum = Utils.SHA1(date + "#" + login + "#" + password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String request = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<magazin>\n" +
                "\t<seller login=\"" +
                login +
                "\" stock=\"" +
                shop +
                "\" date=\"" +
                date +
                "\" checksum=\"" +
                checksum +
                "\" act=\"4\">\n" +
                "</seller>\n" + "<discount first_name=\"" +
                values.get(0) +
                "\"\n" + "last_name=\"" +
                values.get(1) +
                "\"\n" + "patronymic=\"" +
                values.get(2) +
                "\"\n" + "phone=\"" +
                values.get(3) +
                "\"\n" + "discount_code=\"" +
                values.get(4) +
                "\"\n" + "email=\"" +
                values.get(5) +
                "\"\n" + "birthday=\"" +
                values.get(6) +
                "\"\n" + "wear_size=\"" +
                values.get(7) +
                "\"\n" + "shoes_size=\"" +
                values.get(8) +
                "\"\n" + "photo=\"" +
                values.get(9)
                /*Utils.bytesToHex(
                        Utils.getBytes(
                                //  ((BitmapDrawable) values.get(9).getDrawable()).getBitmap())) +
                                (Bitmap) values.get(9)))*/ +
                "\"\n" + "gender=\"" +
                gender +
                "\"/>\n" + "</magazin>";


        log.debug("addCard query = {}", request);
        postRequest(api, request, listener);

    }

    public static boolean hasWIFIConnection(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected())
        {
            return true;
        }
        return false;

    }

    private static void postRequest(final String url, final String query, final RequestListener listener) {
        mExecService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader in = openPostConnection(url, query);
                    String jRequest = Utils.convertStreamToString(in);
                    in.close();
                    listener.OnRequestComplete(jRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.OnRequestError(e);
                }
            }
        });
    }

    private static BufferedReader openPostConnection(String url, String query) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();
        conn.connect();

        int responseCode = conn.getResponseCode();
        Log.d("Response Code ", String.valueOf(responseCode));
        if (responseCode != HttpsURLConnection.HTTP_OK) {
            throw new Exception(responseCode + " Bad Response Code");
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        return in;
    }

    public interface RequestListener {
        void OnRequestComplete(Object result);
        void OnRequestError(Exception error);
    }

}
