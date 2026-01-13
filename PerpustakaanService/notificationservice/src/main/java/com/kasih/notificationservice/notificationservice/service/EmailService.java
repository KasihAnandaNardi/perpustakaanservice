package com.kasih.notificationservice.notificationservice.service;

import com.kasih.notificationservice.notificationservice.dto.PeminjamanNotificationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * Service responsible for constructing and sending emails related to
 * notifications.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Injects the 'from' email address from application.properties
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Constructor for dependency injection.
     * 
     * @param mailSender The Spring Boot mail sender bean.
     */
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a loan confirmation email based on the data received from RabbitMQ.
     * 
     * @param dto The data transfer object containing all necessary notification
     *            details.
     */
    public void sendPeminjamanNotification(PeminjamanNotificationDTO dto) {
        // A crucial check to prevent errors if an Anggota has no email address.
        if (dto.getAnggotaEmail() == null || dto.getAnggotaEmail().isEmpty()) {
            System.err
                    .println("Email not sent: Anggota '" + dto.getAnggotaNama() + "' does not have an email address.");
            return; // Stop processing for this message.
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(dto.getAnggotaEmail());
            message.setSubject("Notifikasi Peminjaman Buku - Perpustakaan");
            message.setText(buildEmailBody(dto));

            mailSender.send(message);
            System.out.println("Email notifikasi peminjaman berhasil dikirim ke " + dto.getAnggotaEmail());

        } catch (Exception e) {
            System.err.println("Gagal mengirim email notifikasi untuk peminjaman id " + dto.getPeminjamanId() + ": "
                    + e.getMessage());
            // In a real production app, you might want to log this error to a file or
            // monitoring system.
        }
    }

    /**
     * Private helper method to build the email body content from a DTO.
     * 
     * @param dto The notification data.
     * @return A formatted string representing the email body.
     */
    private String buildEmailBody(PeminjamanNotificationDTO dto) {
        // Formatter to make dates more readable, e.g., "24 September 2025"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String tanggalPinjamStr = dto.getTanggalPinjam().format(formatter);
        String tanggalKembaliStr = dto.getTanggalKembali().format(formatter);

        // Using String.format for clean and readable text templating.
        return String.format(
                "Halo %s,\n\n" +
                        "Anda telah berhasil melakukan peminjaman buku dengan rincian sebagai berikut:\n\n" +
                        "--- Rincian Peminjaman ---\n" +
                        "ID Peminjaman: %d\n" +
                        "Judul Buku: %s\n" +
                        "Tanggal Pinjam: %s\n" +
                        "Tanggal Harus Kembali: %s\n" +
                        "--------------------------\n\n" +
                        "Mohon untuk mengembalikan buku tepat waktu untuk menghindari denda. Terima kasih.\n\n" +
                        "Salam,\n" +
                        "Admin Perpustakaan",
                dto.getAnggotaNama(),
                dto.getPeminjamanId(),
                dto.getBukuJudul(),
                tanggalPinjamStr,
                tanggalKembaliStr);
    }
}