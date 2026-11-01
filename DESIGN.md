# DESIGN.md: UI Aplikasi Server Minecraft untuk Android

Spesifikasi desain antarmuka. Stack: Kotlin + Jetpack Compose + Material 3 (tema dipangkas). Hanya portrait HP.

## 1. Prinsip

1. **Konten dulu.** Hanya komponen yang diperlukan, hiasan minimal.
2. **Satu warna aksen.** Warna lain hanya untuk status.
3. **Ruang kosong lega, elevasi nyaris nol.** Pemisah pakai perbedaan warna permukaan, bukan bayangan.
4. **Opsi umum di luar, opsi lanjutan di dalam.** Semua opsi tetap bisa dijangkau dan dicari.
5. **Status tidak hanya lewat warna.** Selalu ada ikon dan label teks.
6. **Semua teks lewat resource** (Indonesia dan Inggris), tidak ada string di kode.

## 2. Tema

Mengikuti sistem (terang/gelap). Tidak memakai Material You dinamis, agar identitas warna tetap konsisten.

### Warna

| Token | Terang | Gelap |
|---|---|---|
| `background` | `#FAFAFB` | `#0F1013` |
| `surface` (kartu) | `#FFFFFF` | `#16181C` |
| `surfaceContainer` (input, chip) | `#F0F1F4` | `#1E2126` |
| `outline` (garis tipis) | `#E1E3E8` | `#2A2E35` |
| `onSurface` (teks utama) | `#15171A` | `#ECEDEF` |
| `onSurfaceVariant` (teks sekunder) | `#5F6672` | `#9AA1AD` |
| `accent` | `#5B6CF0` | `#8E9BFF` |
| `onAccent` | `#FFFFFF` | `#10131F` |

### Warna status

| Status | Warna | Ikon | Label |
|---|---|---|---|
| Berjalan | `#2FBF71` | titik terisi | Berjalan / Running |
| Memulai / Berhenti | `#E5A93C` | titik berdenyut | Memulai / Starting, Berhenti / Stopping |
| Mati | `#8A919E` | titik kosong | Mati / Stopped |
| Error / Crash | `#E5484D` | segitiga seru | Error |

Aksen sengaja indigo agar tidak bentrok dengan status hijau/kuning. Nilai warna bisa disesuaikan setelah dites di perangkat. Opsional nanti: mode AMOLED (`background` `#000000`).

### Tipografi

Font sistem (Roboto), tanpa font tambahan agar APK tetap kecil. Konsol memakai monospace sistem.

| Peran | Ukuran / berat |
|---|---|
| Judul halaman (header besar) | 28sp / SemiBold, mengecil jadi 20sp saat digulir |
| Judul kartu | 17sp / SemiBold |
| Isi | 15sp / Regular |
| Teks sekunder, label | 13sp / Regular, `onSurfaceVariant` |
| Konsol | 12sp / monospace |

### Bentuk dan jarak

- Skala jarak 4dp: 4, 8, 12, 16, 24, 32. Margin layar 16dp.
- Kartu: sudut 16dp, tanpa bayangan, `surface` di atas `background`, garis `outline` 1dp.
- Tombol utama: bentuk pil (sudut penuh), tinggi 48dp. Tombol sekunder: pil dengan garis.
- Chip dan input: sudut 12dp, isi `surfaceContainer`.
- Area sentuh minimal 48dp.

### Gerak

Singkat dan fungsional: transisi 150–250ms, tanpa animasi dekoratif. Satu-satunya animasi berulang adalah titik status "Memulai/Berhenti". Hormati pengaturan "hapus animasi" milik sistem.

## 3. Navigasi

```
Root (bottom bar): Server | Aktivitas | Pengaturan
 ├─ Server → Daftar server
 │    ├─ Server baru (wizard)
 │    └─ Detail server (bottom bar disembunyikan, tab atas)
 │         Konsol | Player | File | Mod | Backup | Pengaturan
 ├─ Aktivitas → unduhan, backup, tugas berjalan
 └─ Pengaturan → pengaturan aplikasi
```

- Bottom bar: 3 item, ikon + label, latar `surface` dengan garis atas tipis. Boleh dimulai dengan 2 item (Server, Pengaturan) bila Aktivitas belum perlu.
- Tab atas di detail server: scrollable, indikator garis aksen di bawah tab aktif.
- Tombol kembali sistem selalu naik satu level.

## 4. Layar

### 4.1 Daftar Server (beranda)

- Header besar "Server", tombol cari dan tambah (ikon) di kanan.
- Daftar **kartu server** (lihat 5.1). Urutan: yang berjalan di atas, lalu terakhir dipakai.
- Keadaan kosong: ilustrasi sederhana (ikon besar), teks singkat, tombol pil "Buat server pertama".

### 4.2 Wizard Server Baru (langkah demi langkah)

Indikator langkah di atas (4 titik), tombol Kembali / Lanjut di bawah.

1. **Jenis dan versi:** pilih software (Vanilla, Paper, Fabric, Forge, NeoForge, atau .jar sendiri), lalu versi Minecraft (daftar dengan pencarian; rilis stabil di atas, snapshot disembunyikan secara default).
2. **Nama:** nama server dan (opsional) impor dari zip.
3. **Sumber daya:** RAM (slider dengan saran default berdasarkan RAM perangkat, ada peringatan bila terlalu besar), versi Java otomatis sesuai versi MC.
4. **Ringkasan:** tampilkan semua pilihan, tombol "Buat server". Setelah itu progres unduh JRE dan server tampil di layar yang sama.

### 4.3 Detail Server

- **Header (di atas semua tab):** nama, software + versi, status (titik + label), satu tombol pil besar **Start / Stop / Restart**. Di bawahnya baris ringkas: RAM terpakai/dialokasikan, player online, uptime.
- Konten dipindah per tab:

| Tab | Isi |
|---|---|
| Konsol | Log monospace dengan penggulir otomatis (bisa dijeda), warna hanya untuk level (warn kuning, error merah), kolom input perintah menempel di atas keyboard, tombol salin/bersihkan. |
| Player | Online sekarang (nama, ping bila ada), whitelist, operator, banned. Aksi per player: op, kick, ban. |
| File | Penjelajah folder server, buka/edit file teks, upload lewat pemilih file sistem, hapus/ganti nama. |
| Mod | Daftar mod/plugin terpasang (aktif/nonaktif, hapus), tambah dari file, cari di Modrinth (filter loader dan versi). |
| Backup | Daftar backup, tombol "Backup sekarang", jadwal otomatis, pulihkan, ekspor zip ke lokasi pilihan. |
| Pengaturan | Lihat 4.4. |

### 4.4 Pengaturan Server (tiga lapis)

1. **Dasar (di luar):** nama, versi, RAM, MOTD, max player, gamemode, difficulty, whitelist, online-mode, tombol "Hapus server" di dasar halaman.
2. **Semua properti server (satu tingkat dalam):** seluruh `server.properties`, dikelompokkan seperti Aternos (Umum, Gameplay, Dunia, Jaringan, Lainnya). Toggle untuk boolean, stepper/angka untuk numerik, dropdown untuk pilihan. Ada kolom pencarian di atas.
3. **Lanjutan (paling dalam):** argumen JVM, versi JRE manual, port, RCON/Query, backup otomatis, perintah saat start, dan editor file mentah.

Setiap kelompok berupa kartu menyambung (baris-baris dalam satu kartu, dipisah garis tipis). Nilai yang diubah dari default diberi penanda kecil. Perubahan butuh restart ditandai ikon dan ada banner "Restart untuk menerapkan".

### 4.5 Aktivitas

Daftar tugas yang sedang berjalan atau baru selesai (unduhan JRE/server/mod, backup, impor). Tiap baris: judul, progres, aksi batal/coba lagi. Keadaan kosong: teks "Tidak ada aktivitas".

### 4.6 Pengaturan Aplikasi

Kartu menyambung berkelompok: Tampilan (tema, bahasa), Penyimpanan (lokasi backup, ruang terpakai), Performa (panduan baterai dan pengecualian optimasi), Jaringan (opsi playit.gg bila sudah ada), Tentang (versi, lisensi, tautan sumber). Dilengkapi pencarian pengaturan.

## 5. Komponen

### 5.1 Kartu server (info lengkap)

```
┌──────────────────────────────────────┐
│ Survival Kita                 ● Berjalan │
│ Paper 1.21.4 · Java 21                    │
│ ───────────────────────────────────── │
│ RAM   1,2 / 2 GB  [██████░░░░]            │
│ Player 3 / 10     Uptime 2 j 14 m         │
│ 192.168.1.12:25565            [ Stop ]    │
└──────────────────────────────────────┘
```

- Seluruh kartu bisa diketuk untuk membuka detail. Tombol Start/Stop di kartu bekerja tanpa membuka detail.
- Saat mati: baris RAM/player/uptime diganti "Terakhir berjalan: ..." dan ukuran folder server.
- Alamat IP lokal bisa disalin dengan ketuk lama.

### 5.2 Baris pengaturan

Judul + deskripsi singkat (opsional), kontrol di kanan (toggle, nilai + panah, atau stepper). Tinggi minimal 56dp. Ketuk di seluruh baris.

### 5.3 Lembar bawah (bottom sheet) dan dialog

Konfirmasi tindakan destruktif (hapus server, hapus backup, hentikan paksa) memakai dialog dengan tombol destruktif berwarna error. Pilihan berisi daftar (versi, software) memakai bottom sheet.

### 5.4 Keadaan umum

- **Loading:** kerangka (skeleton) abu-abu untuk daftar, indikator linear untuk unduhan.
- **Kosong:** ikon + satu kalimat + satu aksi.
- **Error:** ikon segitiga, penjelasan singkat, tombol "Coba lagi", dan tautan "Lihat log" bila relevan.

## 6. Notifikasi

Satu notifikasi foreground per server yang berjalan: nama server, status, RAM, jumlah player, dengan aksi **Stop** dan **Buka konsol**. Saluran notifikasi terpisah untuk backup selesai dan error.

## 7. Lokalisasi

- Bahasa: Indonesia dan Inggris (`values/strings.xml` dan `values-in/strings.xml`).
- Pengganti bahasa per aplikasi via `AppCompatDelegate.setApplicationLocales` (dukungan Android 10+), dengan opsi "Ikuti sistem".
- Tanpa menggabungkan kalimat dari potongan string, gunakan placeholder dan plurals.

## 8. Aksesibilitas

- Kontras teks minimal 4.5:1 di kedua tema.
- Semua ikon punya `contentDescription`.
- Ukuran teks mengikuti pengaturan sistem; layout tidak boleh patah pada skala 130%.
- Status tidak mengandalkan warna saja.

## 9. Referensi

- **SpotiFLAC Mobile:** kartu pengaturan menyambung, header besar yang rendah, bottom bar tipis, pencarian dalam pengaturan.
- **Aternos:** pengelompokan opsi server dengan kontrol sederhana, dan editor file untuk konfigurasi lanjutan.
- **Spotify / Apple:** kesederhanaan, tipografi bersih, sedikit hiasan.

## 10. Keputusan terbuka

- Warna aksen final (indigo di atas hanya usulan).
- Apakah tab Aktivitas ada sejak MVP atau ditambah nanti.
- Mode AMOLED dan tema lain (opsional, setelah MVP).
- Jumlah slot server bersamaan di UI (mengikuti jumlah proses service yang dideklarasikan di manifest).
