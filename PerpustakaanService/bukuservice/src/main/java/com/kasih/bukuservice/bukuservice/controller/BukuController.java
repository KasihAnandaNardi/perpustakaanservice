package com.kasih.bukuservice.bukuservice.controller;

import com.kasih.bukuservice.bukuservice.model.Buku;
import com.kasih.bukuservice.bukuservice.service.BukuService;
import org.slf4j.Logger; // 1. Import Logger
import org.slf4j.LoggerFactory; // 2. Import LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buku")
public class BukuController {

    // 3. Inisialisasi Logger
    private static final Logger log = LoggerFactory.getLogger(BukuController.class);

    @Autowired
    private BukuService bukuService;

    @PostMapping
    public Buku save(@RequestBody Buku buku) {
        // 4. Log saat menerima request POST
        log.info("Menerima request POST buku baru. Judul Buku: {}, Pengarang: {}", buku.getJudul(),
                buku.getPengarang());

        Buku savedBuku = bukuService.save(buku);

        // 5. Log saat sukses disimpan
        log.info("Sukses menyimpan buku dengan ID: {}", savedBuku.getId());

        return savedBuku;
    }

    @GetMapping("/{id}")
    public Buku findById(@PathVariable("id") Long id) {
        log.info("Request GET buku berdasarkan ID: {}", id);
        return bukuService.findById(id);
    }

    @GetMapping
    public List<Buku> findAll() {
        log.info("Request GET semua data buku");
        return bukuService.findAll();
    }
}