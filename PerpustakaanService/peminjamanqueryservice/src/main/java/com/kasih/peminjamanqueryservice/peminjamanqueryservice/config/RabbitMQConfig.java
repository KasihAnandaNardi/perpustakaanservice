package com.kasih.peminjamanqueryservice.peminjamanqueryservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.queue}")
    private String queueName;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routingkey}")
    private String routingKey;

    /**
     * Bean ini akan membuat sebuah antrian (queue) di RabbitMQ
     * dengan nama yang diambil dari application.properties.
     * Inilah yang hilang dan menyebabkan error Anda.
     */
    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    /**
     * Bean ini memastikan exchange dengan tipe 'topic' ada di RabbitMQ.
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    /**
     * Bean ini akan "mengikat" atau menghubungkan queue Anda ke exchange
     * menggunakan routing key. Artinya: "Semua pesan yang dikirim ke
     * exchange ini dengan routing key ini, tolong teruskan ke queue saya."
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    /**
     * Bean ini sangat penting agar Spring bisa mengubah pesan JSON
     * dari RabbitMQ kembali menjadi objek Java (PeminjamanEventDTO).
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}