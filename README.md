# 🛡️ BahisEngel - Android SMS Filtre Uygulaması

Bahis sitelerinden gelen SMS'leri otomatik olarak engelleyen Android uygulaması.

## Özellikler

- ✅ Bahis/casino SMS'lerini gerçek zamanlı engeller
- ✅ 30'dan fazla Türk bahis sitesi kelime listesi
- ✅ Engellenen mesajları kaydeder ve listeler
- ✅ Bildirim gönderir (isteğe bağlı)
- ✅ Kolay açma/kapama switch'leri
- ✅ Android 5.0+ (API 21) destekler

## APK Oluşturma Adımları

### 1. Android Studio Kurulumu
https://developer.android.com/studio adresinden Android Studio'yu indirin ve kurun.

### 2. Projeyi Açın
- Android Studio → "Open an existing project"
- Bu klasörü (BahisEngel) seçin

### 3. APK Derleyin
- Menüden: Build → Build Bundle(s)/APK(s) → Build APK(s)
- APK dosyası: `app/build/outputs/apk/debug/app-debug.apk`

### 4. Telefona Kurun
- Telefonunuzda "Bilinmeyen kaynaklardan yükle"yi aktif edin
  (Ayarlar → Güvenlik → Bilinmeyen kaynaklar)
- APK dosyasını telefonunuza kopyalayın ve açın

## İzinler

Uygulama şu izinleri gerektirir:
- `RECEIVE_SMS` - SMS'leri yakalamak için (zorunlu)
- `READ_SMS` - Mevcut SMS'leri okumak için
- `POST_NOTIFICATIONS` - Bildirim göndermek için

## Dosya Yapısı

```
BahisEngel/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml       ← İzinler ve bileşenler
│       ├── java/com/bahisengel/
│       │   ├── MainActivity.java     ← Ana ekran
│       │   └── SmsReceiver.java      ← SMS yakalama + filtreleme
│       └── res/
│           ├── layout/activity_main.xml  ← Arayüz
│           └── values/strings.xml
├── build.gradle
└── settings.gradle
```

## Anahtar Kelimeler (SmsReceiver.java'da düzenlenebilir)

Bahis sitelerini tespit etmek için kullanılan kelimeler:
- Genel: bahis, casino, bet, slot, rulet, poker, jackpot, bonus, iddaa
- İşlem: bedava spin, kayıt ol, üye ol, para yatır, çekim yap
- Siteler: 1xbet, bets10, betturkey, betpark, piabet, superbahis, mobilbahis...

Yeni kelime eklemek için `SmsReceiver.java` dosyasındaki `BAHIS_KEYWORDS` dizisini düzenleyin.

## Önemli Not

Android 10+ sürümlerde SMS engelleme için uygulamanın varsayılan SMS uygulaması olması gerekebilir
veya sistem ayarlarından SMS filtre uygulaması olarak tanımlanması gerekir.
Bazı cihazlarda tam engelleme için "Varsayılan SMS uygulaması" olarak ayarlanmanız gerekebilir.
