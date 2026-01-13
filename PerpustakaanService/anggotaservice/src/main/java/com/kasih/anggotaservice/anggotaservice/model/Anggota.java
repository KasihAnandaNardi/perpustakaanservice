package com.kasih.anggotaservice.anggotaservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anggota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nim;
    private String nama;
    private String alamat;
    private String jenisKelamin;

    // === ADD THIS LINE ===
    private String email;
}