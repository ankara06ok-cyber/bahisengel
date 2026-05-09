# 📱 BahisEngel APK - GitHub ile Derleme Rehberi

## Adım Adım Talimat (10 dakika)

---

## 1. GitHub Hesabı Aç

👉 https://github.com adresine git → "Sign up" ile ücretsiz hesap aç
(zaten varsa giriş yap)

---

## 2. Yeni Repo Oluştur

1. Sağ üstte **"+"** → **"New repository"** tıkla
2. Repository name: `bahisengel`
3. **Public** seç (Private'da Actions bedava değil)
4. **"Create repository"** tıkla

---

## 3. Dosyaları Yükle

Repo sayfasında **"uploading an existing file"** linkine tıkla.

ZIP'i bilgisayarda bir klasöre aç. Şu dosya/klasörleri sürükleyip bırak:

```
📁 .github/
📁 app/
📁 gradle/
📄 build.gradle
📄 settings.gradle
📄 gradlew
📄 gradlew.bat
```

> ⚠️ `.github` klasörünü görmüyorsan: Windows'ta "Gizli öğeleri göster"i aktif et

**"Commit changes"** butonuna tıkla.

---

## 4. APK Otomatik Derlenir

Dosyaları yükledikten sonra GitHub otomatik olarak derlemeye başlar!

1. Repo sayfasında üstteki **"Actions"** sekmesine tıkla
2. "BahisEngel APK Derle" iş akışını göreceksin
3. Sarı ⚙️ = devam ediyor (~3-5 dakika bekle)
4. Yeşil ✅ = tamamlandı!

---

## 5. APK'yı İndir

1. Actions → tamamlanan iş akışına tıkla
2. Sayfanın altında **"Artifacts"** bölümünü bul
3. **"BahisEngel-APK"** linkine tıkla → ZIP indirilir
4. ZIP içinden `app-debug.apk` dosyasını çıkar

---

## 6. Telefona Yükle (USB ile)

1. **Telefon ayarları** → Güvenlik → **"Bilinmeyen kaynaklar"** aktif et
   (veya: Ayarlar → Uygulamalar → Özel uygulama erişimi → Bilinmeyen uygulamalar yükle)

2. APK dosyasını USB ile telefona kopyala

3. Telefonda dosya yöneticisinden APK'yı aç → **"Yükle"**

4. Uygulama açılınca SMS izni ver → **Koruma aktif!** 🛡️

---

## ❓ Sorun mu Çıktı?

Actions sekmesinde kırmızı ❌ görürsen, iş akışına tıkla → hata mesajını bana göster, düzeltirim.
