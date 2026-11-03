# TASKS.md: Daftar Pekerjaan dari Awal sampai Rilis

Checklist lengkap proyek aplikasi server Minecraft untuk Android. Urutan mengikuti fase di rencana. Tiap fase punya **kriteria selesai**; jangan lanjut ke fase berikutnya sebelum terpenuhi. Referensi: rencana (arsitektur, risiko) dan `DESIGN.md` (UI).

Legenda: `[ ]` belum, `[x]` selesai. Tanda **GATE** berarti titik go/no-go.

---

## Fase A: Persiapan

- [ ] Tentukan nama aplikasi dan package id (hindari kata "Minecraft"/"Mojang" dan logo mereka)
- [ ] Cek ketersediaan nama di GitHub, F-Droid, Play Store
- [x] Buat repo GitHub (publik atau privat dulu) + `README.md` awal
- [ ] Pilih lisensi proyek (mis. GPLv3 atau Apache-2.0; cek kompatibilitas dengan lisensi JRE dan dependensi)
- [ ] Setup Android Studio, JDK, NDK, CMake
- [ ] Siapkan 2–3 perangkat uji: minimal satu Android 10–11, satu Android 12+, usahakan beda merek (Xiaomi/Samsung/Oppo)
- [x] Buat proyek Gradle: Kotlin + Compose + Material 3, `minSdk 29`, `targetSdk` terbaru yang disyaratkan Play
- [x] Konfigurasi ABI filter `arm64-v8a` saja
- [x] Buat dua product flavor: `foss` dan `play`
- [x] Setup CI GitHub Actions (build debug, lint, unit test)
- [x] Setup struktur modul: `app`, `core`, `service`, `jvm`, `data` (selesai); `feature-*` dibuat saat fitur pertama dikerjakan di Fase 1
- [x] Setup lokalisasi sejak awal (`values/` dan `values-in/`), aturan: tidak ada string di kode
- [ ] Setup logging dan crash report lokal (tanpa layanan berbayar)

**Selesai bila:** proyek bisa dibuild di CI untuk kedua flavor dan jalan di perangkat uji (layar kosong).

---

## Fase 0: Prototipe risiko (GATE utama)

Tujuan: membuktikan Java server bisa jalan stabil. Jika gagal, rencana diubah sebelum menulis UI.

- [ ] Dapatkan build JRE bionic (aarch64) untuk Java 21 (dari proyek PojavLauncher atau build sendiri); catat sumber dan lisensinya
- [ ] Kemas JRE sementara (di assets) dan ekstrak ke penyimpanan internal saat pertama jalan
- [ ] Tulis loader JNI (C/C++): `dlopen(libjvm.so)`, `JNI_CreateJavaVM`, jalankan `main` dari `server.jar`
- [ ] Atur variabel lingkungan dan jalur JRE (`java.home`, `LD_LIBRARY_PATH`/dependensi `.so` yang diperlukan)
- [ ] Deklarasikan 2 service dengan `android:process` berbeda; jalankan satu JVM di tiap service
- [ ] Arahkan stdout/stderr ke pipe, baca dan tampilkan di layar sederhana
- [ ] Kirim perintah ke stdin server (`stop`, `say`)
- [ ] Implementasi stop: kirim `stop` dulu, lalu matikan proses bila macet (timeout)
- [ ] Foreground service + notifikasi dasar
- [ ] Jalankan Paper 1.21.x sampai pesan `Done!`, lalu sambungkan dari klien Minecraft di jaringan yang sama
- [ ] Uji dua server bersamaan (proses berbeda, port berbeda)
- [ ] Uji ketahanan: jalankan 1 jam, layar mati, aplikasi di latar belakang, di Android 12+ dan merek agresif
- [ ] Ukur: RAM, suhu, waktu start, ukuran APK (dengan dan tanpa JRE)
- [ ] Uji `targetSdk` modern: pastikan tidak ada `exec` dari direktori data yang diblokir
- [ ] Dokumentasikan hasil dan masalah di `docs/prototype-notes.md`

**GATE:** server jalan stabil ≥ 1 jam di minimal 2 perangkat, stop bersih, dua server bersamaan jalan. Jika tidak, evaluasi opsi cadangan (proot, proses anak dengan launcher di `jniLibs`).

---

## Fase 1: MVP (satu server, Vanilla dan Paper)

### Fondasi
- [ ] Implementasi `JreManager`: unduh JRE per versi (flavor `foss`), verifikasi checksum, ekstrak, simpan versi terpasang
- [ ] Pemetaan versi MC ke versi Java (8/16/17/21/25) otomatis
- [ ] Model data: `Server`, `ServerConfig`, `ServerStatus` + Room/DataStore
- [ ] Tema Compose sesuai `DESIGN.md` (warna, tipografi, bentuk, terang/gelap)
- [ ] Komponen dasar: tombol pil, kartu, baris pengaturan, dialog, bottom sheet, skeleton, state kosong/error
- [ ] Navigasi: bottom bar root + tab atas detail

### Instalasi server
- [ ] `Installer` Vanilla: ambil manifest versi Mojang, unduh `server.jar`, verifikasi hash
- [ ] `Installer` Paper: API PaperMC (versi dan build)
- [ ] Terima EULA lewat dialog (tulis `eula.txt`)
- [ ] Wizard server baru (4 langkah) dengan progres unduhan JRE + server
- [ ] Penanganan error: tidak ada internet, ruang kurang, unduhan terputus (lanjutkan/coba lagi)

### Menjalankan server
- [ ] Service per slot server sesuai prototipe, dikelola oleh `ServerController`
- [ ] Status: Mati, Memulai, Berjalan, Berhenti, Error (deteksi dari log `Done!` dan keluar proses)
- [ ] Start/Stop/Restart dari UI dan dari notifikasi
- [ ] Deteksi crash, simpan alasan, tawarkan "Lihat log"
- [ ] Argumen JVM default aman (heap berdasar RAM yang dipilih)

### Konsol
- [ ] Tampilan log monospace, penggulir otomatis (bisa dijeda), batasi buffer (mis. 5.000 baris)
- [ ] Pewarnaan level log (INFO/WARN/ERROR)
- [ ] Input perintah + riwayat perintah
- [ ] Salin dan bersihkan log; simpan log ke file per sesi

### Monitoring dan pengaturan
- [ ] RAM proses server dari `/proc/<pid>/status`, tampil real-time
- [ ] Jumlah player dan daftar online dari parsing log (join/leave) + `list`
- [ ] Pengaturan dasar: editor `server.properties` (nama, MOTD, max player, gamemode, difficulty, online-mode, port)
- [ ] Banner "restart untuk menerapkan"
- [ ] IP lokal ditampilkan dan bisa disalin
- [ ] Layar panduan baterai/pengecualian optimasi + izin notifikasi
- [ ] Layar daftar server dengan kartu info lengkap

### Kualitas
- [ ] Unit test: parser log, pemetaan versi Java, parser properties
- [ ] Uji manual di semua perangkat uji

**Selesai bila:** pengguna baru bisa membuat server Paper dari nol, menjalankan, bergabung dari klien, mengubah properties, dan menghentikannya, tanpa menyentuh terminal.

---

## Fase 2: Multi-server, Fabric, file, backup

- [ ] Multi-server: daftar, slot service, penanganan batas slot dan konflik port
- [ ] Hapus, duplikat, dan ganti nama server
- [ ] `Installer` Fabric (meta API: loader + installer)
- [ ] Tab **Player**: online, whitelist, operator, banned (baca/tulis file JSON dan/atau lewat perintah)
- [ ] Tab **File**: penjelajah folder, lihat/edit file teks, ganti nama, hapus, buat folder
- [ ] Upload file/mod lewat Storage Access Framework
- [ ] Tab **Mod**: daftar mod/plugin terpasang, aktif/nonaktif (ganti ekstensi), hapus, tambah dari file
- [ ] Impor server dari zip (deteksi jenis dan versi, validasi struktur)
- [ ] Impor/ekspor world (saves) sebagai zip
- [ ] `BackupManager`: kompres ke zip, `save-off`/`save-all`/`save-on` untuk server hidup
- [ ] Backup manual + daftar backup + pulihkan + hapus
- [ ] Backup terjadwal (WorkManager), retensi (simpan N backup terakhir)
- [ ] Pilih lokasi backup via SAF
- [ ] Layar **Aktivitas** (unduhan, backup, impor)
- [ ] Pengaturan server lapis 2: semua `server.properties` dikelompokkan + pencarian
- [ ] Uji: backup besar, restore, dua server jalan sambil backup

**Selesai bila:** bisa mengelola beberapa server, memasang mod Fabric manual, dan memulihkan dari backup tanpa kehilangan data.

---

## Fase 3: Forge/NeoForge, Modrinth, konfigurasi

- [ ] `Installer` Forge: ambil versi dari promotions, jalankan installer (`--installServer`) lewat JVM, tangani output dan kegagalan
- [ ] `Installer` NeoForge (alur serupa)
- [ ] Dukungan Quilt (opsional) dan Purpur/Spigot bila mudah
- [ ] Dukungan `.jar` manual (pilih jenis dan Java)
- [ ] `ModrinthClient`: pencarian, filter loader dan versi MC, halaman detail, instal satu ketukan
- [ ] Penanganan dependensi mod yang wajib (peringatan atau instal otomatis)
- [ ] Deteksi pembaruan mod (opsional)
- [ ] Editor konfigurasi mod/plugin (file di `config/`) dengan editor teks dan penyorotan sederhana
- [ ] Manajemen world: ganti world aktif, buat world baru, atur seed/tipe
- [ ] Pengaturan server lapis 3 (Lanjutan): argumen JVM, versi JRE manual, RCON/Query, perintah saat start
- [ ] Uji Forge/NeoForge di beberapa versi dan perangkat

**Selesai bila:** Forge/NeoForge dan Modrinth bekerja untuk versi MC populer; kegagalan installer menampilkan pesan yang jelas.

---

## Fase 4: Jaringan, Play Store, penyempurnaan

### Jaringan
- [ ] Tampilkan alamat LAN dan panduan hotspot
- [ ] Integrasi playit.gg: unduh dan kelola agent sebagai proses terkelola, tampilkan alamat publik, status koneksi
- [ ] Uji dari luar jaringan lokal

### Play Store
- [ ] Siapkan Play Asset Delivery untuk JRE di flavor `play`; hapus unduhan `.so` di flavor ini
- [ ] Pastikan tidak ada unduhan kode eksekusi di build `play`
- [ ] Putuskan tipe foreground service (`specialUse`) dan siapkan justifikasi + video demo
- [ ] Tulis kebijakan privasi, formulir Data Safety, Content Rating
- [ ] Pastikan nama, ikon, dan deskripsi tidak melanggar merek dagang Minecraft/Mojang
- [ ] Uji build rilis `.aab` di track internal

### Polesan
- [ ] Mode AMOLED (opsional)
- [ ] Pencarian dalam Pengaturan Aplikasi
- [ ] Peringatan suhu/baterai dan saran RAM berdasarkan perangkat
- [ ] Panduan khusus per merek (Xiaomi, Oppo/Realme, Samsung, Vivo)
- [ ] Audit aksesibilitas (kontras, ukuran teks 130%, deskripsi ikon)
- [ ] Profiling performa UI (log deras, daftar panjang)

**Selesai bila:** build `play` lolos pengujian internal dan playit.gg bisa dipakai dari dalam aplikasi.

---

## Fase 5: Rilis dan setelahnya

### Rilis GitHub/F-Droid
- [ ] Tanda tangan rilis (keystore aman, cadangan terpisah)
- [ ] Rilis di GitHub: APK arm64 per versi, catatan rilis, checksum
- [ ] Siapkan metadata F-Droid (`fastlane/metadata`, deskripsi, tangkapan layar, changelog)
- [ ] Pastikan build reproducible/bebas dependensi nonbebas untuk F-Droid
- [ ] Ajukan merge request ke `fdroiddata`
- [ ] Siapkan JRE rilis sendiri (GitHub Actions) agar tidak bergantung pihak lain

### Dokumentasi dan komunitas
- [ ] README lengkap: fitur, tangkapan layar, instalasi, FAQ (baterai, RAM, panas)
- [ ] `CONTRIBUTING.md`, templat issue/PR, kode etik
- [ ] Panduan pengguna singkat (membuat server, mod, backup, playit.gg)
- [ ] Halaman proyek di situs pribadi (opsional)

### Pasca-rilis
- [ ] Kumpulkan umpan balik dan bug dari issue
- [ ] Dukungan versi Minecraft baru (rutin: pemetaan Java, API installer)
- [ ] Evaluasi permintaan: Play Store publik, x86, CurseForge, Bedrock, remote control, grafik riwayat
- [ ] Putuskan monetisasi hanya jika sudah ramai (donasi/Ko-fi, atau fitur opsional), tanpa mengganggu versi FOSS

---

## Pekerjaan lintas fase (dikerjakan terus)

- [ ] Uji di perangkat nyata tiap akhir fase
- [ ] Jaga semua string lewat resource (ID dan EN)
- [ ] Jaga ukuran APK dan memantau per rilis
- [ ] Perbarui dokumen: rencana, `DESIGN.md`, `TASKS.md`
- [ ] Catat keputusan teknis penting di `docs/decisions.md`
- [ ] Cadangkan keystore, repo, dan hasil build

---

## Risiko yang dipantau

| Risiko | Gate / mitigasi |
|---|---|
| JVM tidak stabil di Android | Fase 0 (GATE) |
| Ketergantungan build JRE pihak ketiga | Bangun JRE sendiri di Fase 5 |
| Forge/NeoForge installer gagal | Fase 3, pesan error jelas, fallback .jar manual |
| Play Store menolak tipe foreground service | Fase 4, `specialUse` + justifikasi; tetap rilis via GitHub/F-Droid |
| Panas dan baterai | Peringatan UI, batas RAM default, saran per perangkat |
| Service dimatikan OEM | Foreground service + panduan per merek |
| Pelanggaran merek dagang | Nama, ikon, deskripsi netral sejak Fase A |
