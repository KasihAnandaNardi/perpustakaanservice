package com.kasih.peminjamanservice.peminjamanservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Peminjaman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long anggotaId;
    private Long bukuId;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
}