package com.kasih.notificationservice.notificationservice.service;

import com.kasih.notificationservice.notificationservice.dto.PeminjamanNotificationDTO;
import org.slf4j.Logger; // 1. Import Logger
import org.slf4j.LoggerFactory; // 2. Import LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    // 3. Inisialisasi Logger
    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    private final EmailService emailService;

    public NotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void handlePeminjamanNotification(PeminjamanNotificationDTO notificationDTO) {
        // 4. Log saat pesan diterima (Menggantikan System.out.println)
        // Kita log detail penting seperti Email tujuan dan ID Peminjaman
        log.info("Menerima Event Notifikasi dari RabbitMQ. ID Peminjaman: {}, Email Tujuan: {}",
                notificationDTO.getPeminjamanId(),
                notificationDTO.getAnggotaEmail());

        try {
            // Panggil service pengirim email
            emailService.sendPeminjamanNotification(notificationDTO);

            // 5. Log Sukses
            log.info("SUKSES mengirim email notifikasi ke: {}", notificationDTO.getAnggotaEmail());

        } catch (Exception e) {
            // 6. Log Error (Sangat penting agar muncul Merah di Kibana)
            // Parameter 'e' di akhir akan mencetak Stack Trace lengkap jika perlu debugging
            log.error("GAGAL mengirim email ke: {}. Pesan Error: {}",
                    notificationDTO.getAnggotaEmail(), e.getMessage());
        }
    }
}