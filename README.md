# Aplikasi Penyewaan Alat Hiking Multi-User & Auto-Notification System 🏕️⚡

Aplikasi desktop berbasis **Java Swing** dan **MySQL** yang dirancang untuk mengelola bisnis penyewaan peralatan hiking. Aplikasi ini mendukung 2 peran pengguna (**Pemilik Toko** dan **Penyewa**), kalkulasi denda keterlambatan presisi berbasis jam, serta sistem notifikasi pengingat tenggat pengembalian otomatis.

## 📌 Fitur Utama (Berdasarkan Feedback Dosen)

### 1. Hak Akses Multi-User (Role-Based Login)

* **Pemilik Toko (Admin / Owner):**

  * Mengelola inventaris alat (Tambah, Edit, Hapus, Atur Stok).

  * Mengatur harga sewa per hari dan tarif denda keterlambatan per jam.

  * Memantau status seluruh transaksi sewa (`DIPINJAM`, `TERLAMBAT`, `SELESAI`).

  * Menerima notifikasi otomatis terkait penyewaan baru dan pengembalian terlambat.

  * Melihat laporan pendapatan sewa & akumulasi denda.

* **Penyewa (Customer):**

  * Mendaftar/Login ke akun penyewa.

  * Melihat katalog perlengkapan hiking yang tersedia secara *real-time*.

  * Melakukan simulasi sewa, memilih durasi sewa, dan memasukkan ke keranjang.

  * Menerima **notifikasi pengingat pengembalian** di dalam aplikasi.

### 2. Kalkulasi Denda Keterlambatan Presisi Per Jam ⏱️

* Batas pengembalian dicatat dengan presisi tanggal dan jam (`expected_return_time`).

* Jika pengembalian dilakukan melewati jadwal, sistem secara otomatis menghitung selisih keterlambatan dalam jam.

* **Rumus Denda:**

  $$
  \text{Total Denda} = \text{Selisih Keterlambatan (Jam)} \times \text{Tarif Denda per Jam}
  $$

* Total tagihan pengembalian = `Sisa Biaya Sewa + Total Denda`.

### 3. Sistem Notifikasi Pengingat Pengembalian Multi-Channel 🔔

Sistem *Background Scheduler/Timer* akan memeriksa tenggat waktu pengembalian dan mengirimkan notifikasi ke **Penyewa** dan **Pemilik Toko**:

* **Notifikasi H-1 Hari:** Pengingat awal sebelum hari pengembalian.

* **Notifikasi H-30 Menit:** Pengingat persiapan pengembalian barang.

* **Notifikasi H-10 Menit:** Peringatan kritis mendekati batas waktu agar tidak terkena denda jam.

## 🏗️ Arsitektur & Teknologi

Aplikasi dibangun menggunakan *Layered Architecture* modular:

* **Model / Entity:** `User`, `Equipment`, `Rental`, `RentalDetail`, `Notification`.

* **Repository / DAO:** `DatabaseUtil`, `UserRepository`, `EquipmentRepository`, `RentalRepository`, `NotificationRepository`.

* **Service Layer (Logika Bisnis & Background Thread):**

  * `PenaltyCalculatorService`: Menghitung denda jam secara presisi.

  * `NotificationSchedulerService`: Thread otomatis pengirim notifikasi H-1 hari, H-30 menit, dan H-10 menit.

* **View / GUI:**

  * `LoginFrame.java` & `RegisterFrame.java`

  * `OwnerMainFrame.java` (Dashboard Pemilik Toko)

  * `RenterMainFrame.java` (Katalog & Dashboard Penyewa)

  * `CartFrame.java`, `ReturnFrame.java`, `NotificationDialog.java`

## 🗄️ Rancangan Database (`sewa_hiking_db`)

### 1. Tabel `users`

| Kolom | Tipe Data | Keterangan | 
| ----- | ----- | ----- | 
| `id` | INT (PK, AI) | ID Pengguna | 
| `username` | VARCHAR(50) | Username Login | 
| `password` | VARCHAR(255) | Password | 
| `full_name` | VARCHAR(100) | Nama Lengkap | 
| `phone` | VARCHAR(20) | Nomor Telepon/WA | 
| `role` | ENUM | `'PEMILIK'` atau `'PENYEWA'` | 

### 2. Tabel `equipments`

| Kolom | Tipe Data | Keterangan | 
| ----- | ----- | ----- | 
| `id` | INT (PK, AI) | ID Peralatan | 
| `name` | VARCHAR(100) | Nama Alat (ex: Tenda Kapasitas 4) | 
| `category` | VARCHAR(50) | Kategori (Tenda, Carrier, Masak, dll) | 
| `price_per_day` | DECIMAL(15,2) | Harga sewa per hari | 
| `late_fee_per_hour` | DECIMAL(15,2) | Denda keterlambatan per jam | 
| `stock` | INT | Stok tersedia | 

### 3. Tabel `rentals`

| Kolom | Tipe Data | Keterangan | 
| ----- | ----- | ----- | 
| `id` | INT (PK, AI) | ID Transaksi Sewa | 
| `renter_id` | INT (FK -> users) | ID Penyewa | 
| `start_time` | DATETIME | Waktu Mulai Sewa | 
| `expected_return_time` | DATETIME | Batas Jam Pengembalian | 
| `actual_return_time` | DATETIME | Jam Pengembalian Riil (Bisa NULL) | 
| `total_price` | DECIMAL(15,2) | Biaya Pokok Sewa | 
| `late_fee_total` | DECIMAL(15,2) | Total Denda Terkumpul | 
| `status` | ENUM | `'DIPINJAM'`, `'SELESAI'`, `'TERLAMBAT'` | 

### 4. Tabel `rental_details`

| Kolom | Tipe Data | Keterangan | 
| ----- | ----- | ----- | 
| `id` | INT (PK, AI) | ID Detail | 
| `rental_id` | INT (FK -> rentals) | ID Transaksi | 
| `equipment_id` | INT (FK -> equipments) | ID Alat | 
| `quantity` | INT | Jumlah Disewa | 
| `subtotal` | DECIMAL(15,2) | Subtotal Biaya | 

### 5. Tabel `notifications`

| Kolom | Tipe Data | Keterangan | 
| ----- | ----- | ----- | 
| `id` | INT (PK, AI) | ID Notifikasi | 
| `user_id` | INT (FK -> users) | Penerima Notifikasi | 
| `rental_id` | INT (FK -> rentals) | Transaksi Terkait | 
| `type` | VARCHAR(20) | `'H-1_DAY'`, `'H-30_MIN'`, `'H-10_MIN'`, `'OVERDUE'` | 
| `message` | TEXT | Isi Pesan Notifikasi | 
| `is_read` | BOOLEAN | Status dibaca | 
| `created_at` | TIMESTAMP | Waktu Notifikasi Dibuat | 

## 🚀 Panduan Eksekusi & Pengujian

1. Pastikan **MySQL** aktif di XAMPP/WAMP.

2. Buat database `sewa_hiking_db` dan jalankan script SQL pembuat tabel.

3. Buka proyek di NetBeans IDE / IntelliJ IDEA.

4. Sesuaikan kredensial pada `DatabaseUtil.java`.

5. Jalankan `LoginFrame.java` untuk menguji dua jenis akun:

   * **Login Pemilik:** Untuk input stok barang, atur denda per jam, dan pantau pengembalian.

   * **Login Penyewa:** Untuk sewa barang dan mengecek notifikasi tenggat waktu.