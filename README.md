# AgendaKuliah - Aplikasi Manajemen Tugas Kuliah

**AgendaKuliah** adalah aplikasi Android sederhana yang dirancang untuk membantu mahasiswa dalam mengelola tugas-tugas perkuliahan mereka. Aplikasi ini memungkinkan pengguna untuk mencatat tugas, mengatur prioritas, memantau tenggat waktu, dan menandai tugas yang telah selesai.

## Fitur Utama

*   **Sistem Autentikasi**: Fitur Login dan Register untuk keamanan data pengguna secara personal.
*   **Manajemen Tugas**: Tambah, edit, dan hapus tugas kuliah dengan mudah.
*   **Prioritas & Deadline**: Atur tingkat kepentingan tugas (Tinggi, Sedang, Rendah) dan catat tanggal pengumpulannya.
*   **Daftar Tugas Aktif & Selesai**: Pemisahan tab antara tugas yang masih berjalan dan tugas yang sudah tuntas untuk kemudahan pemantauan.
*   **Mode Tampilan Fleksibel**: Tombol melayang (Floating Action Button) untuk berganti antara tampilan **Daftar (List)** dan **Grid (Kotak)**.
*   **Profil Pengguna**: Menampilkan ringkasan statistik tugas (jumlah tugas aktif vs selesai).

## Teknologi yang Digunakan

*   **Bahasa Pemrograman**: Java
*   **Arsitektur**: MVVM (Model-View-ViewModel) dengan LiveData.
*   **Penyimpanan Data**: SQLite (menggunakan `DatabaseHelper`).
*   **View Binding**: Untuk interaksi kode dengan layout yang lebih aman dan efisien.
*   **Komponen UI**:
    *   `ViewPager` dengan `TabLayout` untuk navigasi antar fragment.
    *   `RecyclerView` untuk menampilkan daftar tugas.
    *   `FloatingActionButton` untuk aksi cepat (tambah tugas dan ganti layout).
    *   `CardView` untuk desain item yang modern.

## Struktur Project

*   `com.f55124091.agendakuliah.ui`: Berisi aktivitas utama dan adapter ViewPager.
*   `com.f55124091.agendakuliah.ui.fragment`: Berisi fragment untuk Tugas Aktif, Selesai, dan Profil.
*   `com.f55124091.agendakuliah.viewmodel`: Mengelola logika data dan interaksi dengan database.
*   `com.f55124091.agendakuliah.model`: Definisi objek data (User dan Task).
*   `com.f55124091.agendakuliah.database`: Logika operasi database SQLite.

## Cara Instalasi

1.  Clone repository ini atau download sebagai ZIP.
2.  Buka project menggunakan **Android Studio**.
3.  Tunggu proses Gradle Sync selesai.
4.  Jalankan aplikasi di Emulator atau perangkat Android asli.

---
*Projek ini dibuat sebagai bagian dari Praktikum Pemrograman Mobile (UTS).*
