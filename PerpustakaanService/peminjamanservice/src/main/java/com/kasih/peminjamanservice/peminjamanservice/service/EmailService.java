package com.kasih.peminjamanservice.peminjamanservice.service;

import com.kasih.peminjamanservice.peminjamanservice.model.Peminjaman;
import com.kasih.peminjamanservice.peminjamanservice.vo.Anggota;
import com.kasih.peminjamanservice.peminjamanservice.vo.Buku;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPeminjamanNotification(Peminjaman peminjaman, Anggota anggota, Buku buku) {
        if (anggota.getEmail() == null || anggota.getEmail().isEmpty()) {
            System.err.println("Email not sent: Anggota '" + anggota.getNama() + "' does not have an email address.");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(anggota.getEmail());
            message.setSubject("Notifikasi Peminjaman Buku - Perpustakaan");
            message.setText(buildEmailBody(peminjaman, anggota, buku));

            mailSender.send(message);
            System.out.println("Email notifikasi peminjaman berhasil dikirim ke " + anggota.getEmail());
        } catch (Exception e) {
            System.err.println("Gagal mengirim email notifikasi untuk peminjaman id " + peminjaman.getId() + ": "
                    + e.getMessage());
            // In a real app, you might want to log this error more formally
        }
    }

    private String buildEmailBody(Peminjaman peminjaman, Anggota anggota, Buku buku) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String tanggalPinjamStr = peminjaman.getTanggalPinjam().format(formatter);
        String tanggalKembaliStr = peminjaman.getTanggalKembali().format(formatter);

        return String.format(
                "Halo %s,\n\n" +
                        "Anda telah berhasil melakukan peminjaman buku dengan rincian sebagai berikut:\n\n" +
                        "--- Rincian Peminjaman ---\n" +
                        "ID Peminjaman: %d\n" +
                        "Judul Buku: %s\n" +
                        "Tanggal Pinjam: %s\n" +
                        "Tanggal Harus Kembali: %s\n" +
                        "--------------------------\n\n" +
                        "Mohon untuk mengembalikan buku tepat waktu. Terima kasih.\n\n" +
                        "Salam,\n" +
                        "Admin Perpustakaan",
                anggota.getNama(),
                peminjaman.getId(),
                buku.getJudul(),
                tanggalPinjamStr,
                tanggalKembaliStr);
    }
}