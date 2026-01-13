package com.kasih.peminjamanqueryservice.peminjamanqueryservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document("peminjaman_views") // Nama collection di MongoDB
@Data
public class PeminjamanView {
    @Id
    private Long peminjamanId; // Kita gunakan ID dari SQL sebagai ID di sini
    private String namaAnggota;
    private String emailAnggota;
    private String judulBuku;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
}