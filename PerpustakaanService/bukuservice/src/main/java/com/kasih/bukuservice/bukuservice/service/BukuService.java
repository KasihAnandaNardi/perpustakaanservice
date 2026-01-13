package com.kasih.bukuservice.bukuservice.service;

import com.kasih.bukuservice.bukuservice.model.Buku;
import com.kasih.bukuservice.bukuservice.repository.BukuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BukuService {
    @Autowired
    private BukuRepository BukuRepository;

    public Buku save(Buku Buku) {
        return BukuRepository.save(Buku);
    }

    public Buku findById(Long id) {
        return BukuRepository.findById(id).orElse(null);
    }

    public List<Buku> findAll() {
        return BukuRepository.findAll();
    }
}
