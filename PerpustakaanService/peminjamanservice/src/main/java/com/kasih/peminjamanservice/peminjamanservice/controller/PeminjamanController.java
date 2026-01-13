package com.kasih.peminjamanservice.peminjamanservice.controller;

import com.kasih.peminjamanservice.peminjamanservice.model.Peminjaman;
import com.kasih.peminjamanservice.peminjamanservice.service.PeminjamanService;
import org.slf4j.Logger; // 1. Import Logger
import org.slf4j.LoggerFactory; // 2. Import LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/peminjaman/command")
public class PeminjamanController {

    // 3. Inisialisasi Logger
    private static final Logger log = LoggerFactory.getLogger(PeminjamanController.class);

    private final PeminjamanService peminjamanService;

    @Autowired
    public PeminjamanController(PeminjamanService peminjamanService) {
        this.peminjamanService = peminjamanService;
    }

    @PostMapping
    public Peminjaman save(@RequestBody Peminjaman peminjaman) {
        // 4. Log saat request masuk (Menangkap siapa pinjam apa)
        log.info("Menerima REQUEST PEMINJAMAN. AnggotaID: {}, BukuID: {}, Tanggal: {}",
                peminjaman.getAnggotaId(),
                peminjaman.getBukuId(),
                peminjaman.getTanggalPinjam());

        Peminjaman savedPeminjaman = peminjamanService.save(peminjaman);

        // 5. Log saat sukses (Menangkap ID transaksi yang terbentuk)
        log.info("SUKSES memproses peminjaman. ID Transaksi: {}", savedPeminjaman.getId());

        return savedPeminjaman;
    }
}