package com.kasih.peminjamanservice.peminjamanservice.vo;

import com.kasih.peminjamanservice.peminjamanservice.model.Peminjaman;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTemplateVO {
    private Peminjaman peminjaman;
    private Anggota anggota;
    private Buku buku;
}