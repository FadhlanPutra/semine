# Semine

> *Nama kerja, belum final.*

**Semine adalah aplikasi Android untuk menjalankan dan mengelola server Minecraft Java Edition langsung di HP, lengkap dengan antarmuka grafis, tanpa terminal.**

*English summary: Semine is an Android app that lets you run and manage Minecraft Java Edition servers locally on your phone, with a full graphical UI (no terminal needed): install Paper/Fabric/Forge, edit settings, manage mods, back up worlds, and monitor RAM and players.*

> **Status: tahap perencanaan.** Belum ada rilis. Dokumen ini menjelaskan tujuan dan arah proyek.

---

## Untuk apa proyek ini?

Menjalankan server Minecraft biasanya butuh PC atau VPS, dan mengatur server lewat terminal dan file konfigurasi. Hal itu menyulitkan pemula. Padahal HP modern (mid sampai high-end) cukup kuat untuk server kecil yang dipakai bermain bersama teman di jaringan yang sama.

Semine menghadirkan pengalaman "server sungguhan" di HP, tetapi semua dikelola lewat UI:

- Buat server baru lewat wizard, tanpa menyentuh command line.
- Ubah `server.properties`, pasang mod, ubah konfigurasi, dan impor server atau world dari satu aplikasi.
- Lihat konsol, pemakaian RAM, dan player online secara real-time.

## Cara kerja singkat

Android tidak menjalankan Java server biasa begitu saja. Semine membawa JRE khusus Android (build OpenJDK untuk Android seperti yang dipakai PojavLauncher) dan menjalankan server di dalam **foreground service** terpisah untuk tiap server. Aplikasi hanya berperan sebagai pengelola: menyiapkan Java, mengunduh software server, menjalankan, memantau, dan mencadangkan.

## Fitur yang direncanakan

**Inti**
- Banyak server sekaligus (beberapa slot proses)
- Start, stop, restart, dan deteksi crash
- Konsol: log langsung + input perintah
- Monitoring real-time: RAM server, jumlah dan daftar player online

**Instalasi server**
- Unduh otomatis lewat API resmi: Vanilla, Paper, Fabric, Forge, NeoForge
- Dukungan `.jar` manual
- Pemilihan versi Java otomatis sesuai versi Minecraft

**Pengelolaan**
- Editor `server.properties` (kelompok opsi umum, semua properti, dan lanjutan)
- Manajemen player: whitelist, operator, banned
- Penjelajah dan editor file server
- Mod/plugin: upload manual, pencarian dan instal dari Modrinth
- Impor/ekspor server dan world (zip)
- Backup manual dan terjadwal (zip ke lokasi pilihan pengguna)

**Jaringan**
- Utama: LAN/hotspot
- Belakangan: integrasi playit.gg agar bisa diakses dari internet

## Yang tidak (atau belum) didukung

- Minecraft Bedrock Edition
- Remote control dari perangkat lain
- CurseForge (ditunda, butuh API key)
- Grafik riwayat pemakaian (hanya nilai real-time)
- Perangkat x86 (awalnya arm64 saja)
- Modpack besar yang berat: HP punya batas RAM dan panas

## Persyaratan

- Android 10 atau lebih baru
- Prosesor arm64
- Disarankan perangkat mid sampai high-end dan RAM cukup (server kecil umumnya perlu 1,5 sampai 2 GB untuk server saja)
- Koneksi internet untuk mengunduh JRE, software server, dan mod

## Rencana distribusi

1. **GitHub** dan **F-Droid** dulu (JRE diunduh di dalam aplikasi)
2. **Play Store** bila terbukti diminati (JRE dikirim lewat Play Asset Delivery agar sesuai kebijakan Google)

## Teknologi

- Kotlin + Jetpack Compose (Material 3, tema dipangkas, satu warna aksen)
- Lapisan JNI/C untuk memuat JVM
- Room/DataStore untuk data, WorkManager untuk backup terjadwal
- Antarmuka dwibahasa (Indonesia dan Inggris), portrait

## Dokumen proyek

- [`DESIGN.md`](DESIGN.md): spesifikasi antarmuka (warna, tipografi, layar, komponen)
- [`TASKS.md`](TASKS.md): daftar pekerjaan dari persiapan sampai rilis, lengkap dengan gate dan risiko

## Risiko utama

Hal paling berisiko adalah menjalankan JVM yang stabil di Android (hambatan eksekusi, mekanisme pembunuh proses sistem, panas). Karena itu pekerjaan pertama adalah **prototipe**: Paper berjalan stabil minimal satu jam di dua perangkat berbeda. Jika gagal, rencana akan dievaluasi sebelum UI dibangun.

## Status dan kontribusi

Proyek pribadi yang direncanakan menjadi open source. Lisensi akan ditentukan sebelum rilis publik. Kontribusi dan saran akan dibuka setelah prototipe terbukti.

## Disclaimer

Semine **bukan produk resmi Minecraft** dan tidak berafiliasi dengan, didukung, atau disetujui oleh Mojang Studios atau Microsoft. "Minecraft" adalah merek dagang milik Mojang Studios.
