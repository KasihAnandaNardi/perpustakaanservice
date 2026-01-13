package com.pengembalianservice.pengembalianservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor // <-- Pastikan anotasi ini ada
@AllArgsConstructor // <-- Pastikan anotasi ini ada
public class Pengembalian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long peminjamanId;
    private LocalDate tanggalDikembalikan;
    private int terlambat;
    private double denda;
}