package com.kasih.anggotaservice.anggotaservice.controller;

import com.kasih.anggotaservice.anggotaservice.model.Anggota;
import com.kasih.anggotaservice.anggotaservice.service.AnggotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger; // Import Logger
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/anggota")
public class AnggotaController {
    private static final Logger log = LoggerFactory.getLogger(AnggotaController.class);
    @Autowired
    private AnggotaService anggotaService;

    @PostMapping
    @ResponseBody
    public Anggota save(@RequestBody Anggota anggota) {
        // 2. Tulis Log saat ada POST request
        log.info("Menerima request POST anggota baru. Nama: {}, Email: {}", anggota.getNama(), anggota.getEmail());

        Anggota savedAnggota = anggotaService.save(anggota);

        // 3. (Opsional) Tulis Log saat sukses
        log.info("Sukses menyimpan anggota dengan ID: {}", savedAnggota.getId());

        return savedAnggota;
    }

    @GetMapping("/{id}")
    public Anggota findById(@PathVariable Long id) {
        log.info("Mencari anggota dengan ID: {}", id); // Log GET by ID
        return anggotaService.findById(id);
    }

    @GetMapping("/")
    public List<Anggota> findAll() {
        log.info("Mengambil semua data anggota"); // Log GET All
        return anggotaService.findAll();
    }

    // === ADD THIS ENTIRE METHOD ===
    @PutMapping("/{id}")
    public Anggota update(@PathVariable Long id, @RequestBody Anggota anggota) {
        // Set the ID from the URL path to ensure we update the correct record
        anggota.setId(id);
        return anggotaService.save(anggota);
    }
}