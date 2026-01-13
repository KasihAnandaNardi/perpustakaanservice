package com.pengembalianservice.pengembalianservice.service;

import com.pengembalianservice.pengembalianservice.model.Pengembalian;
import com.pengembalianservice.pengembalianservice.repository.PengembalianRepository;
import com.pengembalianservice.pengembalianservice.vo.PeminjamanView;
import com.pengembalianservice.pengembalianservice.vo.PeminjamanResponseTemplateVO;
import com.pengembalianservice.pengembalianservice.vo.PengembalianResponseTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class PengembalianService {
    @Autowired
    private PengembalianRepository pengembalianRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Pengembalian save(Pengembalian pengembalian) {
        // === PERUBAHAN DI SINI ===
        // Hapus tanda hubung dari nama service agar cocok dengan yang ada di Eureka
        PeminjamanView peminjamanView = restTemplate.getForObject(
                "http://PEMINJAMANQUERYSERVICE/api/peminjaman/query/" + pengembalian.getPeminjamanId(), // <-- NAMA
                                                                                                        // DIPERBAIKI
                PeminjamanView.class);

        if (peminjamanView == null) {
            throw new RuntimeException("Data Peminjaman tidak ditemukan dengan ID: " + pengembalian.getPeminjamanId());
        }

        // ... sisa logika Anda tidak perlu diubah ...
        pengembalian.setTanggalDikembalikan(LocalDate.now());
        long daysBetween = ChronoUnit.DAYS.between(peminjamanView.getTanggalKembali(),
                pengembalian.getTanggalDikembalikan());

        int keterlambatan = daysBetween > 0 ? (int) daysBetween : 0;
        pengembalian.setTerlambat(keterlambatan);

        double denda = keterlambatan * 1000.0;
        pengembalian.setDenda(denda);

        return pengembalianRepository.save(pengembalian);
    }

    // --- METHOD BARU YANG ANDA BUTUHKAN ---
    public PengembalianResponseTemplateVO findDetailById(Long id) {
        Pengembalian pengembalian = pengembalianRepository.findById(id).orElse(null);
        if (pengembalian == null) {
            return null;
        }

        try {
            // === PERBAIKAN DI SINI ===
            // Gunakan PeminjamanView.class, BUKAN PeminjamanResponseTemplateVO.class
            PeminjamanView peminjamanDetail = restTemplate.getForObject(
                    "http://PEMINJAMANQUERYSERVICE/api/peminjaman/query/" + pengembalian.getPeminjamanId(),
                    PeminjamanView.class);

            PengembalianResponseTemplateVO response = new PengembalianResponseTemplateVO();
            response.setPengembalian(pengembalian);

            // Masukkan data PeminjamanView ke response
            response.setPeminjamanDetail(peminjamanDetail);

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}