package com.kasih.peminjamanservice.peminjamanservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// INI JUGA HANYA WADAH DATA SEDERHANA (POJO)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Buku {
    // Field-field ini harus sama persis dengan yang ada di model Buku
    // di dalam proyek bukuservice.
    private Long id;
    private String judul;
    private String pengarang;
    private String penerbit;
    private String tahunTerbit;
}