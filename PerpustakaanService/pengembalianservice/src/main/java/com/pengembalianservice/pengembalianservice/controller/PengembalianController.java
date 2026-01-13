package com.pengembalianservice.pengembalianservice.controller;

import com.pengembalianservice.pengembalianservice.model.Pengembalian;
import com.pengembalianservice.pengembalianservice.service.PengembalianService;
import com.pengembalianservice.pengembalianservice.vo.PengembalianResponseTemplateVO;
import org.slf4j.Logger; // 1. Import Logger
import org.slf4j.LoggerFactory; // 2. Import LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pengembalian")
public class PengembalianController {

    // 3. Inisialisasi Logger
    private static final Logger log = LoggerFactory.getLogger(PengembalianController.class);

    @Autowired
    private PengembalianService pengembalianService;

    @PostMapping
    public ResponseEntity<Pengembalian> save(@RequestBody Pengembalian pengembalian) {
        // 4. Log Request Masuk
        log.info("Menerima Request PENGEMBALIAN untuk PeminjamanID: {}", pengembalian.getPeminjamanId());

        try {
            Pengembalian pengembalianBaru = pengembalianService.save(pengembalian);

            // 5. Log Sukses
            log.info("SUKSES menyimpan pengembalian. ID Pengembalian: {}", pengembalianBaru.getId());

            return ResponseEntity.ok(pengembalianBaru);
        } catch (Exception e) {
            // 6. Log Error (Sangat Penting untuk debugging)
            // Kita gunakan log.error agar muncul merah di dashboard nanti jika
            // dikonfigurasi
            log.error("GAGAL memproses pengembalian. PeminjamanID: {}. Error: {}",
                    pengembalian.getPeminjamanId(), e.getMessage());

            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PengembalianResponseTemplateVO> findDetailById(@PathVariable("id") Long id) {
        // 7. Log Get Detail
        log.info("Request GET detail pengembalian ID: {}", id);

        PengembalianResponseTemplateVO response = pengembalianService.findDetailById(id);

        if (response == null) {
            log.warn("Data pengembalian ID: {} TIDAK DITEMUKAN.", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}