# decisions.md: Catatan Keputusan Teknis

Satu entri per keputusan. Format: tanggal, keputusan, alasan singkat.

---

## 2026-11-01 — compileSdk 37, targetSdk 36, minSdk 29

**Keputusan:** `compileSdk = 37`, `targetSdk = 36`, `minSdk = 29`.

**Alasan:** `compileSdk` hanya memengaruhi API yang tersedia saat kompilasi, bukan perilaku aplikasi saat berjalan. Build gagal pada `compileSdk 36.1` karena `androidx.core:core-ktx:1.19.1` dan `androidx.lifecycle:lifecycle-runtime-ktx:2.11.0` mensyaratkan `compileSdk 37`. `targetSdk` tetap 36 dan `minSdk` tetap 29 sesuai keputusan terkunci.

---

## 2026-11-01 — Alias plugin library di root dan modul

**Keputusan:** plugin `com.android.library` didaftarkan di `build.gradle.kts` root dengan `apply false`, dan setiap modul library memakai `alias(libs.plugins.android.library)`.

**Alasan:** AGP sudah ada di classpath root lewat `com.android.application`, sehingga memakai alias ber-versi di modul gagal ("plugin is already on the classpath with an unknown version"). Mendaftarkannya di root membuat versi terkontrol di satu tempat (version catalog) dan modul cukup memakai alias tanpa duplikasi versi.

---

## 2026-11-01 — Modul `feature-*` ditunda

**Keputusan:** struktur modul saat ini hanya `app`, `core`, `service`, `jvm`, `data`. Modul `feature-*` dibuat saat fitur pertama dikerjakan di Fase 1.

**Alasan:** modul fitur kosong tidak memberi nilai dan menambah waktu build serta konfigurasi; batas fitur baru jelas setelah UI pertama mulai dibangun.

---

## 2026-11-01 — Kunci portrait tetap dipakai

**Keputusan:** aplikasi tetap portrait saja, meskipun Android 16 dapat mengabaikan permintaan orientasi pada layar besar.

**Alasan:** tablet dan landscape di luar scope produk. Kunci portrait tetap menjadi perilaku yang diharapkan di HP, dan mengabaikannya di layar besar tidak menimbulkan masalah karena perangkat tersebut tidak didukung.

---

## 2026-11-01 — Peringatan versi dependensi tidak dinaikkan selama Fase A

**Keputusan:** peringatan versi dependensi (AGP, Compose BOM, plugin Compose) tidak ditindaklanjuti selama Fase A; versi dinaikkan satu per satu menjelang rilis pertama.

**Alasan:** mengejar versi terbaru di tengah persiapan berisiko memecah build tanpa manfaat langsung. Menaikkan satu per satu menjelang rilis memudahkan menemukan penyebab bila ada yang gagal.

---

## 2026-11-01 — Pesan commit dalam bahasa Inggris

**Keputusan:** pesan commit ditulis dalam bahasa Inggris.

**Alasan:** riwayat git tetap konsisten dan dapat dibaca kontributor luar bila proyek dibuka untuk umum, sementara dokumen dan diskusi proyek tetap berbahasa Indonesia.

---

## 2026-11-03 — CI GitHub Actions

**Keputusan:** CI berjalan di `ubuntu-latest` dengan JDK 21 (distribusi temurin) dan menjalankan `assembleFossDebug`, `assemblePlayDebug`, `lintFossDebug`, serta `:app:testFossDebugUnitTest` (dengan `--no-daemon --stacktrace`). Action yang dipakai dan terbukti jalan pada run hijau pertama: `actions/checkout@v7`, `actions/setup-java@v6`, `gradle/actions/setup-gradle@v6`, `actions/upload-artifact@v7`.

**Alasan:** satu job tunggal cukup untuk ukuran proyek ini dan menangkap kesalahan build kedua flavor, lint, dan unit test sebelum digabung. Versi mayor dipatok agar pembaruan tidak masuk diam-diam, dan action `setup-gradle` dipakai supaya cache Gradle ditangani tanpa skrip cache buatan sendiri.

---

## 2026-11-03 — `gradlew` executable dengan akhir baris LF

**Keputusan:** `gradlew` disimpan di git dengan mode executable (`100755`) dan akhir baris LF, supaya bisa dijalankan langsung (`./gradlew`) di runner Linux.

**Alasan:** runner Linux menolak menjalankan skrip tanpa bit executable (`Permission denied`), dan akhir baris CRLF merusak skrip shell. Menyimpannya benar di git lebih baik daripada menambah langkah `chmod`/konversi di setiap workflow.
