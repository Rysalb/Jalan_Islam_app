<!-- BEGIN:core-ui-states-rules -->
# 10 Core UI States (Standar Wajib Desain & Pengalaman Pengguna)

Setiap pembuatan atau pembaruan halaman, kartu, modal, tabel, dan alur form di aplikasi PT Beruang Salju **WAJIB** menerapkan 10 Core UI States berikut secara konsisten:

1. **Empty State:**
   - Pandu pengguna sebelum data ada (*guide user before data exists*).
   - Tampilkan ikon/ilustrasi ramah, penjelasan singkat, dan tombol tindakan pertama (Call to Action / CTA, misal: *Tambah Data Baru* atau link panduan).

2. **Loading State:**
   - Kecepatan yang dirasakan sangat penting (*perceived speed matters; keep wait active*).
   - Gunakan skeleton placeholder atau spinner aktif dengan teks proses spesifik (*Memuat data transaksi...*), hindari layar kosong atau beku tanpa indikator.

3. **Error State:**
   - Jelaskan penyebab masalah secara transparan disertai tombol pemulihan (*clear cause plus actionable recovery*).
   - Sertakan tombol *Coba Lagi* / *Refresh* dan hindari pesan error teknis yang membingungkan staf.

4. **Offline State:**
   - Beri tahu status koneksi; cegah tampilan kosong (*inform state; prevent blank views*).
   - Pantau status jaringan (`navigator.onLine`, event `online`/`offline`). Tampilkan banner peringatan dan nonaktifkan tombol simpan/unggah untuk mencegah hilangnya input pengguna.

5. **Slow Network State:**
   - Tunjukkan kemajuan inkremental; hindari spinner beku (*show incremental progress, avoid frozen spinners*).
   - Berikan umpan balik bertahap saat mengunggah berkas resi atau proses API berat (*Mengunggah berkas resi...*, *Menyinkronkan ke server...*).

6. **Zero Search Results State:**
   - Berikan saran atau alternatif; hindari jalan buntu (*provide suggestions/alternatives, avoid dead ends*).
   - Tampilkan pesan bahwa pencarian tidak ditemukan, tombol *Reset Filter & Pencarian*, dan jika relevan opsi alternatif (*Gunakan kata kunci sebagai input manual*).

7. **Permission Denied State:**
   - Jelaskan alasan hak akses dan rute pemulihan (*explain rationale and recovery route*).
   - Beri informasi role/jabatan yang berwenang dan sediakan tombol kembali ke dashboard utama.

8. **Session Expiry & Draft Preservation:**
   - Amankan draf formulir sebelum sesi terputus (*persist draft state before auth teardown*).
   - Simpan draf input pengguna secara berkala ke penyimpanan lokal (`localStorage`). Jika halaman ter-refresh atau sesi terputus, tawarkan tombol *Pulihkan Draf*.

9. **Form Validation State:**
   - Tempatkan error validasi tepat di bawah input yang bersangkutan (*inline error placement at exact input field*).
   - Tandai border input dengan warna merah `var(--error)` dan tampilkan teks penjelasan spesifik tepat di bawah elemen input.

10. **Success State:**
    - Berikan apresiasi keberhasilan dan langkah selanjutnya yang jelas (*acknowledge completion, provide clear next step*).
    - Tampilkan notifikasi sukses, nomor referensi/pengajuan yang baru terbentuk, dan arahkan pengguna kembali ke daftar data atau tindakan selanjutnya.
<!-- END:core-ui-states-rules -->