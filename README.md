# proyek-akhir-DewaPeBO

# Aplikasi Manajemen Toko Sewa Alat Hiking 🏕️

Aplikasi desktop berbasis **Java Swing** dan **MySQL** untuk membantu operasional penyewaan peralatan hiking. Aplikasi ini didesain menggunakan arsitektur berlapis (Entity, Repository, View) untuk memudahkan petugas toko dalam mengelola ketersediaan alat, mencatat penyewaan, serta memproses pengembalian barang.

## 📌 Fitur Utama

1. **Manajemen Inventaris Alat (CRUD)**
   - Menambahkan, mengubah, menghapus, dan menampilkan daftar perlengkapan hiking (Tenda, Carrier, Sepatu, dll).
   - Menetapkan harga sewa per hari dan melacak jumlah stok fisik secara *real-time*.

2. **Sistem Penyewaan (Check-Out)**
   - Petugas dapat mencatat identitas penyewa (Nama, No KTP, No HP).
   - Memasukkan alat ke dalam "Keranjang Sewa" beserta kuantitasnya.
   - Menentukan durasi sewa (berapa hari).
   - Sistem secara otomatis mengkalkulasi **Total Biaya** (Harga per Hari × Kuantitas × Durasi).
   - Stok barang otomatis berkurang saat transaksi sewa berhasil disimpan.

3. **Sistem Pengembalian (Check-In)**
   - Petugas dapat memproses pengembalian berdasarkan ID Transaksi.
   - Mengubah status transaksi menjadi "Selesai".
   - Stok barang otomatis kembali bertambah ke dalam sistem.

4. **Histori Transaksi**
   - Menampilkan riwayat seluruh transaksi penyewaan (Aktif / Selesai).

---

## 🏗️ Arsitektur & Teknologi

Proyek ini dibangun dengan pendekatan *Layered Architecture*:
- **Model / Entity:** Representasi objek data (`Equipment`, `Rental`, `CartItem`).
- **Repository:** Kelas yang mengurus logika *Database* & SQL (`DatabaseUtil`, `EquipmentRepository`, `RentalRepository`).
- **View:** Antarmuka pengguna grafis (GUI) yang dibuat dengan NetBeans GUI Builder (`MainFrame`, `RentalFrame`, `ReturnFrame`, `HistoryFrame`).

**Teknologi yang digunakan:**
- Java SE (Minimal JDK 8+)
- Java Swing & AWT (GUI)
- MySQL / MariaDB
- JDBC (Java Database Connectivity)

---

## 🗄️ Rancangan Struktur Database

Buat database dengan nama `sewa_hiking_db`. Berikut adalah tabel yang dibutuhkan:

### 1. `equipments` (Data Alat)
| Kolom | Tipe Data | Keterangan |
| :--- | :--- | :--- |
| `id` | INT (PK, AI) | ID Alat |
| `name` | VARCHAR | Nama Alat (ex: Tenda Kapasitas 4) |
| `category` | VARCHAR | Kategori (ex: Tenda, Tas, Masak) |
| `price_per_day` | DOUBLE | Harga sewa per hari |
| `stock` | INT | Jumlah stok tersedia |

### 2. `temporary_cart` (Keranjang Sewa Sementara)
| Kolom | Tipe Data | Keterangan |
| :--- | :--- | :--- |
| `id` | INT (PK, AI) | ID Keranjang |
| `equipment_id` | INT (FK) | Relasi ke `equipments` |
| `quantity` | INT | Jumlah yang mau disewa |

### 3. `rentals` (Riwayat Transaksi Sewa)
| Kolom | Tipe Data | Keterangan |
| :--- | :--- | :--- |
| `id` | INT (PK, AI) | ID Transaksi Sewa |
| `customer_name` | VARCHAR | Nama Penyewa |
| `customer_ktp` | VARCHAR | No KTP / Identitas |
| `duration_days` | INT | Durasi penyewaan (hari) |
| `total_price` | DOUBLE | Total biaya sewa |
| `status` | VARCHAR | Status: `DIPINJAM` atau `SELESAI` |
| `timestamp` | TIMESTAMP | Waktu transaksi dilakukan |

---