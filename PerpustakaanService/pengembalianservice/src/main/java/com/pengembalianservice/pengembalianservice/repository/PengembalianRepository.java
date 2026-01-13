package com.pengembalianservice.pengembalianservice.repository;

import com.pengembalianservice.pengembalianservice.model.Pengembalian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository untuk entitas Pengembalian.
 * JpaRepository<Pengembalian, Long> berarti repository ini akan mengelola
 * objek Pengembalian yang memiliki primary key dengan tipe Long.
 */
@Repository
public interface PengembalianRepository extends JpaRepository<Pengembalian, Long> {

    // Sama seperti PeminjamanRepository, JpaRepository sudah menyediakan
    // semua method CRUD (Create, Read, Update, Delete) yang umum digunakan.

    // Contoh query kustom yang mungkin berguna di masa depan:
    // Optional<Pengembalian> findByPeminjamanId(Long peminjamanId);
}