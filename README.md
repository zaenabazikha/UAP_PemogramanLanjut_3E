# Sistem Informasi Akademik Mahasiswa 2025

**Sistem Informasi Akademik** adalah aplikasi manajemen data akademik berbasis desktop yang dikembangkan sebagai tugas mata kuliah **Pemrograman Lanjut**.

Aplikasi ini dibangun menggunakan **Java Swing** dengan antarmuka yang modern (*Nimbus Look & Feel*), mengusung tema warna identitas UMM (Merah & Putih), serta dilengkapi dengan fitur CRUD lengkap dan sistem pelaporan sederhana.

---

## Fitur Utama

Aplikasi ini memiliki fitur unggulan sebagai berikut:

### 1. Dashboard Statistik
* Tampilan ringkasan yang bersih.
* Kartu statistik *real-time* untuk memantau **Total Mahasiswa** dan informasi **Tahun Ajaran**.

### 2. Manajemen Data (CRUD)
* **Input Data:** Formulir input yang rapi (GridBagLayout) untuk NIM, Nama, Jurusan, Semester, dan Kelas.
* **Tabel Data:** Menampilkan daftar mahasiswa dengan desain baris selang-seling (*Zebra Stripes*) agar nyaman dibaca.
* **Edit Data:** Menggunakan *Pop-up Dialog* (`JOptionPane`) untuk mengubah data tanpa perlu menghapus dan input ulang.
* **Hapus Data:** Fitur penghapusan dengan konfirmasi keamanan (*Confirmation Dialog*).

### 3. Laporan (Report View)
* **Generate Laporan:** Fitur untuk mencetak ringkasan data mahasiswa dalam format teks yang rapi.
* **View Only:** Area laporan bersifat *read-only* untuk menjaga integritas data tampilan.

### 4. Sistem Penyimpanan (File Handling)
* **Database CSV:** Data mahasiswa disimpan permanen di file `data_mahasiswa_kelas.txt`.
* **Auto-Save/Load:** Aplikasi otomatis membaca data saat dibuka dan menyimpan perubahan (Simpan/Edit/Hapus) secara instan.

### 5. Antarmuka Modern (UI/UX)
* Menggunakan **Gradient Panel** (Merah ke Putih) untuk latar belakang yang estetik.
* Header tabel dikustomisasi dengan warna merah maroon khas UMM.

---

## Penjelasan Teknis Kode

Aplikasi ini menggunakan struktur **Single-Class Application** yang terorganisir rapi dalam package `org.example`.

### A. Model Data (`static class Mahasiswa`)
Merepresentasikan objek mahasiswa dengan konsep **Encapsulation**. Dilengkapi dengan method `toCSV()` untuk mempermudah konversi data menjadi format teks dipisahkan koma (*Comma Separated Values*).

### B. Tampilan & Logika (`class AkademikMahasiswa`)
Kelas utama turunan `JFrame` yang menangani:
1.  **Manajemen Layout:** Menggunakan `JTabbedPane` untuk navigasi antar halaman (Dashboard, Input, Data, Laporan).
2.  **Event Handling Eksplisit:** Menggunakan Lambda Expression dengan tipe data eksplisit `(ActionEvent e)` untuk memastikan library `java.awt.event` terbaca aktif oleh IDE.
3.  **Logika CRUD:** Logika validasi input (NIM tidak boleh kosong) dan mekanisme update data via dialog.
4.  **File Handling (I/O):** Menggunakan `PrintWriter` dan `BufferedReader` untuk operasi baca/tulis file teks yang ringan dan cepat.

---

## Cara Menjalankan Program

Anda dapat menjalankan aplikasi ini menggunakan salah satu dari metode berikut:

### Metode 1: Menggunakan IDE (Disarankan)
Cara termudah jika Anda ingin melihat atau mengedit kode.
1.  Buka **IntelliJ IDEA**, **NetBeans**, atau **VS Code**.
2.  Pastikan struktur package adalah `org.example`.
3.  Buka file `src/org/example/AkademikMahasiswa.java`.
4.  Klik tombol **Run** (▶) atau tekan `Shift + F10`.

### Metode 2: Menggunakan Terminal / CMD (Manual)
Cocok untuk menjalankan program tanpa membuka aplikasi berat.
1.  Buka Command Prompt (CMD) atau Terminal.
2.  Arahkan ke direktori `src` proyek:
    ```bash
    cd path/ke/folder/project/src
    ```
3.  Compile kode program:
    ```bash
    javac org/example/AkademikMahasiswa.java
    ```
4.  Jalankan program:
    ```bash
    java org.example.AkademikMahasiswa
    ```

> **Catatan:** File `data_mahasiswa_kelas.txt` akan dibuat otomatis oleh aplikasi di folder root proyek saat pertama kali data disimpan.

---
## Anggota Kelompok

**Tugas Praktikum / UAP Pemrograman 2025**

| Nama Mahasiswa              | NIM             |
|:----------------------------|:----------------|
| Salzabilla Aurelia Maheswari| 202410370110283 |
| Waode Zaenab Azikha Sagala  | 202410370110308 |