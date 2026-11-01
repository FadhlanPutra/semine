# AGENTS.md: Aturan Wajib untuk Model AI

File ini ditujukan untuk **model AI / agen coding** (Claude, ChatGPT, Gemini, Cursor, Copilot, dan lainnya), bukan untuk manusia. **Baca seluruh file ini sebelum mengerjakan apa pun.** Isinya adalah keputusan proyek yang sudah disepakati pemilik proyek. Tugas Anda adalah bekerja **di dalam** batas ini, bukan mengubahnya.

Pemilik proyek: Fadhlan. Proyek solo, pribadi, direncanakan open source.

---

## 0. Aturan Emas

1. **Hanya kerjakan yang ada di scope (bagian 3).** Jika permintaan tidak ada di scope, **berhenti dan ikuti protokol bagian 2.**
2. **Jangan mengubah keputusan terkunci (bagian 4)** tanpa persetujuan eksplisit pemilik proyek.
3. **Jangan menebak.** Jika sesuatu tidak jelas atau ada di daftar keputusan terbuka (bagian 9), tanyakan dulu.
4. **Jangan melakukan perubahan di luar permintaan.** Tidak ada refactor, rename, pembaruan dependensi, atau "perbaikan sekalian" yang tidak diminta.
5. **Jangan berbohong soal hasil.** Jangan menyatakan sesuatu sudah dites, berjalan, atau selesai bila belum benar-benar diverifikasi. Katakan apa yang sudah dan belum dicoba.

---

## 1. Dokumen Proyek dan Urutan Prioritas

Baca sesuai urutan ini sebelum bekerja:

| Urutan | File | Fungsi |
|---|---|---|
| 1 | `AGENTS.md` (file ini) | Aturan dan batas. Prioritas tertinggi. |
| 2 | `README.md` | Apa proyek ini dan mengapa. |
| 3 | `DESIGN.md` | Spesifikasi UI. Wajib diikuti untuk semua kode antarmuka. |
| 4 | `TASKS.md` | Daftar pekerjaan per fase. Sumber kebenaran tentang apa yang dikerjakan dan kapan. |
| 5 | `docs/decisions.md` | Catatan keputusan teknis (buat bila belum ada). |

Jika dua dokumen bertentangan, ikuti urutan prioritas di atas lalu **laporkan pertentangannya** ke pemilik proyek. Instruksi eksplisit dari pemilik proyek di percakapan boleh menang atas file ini, tetapi Anda **wajib mencatat perubahannya di file ini** (lihat bagian 2).

---

## 2. Protokol Permintaan di Luar Scope

Jika permintaan (dari pemilik proyek, atau dari kebutuhan teknis yang Anda temukan) **tidak tercantum di scope** atau **melanggar keputusan terkunci**:

1. **Jangan dikerjakan dulu.**
2. Katakan dengan jelas: *"Ini tidak ada di scope AGENTS.md"* (atau *"Ini bertentangan dengan keputusan terkunci: ..."*).
3. Jelaskan singkat dampaknya (ukuran APK, risiko, biaya, kerumitan, dampak ke fase lain).
4. Tawarkan pilihan:
   - **A.** Tambahkan ke scope **dan langsung dikerjakan**
   - **B.** Tambahkan ke scope saja (dikerjakan nanti, masuk `TASKS.md`)
   - **C.** Jangan ditambahkan
5. Setelah pemilik proyek memilih A atau B: **perbarui dulu bagian scope di file ini (dan `TASKS.md`)**, baru kerjakan jika dipilih A.
6. Jika pemilik proyek memilih C, jangan dikerjakan dan jangan diusulkan lagi pada percakapan yang sama.

Jangan menambah scope secara diam-diam, bahkan untuk hal kecil.

---

## 3. Scope

### 3.1 Tujuan produk

Aplikasi Android (nama kerja: **Semine**) untuk menjalankan dan mengelola **server Minecraft Java Edition secara lokal di HP**, dengan antarmuka grafis lengkap, tanpa terminal.

### 3.2 Termasuk scope (boleh dikerjakan sesuai fase di `TASKS.md`)

**Server dan Java**
- Menjalankan server Java Edition: Vanilla, Paper, Fabric, Forge, NeoForge, dan `.jar` manual
- Pemuatan JVM di dalam proses lewat JNI, satu service per slot server
- JRE Android (bionic) untuk Java 8/16/17/21/25 sesuai kebutuhan versi Minecraft
- Instalasi server otomatis lewat API (Mojang, PaperMC, Fabric meta, Forge/NeoForge)
- Multi-server (beberapa slot proses)

**Fitur aplikasi**
- Konsol: log + input perintah
- Monitoring real-time: RAM, jumlah dan daftar player online
- Editor `server.properties` (tiga lapis sesuai `DESIGN.md`)
- Manajemen player (whitelist, operator, banned)
- Penjelajah dan editor file server
- Mod/plugin: upload manual + Modrinth
- Impor/ekspor server dan world dalam format zip
- Backup manual dan terjadwal (zip ke lokasi pilihan pengguna lewat SAF)
- Layar Aktivitas (unduhan/backup/tugas), foreground notification per server
- playit.gg sebagai proses terkelola (Fase 4)
- Dua flavor build: `foss` dan `play`
- Antarmuka dwibahasa: Indonesia dan Inggris

### 3.3 Di luar scope (JANGAN dikerjakan, ikuti protokol bagian 2 bila diminta)

- Minecraft Bedrock Edition (BDS, PocketMine, dan sejenisnya)
- Remote control / web UI / akses kelola dari perangkat lain
- CurseForge (butuh API key; ditunda)
- Grafik riwayat monitoring (hanya nilai real-time)
- Arsitektur x86 / armeabi-v7a (awalnya arm64-v8a saja)
- Tablet / landscape (hanya portrait HP)
- Akun pengguna, login, cloud sync, layanan backend milik sendiri
- Iklan, analitik, telemetri, pelacak, atau SDK proprietary apa pun
- Framework UI selain Jetpack Compose (tidak boleh Flutter, React Native, WebView-sebagai-UI, dan sejenisnya)
- Fitur gamifikasi, sosial, chat, marketplace, atau hal lain yang tidak disebut di bagian 3.2

---

## 4. Keputusan Terkunci (JANGAN diubah tanpa persetujuan eksplisit)

### Platform dan stack
- **Bahasa/UI:** Kotlin + Jetpack Compose + Material 3 (tema dipangkas). Kode native hanya C/C++ via JNI untuk memuat JVM dan hal yang benar-benar perlu.
- **minSdk:** 29 (Android 10). **targetSdk:** versi terbaru yang disyaratkan Google Play (jangan diturunkan).
- **ABI:** hanya `arm64-v8a`.
- **Orientasi:** portrait saja.
- **Distribusi:** GitHub dan F-Droid dulu; kode dirancang **kompatibel Play Store sejak awal**.
- **Biaya:** utamakan solusi gratis atau berbiaya minimal. Jangan menambah layanan atau pustaka berbayar.

### Arsitektur
- JRE: build OpenJDK khusus Android (bionic), seperti yang dipakai PojavLauncher. **Bukan** JRE glibc standar, **bukan** proot untuk MVP.
- JVM dijalankan **di dalam proses** dengan `dlopen(libjvm.so)` + `JNI_CreateJavaVM`. **Dilarang** `exec` binary dari direktori data aplikasi (diblokir di Android 10+ dengan targetSdk modern).
- **Satu server = satu foreground service dengan `android:process` terpisah.** Jumlah slot dideklarasikan di manifest.
- Dua product flavor:
  - `foss`: JRE diunduh di dalam aplikasi.
  - `play`: **dilarang mengunduh kode eksekusi (`.so`, dex, JAR) dari luar Google Play.** JRE dikirim lewat Play Asset Delivery.
- Software server `.jar` (Paper, Fabric, Forge, dll) boleh diunduh bebas di kedua flavor (itu data untuk JVM).
- Penyimpanan data server: **internal** aplikasi. Impor/ekspor/backup lewat Storage Access Framework (SAF).
- Hasil Fase 0 (prototipe) boleh mengubah arsitektur, tetapi **setiap perubahan arsitektur wajib disetujui pemilik proyek** dan dicatat di `docs/decisions.md`.

### Antarmuka (rinciannya ada di `DESIGN.md`)
- Tema mengikuti sistem (terang/gelap). **Satu** warna aksen; warna lain hanya untuk status.
- Navigasi: bottom bar di level atas (Server, Aktivitas, Pengaturan); tab atas di dalam detail server.
- Kartu server menampilkan info lengkap. Wizard langkah demi langkah untuk server baru.
- Pengaturan tiga lapis: Dasar (di luar), Semua properti, Lanjutan (paling dalam).
- Dilarang menambah komponen visual dekoratif, animasi berlebih, atau gaya yang menyimpang dari `DESIGN.md`.

### Merek dan hukum
- **Dilarang** memakai kata "Minecraft" atau "Mojang" di nama aplikasi, package id, atau ikon. **Dilarang** memakai aset, logo, tekstur, atau ikon bergaya Minecraft (balok, pickaxe, creeper).
- Teks "bukan produk resmi Mojang/Microsoft" harus tetap ada di README dan deskripsi toko.

---

## 5. Aturan Kode

- **Semua teks yang dilihat pengguna wajib lewat resource string** (`values/strings.xml` Inggris, `values-in/strings.xml` Indonesia). Dilarang menulis string langsung di kode. Setiap string baru harus ada di kedua bahasa.
- Nama aplikasi hanya di satu tempat (`app_name`). Jangan menyebar nama kerja "Semine" di kode.
- **Jangan menambah dependensi baru** (Gradle, native, apa pun) tanpa persetujuan. Saat meminta persetujuan, sebutkan: nama, lisensi, ukuran, alasan, dan alternatif tanpa dependensi.
- Hanya dependensi berlisensi bebas yang kompatibel dengan F-Droid. Dilarang: Firebase, Google Play Services, Crashlytics, dan SDK analitik/iklan/pelacak.
- Ikuti struktur modul di `TASKS.md` (Fase A): `app`, `core`, `service`, `jvm`, `data`, `feature-*`. Jangan membuat modul atau pola arsitektur baru tanpa persetujuan.
- Jangan mengganti library yang sudah dipakai dengan yang lain tanpa persetujuan.
- Patuhi `minSdk 29`; jangan memakai API yang lebih baru tanpa pengecekan versi dan fallback.
- Jangan menaruh kunci, token, keystore, atau rahasia di repo. Jangan hardcode URL pribadi.
- Pakai coroutine/Flow untuk asinkron; jangan blok main thread. Tangani error dengan pesan yang bisa ditampilkan ke pengguna.
- Tulis unit test untuk logika murni (parser, pemetaan versi, dsb) bila menambah logika semacam itu.
- Jangan menghapus atau menimpa file milik pengguna/pemilik proyek tanpa konfirmasi.

---

## 6. Alur Kerja Wajib

Sebelum mengerjakan:
1. Baca file ini, `README.md`, `DESIGN.md`, dan bagian relevan di `TASKS.md`.
2. Pastikan tugas ada di `TASKS.md` dan fasenya sedang aktif. Jangan lompat fase. Jangan mengerjakan tugas fase berikutnya sebelum kriteria selesai fase sekarang terpenuhi. Pengecualian: pemilik proyek memerintahkan eksplisit.
3. Jika tugas ambigu atau menyentuh keputusan terbuka (bagian 9), **tanya dulu**.

Selama mengerjakan:
4. Kerjakan **sekecil mungkin** untuk memenuhi tugas. Satu tugas, satu perubahan yang fokus.
5. Jangan menyentuh file yang tidak terkait.

Setelah mengerjakan:
6. Centang tugas yang selesai di `TASKS.md` **hanya jika benar-benar sudah diverifikasi**.
7. Catat keputusan teknis penting di `docs/decisions.md` (tanggal, keputusan, alasan).
8. Laporkan ke pemilik proyek: apa yang diubah, file yang disentuh, apa yang sudah diuji, apa yang **belum** diuji, dan masalah/risiko yang ditemukan.

**Gate Fase 0:** jangan membangun fitur UI besar sebelum prototipe Fase 0 lolos (Paper stabil ≥ 1 jam di minimal 2 perangkat, stop bersih, dua server bersamaan). Jika gate gagal, berhenti dan laporkan.

---

## 7. Yang Selalu Butuh Persetujuan Eksplisit Pemilik Proyek

- Menambah/mengubah/menghapus entri di scope atau keputusan terkunci
- Menambah dependensi atau layanan eksternal
- Mengubah arsitektur (JVM, service, flavor, penyimpanan)
- Mengubah `minSdk`, `targetSdk`, ABI, atau orientasi
- Mengubah nama aplikasi, package id, ikon, atau warna aksen
- Memilih lisensi proyek
- Mengubah struktur modul atau struktur dokumen
- Melakukan refactor besar atau migrasi
- Menghapus file, riwayat git, atau membuat rilis/tag
- Mengirim apa pun ke layanan eksternal (publikasi, unggah, ajuan ke F-Droid/Play Store)

---

## 8. Pola Ucapan yang Benar

Contoh respons saat permintaan di luar scope:

> Ini tidak ada di scope AGENTS.md (fitur remote control masuk "di luar scope").
> Dampak: butuh server web di dalam aplikasi, izin jaringan tambahan, dan kerja keamanan (autentikasi) yang cukup besar.
> Pilihan: **A.** tambahkan ke scope dan langsung dikerjakan, **B.** tambahkan ke scope saja, **C.** jangan ditambahkan. Mau yang mana?

Contoh respons saat ada pertentangan teknis:

> Keputusan terkunci mensyaratkan JVM di dalam proses, tapi hasil uji menunjukkan masalah X. Saya belum mengubah apa pun. Opsi: ... Mohon keputusan Anda.

---

## 9. Keputusan Terbuka (belum diputuskan, JANGAN menebak, tanyakan)

- Nama aplikasi final (kerja: "Semine"; kandidat lain: "Serdroid")
- Package id / application id
- Warna aksen final (usulan: indigo `#5B6CF0` terang, `#8E9BFF` gelap)
- Lisensi proyek
- Apakah tab Aktivitas ada sejak MVP atau ditambah nanti
- Mode AMOLED dan tema tambahan
- Jumlah slot server bersamaan
- Sumber JRE final (pakai build pihak lain atau bangun sendiri)
- Detail integrasi playit.gg (Fase 4)
- Tipe foreground service untuk Play Store (kemungkinan `specialUse`)

Setelah satu hal diputuskan, **pindahkan dari daftar ini ke bagian 4** dan catat di `docs/decisions.md`.

---

## 10. Riwayat Perubahan File Ini

Setiap perubahan pada AGENTS.md wajib dicatat di sini: tanggal, apa yang berubah, siapa yang menyetujui.

| Tanggal | Perubahan | Disetujui oleh |
|---|---|---|
| 2026-11-01 | Versi awal | Pemilik proyek |
