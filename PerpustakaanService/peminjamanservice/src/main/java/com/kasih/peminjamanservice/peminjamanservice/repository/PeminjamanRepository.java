package com.kasih.peminjamanservice.peminjamanservice.repository;

import com.kasih.peminjamanservice.peminjamanservice.model.Peminjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository untuk entitas Peminjaman.
 * JpaRepository<Peminjaman, Long> berarti repository ini akan mengelola
 * objek Peminjaman yang memiliki primary key dengan tipe Long.
 */
@Repository
public interface PeminjamanRepository extends JpaRepository<Peminjaman, Long> {

    // Spring Data JPA secara otomatis akan menyediakan method-method standar
    // seperti:
    // - save(Peminjaman peminjaman) -> untuk menyimpan atau memperbarui
    // - findById(Long id) -> untuk mencari berdasarkan ID
    // - findAll() -> untuk mendapatkan semua data
    // - deleteById(Long id) -> untuk menghapus berdasarkan ID

    // Anda bisa menambahkan query kustom di sini jika diperlukan.
    // Contoh: List<Peminjaman> findByAnggotaId(Long anggotaId);
}