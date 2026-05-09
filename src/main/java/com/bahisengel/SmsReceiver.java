package com.bahisengel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SmsReceiver extends BroadcastReceiver {

    // Bahis sitelerine ait anahtar kelimeler (Türkçe odaklı)
    private static final String[] BAHIS_KEYWORDS = {
        "bahis", "casino", "bet", "slot", "rulet", "poker", "blackjack",
        "jackpot", "bonus", "iddaa", "bedava spin", "ücretsiz spin",
        "kayıt ol", "üye ol", "para yatır", "çekim yap", "kazanma garantili",
        "vip üyelik", "hoş geldin bonusu", "ilk yatırım bonusu",
        "1xbet", "bets10", "betturkey", "betpark", "piabet", "superbahis",
        "mobilbahis", "betboo", "tipobet", "perabet", "belugabahis",
        "restbet", "betvakti", "casinomaxi", "jojobet", "youwin",
        "winxbet", "hipercasino", "mroyun", "bahsegel", "tempobet",
        "www.bet", ".bet/", "bahis sitesi", "canlı bahis", "spor bahis",
        "oranlar yükseldi", "maç bahsi", "oran artışı"
    };

    private static final String PREFS_NAME = "BahisEngelPrefs";
    private static final String KEY_BLOCKED_LIST = "blocked_messages";
    private static final String KEY_BLOCK_ENABLED = "block_enabled";
    private static final String KEY_NOTIFY_ENABLED = "notify_enabled";
    private static final String KEY_AUTO_DELETE = "auto_delete";
    private static final String KEY_BLOCKED_COUNT = "blocked_count";
    private static final String CHANNEL_ID = "bahis_engel_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Engelleme kapalıysa işlem yapma
        if (!prefs.getBoolean(KEY_BLOCK_ENABLED, true)) return;

        Bundle bundle = intent.getExtras();
        if (bundle == null) return;

        Object[] pdus = (Object[]) bundle.get("pdus");
        String format = bundle.getString("format");
        if (pdus == null) return;

        for (Object pdu : pdus) {
            SmsMessage sms;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                sms = SmsMessage.createFromPdu((byte[]) pdu, format);
            } else {
                sms = SmsMessage.createFromPdu((byte[]) pdu);
            }

            if (sms == null) continue;

            String sender = sms.getDisplayOriginatingAddress();
            String body = sms.getMessageBody();

            if (isBahisMesaji(body)) {
                // Mesajı engelle (gelmesini iptal et)
                abortBroadcast();

                // Engellenen sayacını artır
                int count = prefs.getInt(KEY_BLOCKED_COUNT, 0);
                prefs.edit().putInt(KEY_BLOCKED_COUNT, count + 1).apply();

                // Engellenen mesajı kaydet
                saveBlockedMessage(context, prefs, sender, body);

                // Bildirim gönder
                if (prefs.getBoolean(KEY_NOTIFY_ENABLED, true)) {
                    showNotification(context, sender);
                }
            }
        }
    }

    /**
     * SMS metninde bahis anahtar kelimelerini kontrol eder
     */
    private boolean isBahisMesaji(String body) {
        if (body == null) return false;
        String lowerBody = body.toLowerCase(new Locale("tr", "TR"));

        for (String keyword : BAHIS_KEYWORDS) {
            if (lowerBody.contains(keyword.toLowerCase(new Locale("tr", "TR")))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Engellenen mesajı SharedPreferences'a kaydeder
     */
    private void saveBlockedMessage(Context context, SharedPreferences prefs, String sender, String body) {
        try {
            String existing = prefs.getString(KEY_BLOCKED_LIST, "[]");
            JSONArray list = new JSONArray(existing);

            JSONObject msg = new JSONObject();
            msg.put("sender", sender != null ? sender : "Bilinmiyor");
            msg.put("body", body.length() > 100 ? body.substring(0, 100) + "..." : body);
            msg.put("time", new SimpleDateFormat("dd.MM.yyyy HH:mm", new Locale("tr", "TR")).format(new Date()));

            // Listenin başına ekle, 100 mesajla sınırla
            JSONArray newList = new JSONArray();
            newList.put(msg);
            for (int i = 0; i < Math.min(list.length(), 99); i++) {
                newList.put(list.get(i));
            }

            prefs.edit().putString(KEY_BLOCKED_LIST, newList.toString()).apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Bildirim gösterir
     */
    private void showNotification(Context context, String sender) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "BahisEngel Bildirimleri", NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(channel);
        }

        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("🛡️ Bahis SMS Engellendi")
            .setContentText(sender + " adresinden gelen bahis mesajı engellendi")
            .setAutoCancel(true)
            .setContentIntent(pi);

        nm.notify((int) System.currentTimeMillis(), builder.build());
    }
}
