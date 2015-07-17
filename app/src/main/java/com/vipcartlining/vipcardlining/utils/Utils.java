package com.vipcartlining.vipcardlining.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.vipcartlining.vipcardlining.R;
import com.vipcartlining.vipcardlining.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by CodeX on 24.06.2015.
 */

public class Utils {
    private static Logger log = LoggerFactory.getLogger(Utils.class);
    private static Gson mGson = new Gson();

    public static void showErrorDialog(Context context, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(context.getResources().getString(R.string.s_dialog_title));
        builder.setMessage(context.getResources().getString(R.string.s_dialog_message).concat(message));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            }
        });

        builder.show();
    }

    public static Response convertJSONtoProduct(JSONObject jsonObj) {
        String json = jsonObj.toString();
        JsonObject jRequest = mGson.fromJson(json, JsonObject.class).getAsJsonObject("magazin");
        Response response = mGson.fromJson(jRequest, new TypeToken<Response>() {
        }.getType());
        return response;
    }

    public static JSONObject convertXmltoJSON(String xml) {
        JSONObject jsonObj = null;
        try {
            jsonObj = XML.toJSONObject(xml);
        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        }

        Log.d("XML", xml);
        Log.d("JSON", jsonObj.toString());

        return jsonObj;
    }

    static void convertJsonToXml(String str) {
        JSONObject json = null;
        try {
            json = new JSONObject(str);
            String xml = XML.toString(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getKeyByValue(Map<String, String> map, String value) {
        for(Map.Entry entry: map.entrySet()){
            if(value.equals(entry.getValue())){
                return (String) entry.getKey();
            }
        }
        return null;
    }

    public static HashMap<String, String> parseStringArray(Context context, int stringArrayResourceId) {
        String[] stringArray = context.getResources().getStringArray(stringArrayResourceId);
        HashMap<String, String> outPutMap = new HashMap<String, String>(stringArray.length);
        for (String entry : stringArray) {
            String[] splitResult = entry.split("\\|", 2);
            outPutMap.put(splitResult[0], splitResult[1]);
        }
        return outPutMap;
    }

    public static String convertStreamToString(BufferedReader reader) {
        StringBuilder sb = new StringBuilder();
        String resultString = null;
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (sb.toString().toLowerCase().contains("<stock")) {
            resultString = sb.toString().replace("</magazin>", "<stock empty=\"\"/></magazin>").replace("</stock>", "<product empty=\"\"/></stock>");
        } else {resultString = sb.toString();}

        return resultString;
                //sb.toString().replace("</stock>", "<product empty=\"\"/></stock>").replace("</magazin>", "<stock empty=\"\"/></magazin>");
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        return sdfDate.format(now);
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
