package com.kasih.peminjamanqueryservice.peminjamanqueryservice.controller;

import com.kasih.peminjamanqueryservice.peminjamanqueryservice.model.PeminjamanView;
import com.kasih.peminjamanqueryservice.peminjamanqueryservice.repository.PeminjamanViewRepository;

import org.slf4j.Logger; // 1. Import Logger
import org.slf4j.LoggerFactory; // 2. Import LoggerFactory
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/peminjaman/query")
public class PeminjamanQueryController {

    // 3. Inisialisasi Logger
    private static final Logger log = LoggerFactory.getLogger(PeminjamanQueryController.class);

    private final PeminjamanViewRepository repository;

    public PeminjamanQueryController(PeminjamanViewRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<PeminjamanView> getAllPeminjaman() {
        // 4. Log request ambil semua data
        log.info("Menerima Request GET semua data peminjaman (History)");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeminjamanView> getPeminjamanById(@PathVariable Long id) {
        // 5. Log request ambil data spesifik
        log.info("Menerima Request GET detail peminjaman dengan ID: {}", id);

        Optional<PeminjamanView> data = repository.findById(id);

        if (data.isPresent()) {
            log.info("Data peminjaman ID: {} DITEMUKAN.", id);
            return ResponseEntity.ok(data.get());
        } else {
            // 6. Log jika data tidak ketemu (Penting untuk debugging)
            log.warn("Data peminjaman ID: {} TIDAK DITEMUKAN.", id);
            return ResponseEntity.notFound().build();
        }
    }
}