package com.kasih.notificationservice.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeminjamanNotificationDTO {
    private Long peminjamanId;
    private String anggotaNama;
    private String anggotaEmail;
    private String bukuJudul;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
}