package com.pengembalianservice.pengembalianservice.vo;

import lombok.Data;
import java.time.LocalDate;

// Kelas ini harus memiliki field yang SAMA PERSIS dengan PeminjamanView di peminjamanqueryservice
@Data
public class PeminjamanView {
    private Long peminjamanId;
    private String namaAnggota;
    private String emailAnggota;
    private String judulBuku;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali; // Ini field yang paling kita butuhkan
}