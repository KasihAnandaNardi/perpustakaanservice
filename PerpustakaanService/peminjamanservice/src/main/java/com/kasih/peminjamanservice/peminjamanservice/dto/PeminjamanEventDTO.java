package com.kasih.peminjamanservice.peminjamanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeminjamanEventDTO {
    private Long peminjamanId;
    private String anggotaNama;
    private String anggotaEmail;
    private String bukuJudul;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
}