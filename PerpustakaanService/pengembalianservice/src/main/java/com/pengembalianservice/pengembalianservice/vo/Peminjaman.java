package com.pengembalianservice.pengembalianservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

// INI HANYA WADAH DATA (POJO), BUKAN ENTITAS DATABASE
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Peminjaman {
    // Field-field ini harus cocok dengan yang ada di Peminjaman.java
    // di dalam proyek peminjaman-service
    private Long id;
    private Long anggotaId;
    private Long bukuId;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
}