package com.kasih.peminjamanservice.peminjamanservice.config;

// We remove the unused imports for Queue, Binding, and BindingBuilder
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // The producer only needs to know the name of the exchange it's sending to.
    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    // --- REMOVED ---
    // The @Value for queueName and routingKey fields have been removed
    // because they are not needed in this configuration file.

    // --- REMOVED ---
    // The Queue bean is the responsibility of the consumer (notificationservice),
    // not the producer. Removing this bean fixes your error.
    /*
     * @Bean
     * public Queue queue() {
     * return new Queue(queueName);
     * }
     */

    // This bean is good practice. It tells Spring to ensure the exchange
    // exists on the RabbitMQ server when the application starts.
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    // --- REMOVED ---
    // The Binding bean connects a queue to an exchange. This is also the
    // responsibility of the consumer, not the producer.
    /*
     * @Bean
     * public Binding binding(Queue queue, TopicExchange exchange) {
     * return BindingBuilder.bind(queue).to(exchange).with(routingKey);
     * }
     */

    // This bean is ESSENTIAL. It tells Spring to convert your Java DTO object
    // into JSON format before sending it as a message.
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}