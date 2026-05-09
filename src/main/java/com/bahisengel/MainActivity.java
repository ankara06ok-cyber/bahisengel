package com.bahisengel;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERM_REQUEST = 1001;
    private static final String PREFS_NAME = "BahisEngelPrefs";
    private static final String KEY_BLOCKED_LIST = "blocked_messages";
    private static final String KEY_BLOCK_ENABLED = "block_enabled";
    private static final String KEY_NOTIFY_ENABLED = "notify_enabled";
    private static final String KEY_AUTO_DELETE = "auto_delete";
    private static final String KEY_BLOCKED_COUNT = "blocked_count";

    private SharedPreferences prefs;
    private TextView tvBlockedCount;
    private Switch swBlockEnabled, swNotifyEnabled, swAutoDelete;
    private ListView lvMessages;
    private List<String> messageList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Görünümleri bağla
        tvBlockedCount = findViewById(R.id.tv_blocked_count);
        swBlockEnabled  = findViewById(R.id.sw_block_enabled);
        swNotifyEnabled = findViewById(R.id.sw_notify_enabled);
        swAutoDelete    = findViewById(R.id.sw_auto_delete);
        lvMessages      = findViewById(R.id.lv_messages);

        // Liste adaptörü
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
        lvMessages.setAdapter(adapter);

        // İzin kontrolü
        checkAndRequestPermissions();

        // Toggle değerlerini yükle
        loadSettings();

        // Toggle dinleyicileri
        swBlockEnabled.setOnCheckedChangeListener((b, isChecked) ->
            prefs.edit().putBoolean(KEY_BLOCK_ENABLED, isChecked).apply());
        swNotifyEnabled.setOnCheckedChangeListener((b, isChecked) ->
            prefs.edit().putBoolean(KEY_NOTIFY_ENABLED, isChecked).apply());
        swAutoDelete.setOnCheckedChangeListener((b, isChecked) ->
            prefs.edit().putBoolean(KEY_AUTO_DELETE, isChecked).apply());

        // Listeyi temizle butonu
        Button btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(v -> {
            prefs.edit()
                .putString(KEY_BLOCKED_LIST, "[]")
                .putInt(KEY_BLOCKED_COUNT, 0)
                .apply();
            loadSettings();
            Toast.makeText(this, "Liste temizlendi", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSettings();
    }

    private void loadSettings() {
        swBlockEnabled.setChecked(prefs.getBoolean(KEY_BLOCK_ENABLED, true));
        swNotifyEnabled.setChecked(prefs.getBoolean(KEY_NOTIFY_ENABLED, true));
        swAutoDelete.setChecked(prefs.getBoolean(KEY_AUTO_DELETE, false));

        int count = prefs.getInt(KEY_BLOCKED_COUNT, 0);
        tvBlockedCount.setText(count + " mesaj engellendi");

        // Engellenen mesaj listesini yükle
        messageList.clear();
        try {
            String stored = prefs.getString(KEY_BLOCKED_LIST, "[]");
            JSONArray arr = new JSONArray(stored);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String line = "📵 " + obj.getString("time") + " | " +
                              obj.getString("sender") + "\n" +
                              obj.getString("body");
                messageList.add(line);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

        if (messageList.isEmpty()) {
            messageList.add("Henüz engellenmiş mesaj yok.\nKoruma aktifse bahis SMS'leri burada görünecek.");
            adapter.notifyDataSetChanged();
        }
    }

    private void checkAndRequestPermissions() {
        List<String> missing = new ArrayList<>();
        String[] needed = {
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
        };
        for (String perm : needed) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                missing.add(perm);
            }
        }
        if (!missing.isEmpty()) {
            ActivityCompat.requestPermissions(this, missing.toArray(new String[0]), PERM_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == PERM_REQUEST) {
            for (int r : results) {
                if (r != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                        "SMS izni gerekli! Ayarlardan izin verin.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Toast.makeText(this, "İzinler verildi. Koruma aktif!", Toast.LENGTH_SHORT).show();
        }
    }
}
