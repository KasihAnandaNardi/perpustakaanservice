package com.kasih.peminjamanservice.peminjamanservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// INI HANYA WADAH DATA SEDERHANA (POJO)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anggota {
    // Field-field ini harus sama persis dengan yang ada di model Anggota
    // di dalam proyek anggotaservice.
    private Long id;
    private String nim;
    private String nama;
    private String email;
    private String alamat;
    private String jenisKelamin;
}