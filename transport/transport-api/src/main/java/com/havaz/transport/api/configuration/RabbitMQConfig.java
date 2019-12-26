package com.havaz.transport.api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.util.Assert;

@Configuration
@EnableRabbit
public class RabbitMQConfig implements RabbitListenerConfigurer {

    public static final String SPRING_BEAN_RABBIT_LISTENER_CONTAINER_FACTORY_ERP = "rabbitListenerContainerFactoryERP";

    @Value("${havaz.rabbitmq.san.exchange}")
    private String sanExchange;

    @Value("${havaz.rabbitmq.san.host}")
    private String sanHost;

    @Value("${havaz.rabbitmq.san.port}")
    private Integer sanPort;

    @Value("${havaz.rabbitmq.san.username}")
    private String sanUsername;

    @Value("${havaz.rabbitmq.san.password}")
    private String sanPassword;

    @Value("${havaz.rabbitmq.san.virtual-host}")
    private String sanVirtualHost;

    @Value("${havaz.rabbitmq.san.driver.arrived.routing-key}")
    private String routingDriverKey;

    @Value("${havaz.rabbitmq.san.driver.arrived.queue}")
    private String queueName;

    @Value("${havaz.rabbitmq.san.driver.confirmed.queue}")
    private String queueDriverConfirm;

    @Value("${havaz.rabbitmq.san.driver.confirmed.routing-key}")
    private String driverConfirmRoutingKey;

    @Value("${havaz.rabbitmq.erp.host}")
    private String erpHost;

    @Value("${havaz.rabbitmq.erp.virtual-host}")
    private String erpVirtualHost;

    @Value("${havaz.rabbitmq.erp.port}")
    private Integer erpPort;

    @Value("${havaz.rabbitmq.erp.username}")
    private String erpUsername;

    @Value("${havaz.rabbitmq.erp.password}")
    private String erpPassword;

    @Value("${havaz.rabbitmq.transport.transferticket.queue}")
    private String queueTransferTicketName;

    @Value("${havaz.rabbitmq.transport.transferticket.exchange}")
    private String exchangeTransferTicket;

    @Value("${havaz.rabbitmq.transport.transferticket.routing-key}")
    private String transferTicketRoutingKey;

    private final ObjectMapper objectMapper;

    @Autowired
    public RabbitMQConfig(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "objectMapper cannot be null");
        this.objectMapper = objectMapper;
    }

    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
        registrar.setContainerFactory(rabbitListenerContainerFactoryERP());
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(sanExchange);
    }

    @Bean
    public TopicExchange exchangeTransferTicket() {
        return new TopicExchange(exchangeTransferTicket);
    }

    @Bean
    public Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    public Queue queueDriverConfirmed() {
        return new Queue(queueDriverConfirm, false);
    }

    @Bean
    public Queue queueTransferTicket() {
        return new Queue(queueTransferTicketName, false);
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        AmqpAdmin amqpAdmin = new RabbitAdmin(rabbitMQConnectionFactoryERP());
        amqpAdmin.declareQueue(queueTransferTicket());
        return amqpAdmin;
    }

    @Bean
    public Binding topicBinding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(routingDriverKey);
    }

    @Bean
    public Binding topicBindingDriver() {
        return BindingBuilder.bind(queueDriverConfirmed()).to(exchange())
                .with(driverConfirmRoutingKey);
    }

    @Bean
    public Binding transferTicketBinding() {
        return BindingBuilder.bind(queueTransferTicket()).to(exchangeTransferTicket())
                .with(transferTicketRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter() {
        return new CustomJackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter();
        mappingJackson2MessageConverter.setObjectMapper(objectMapper);
        return mappingJackson2MessageConverter;
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplateERP() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitMQConnectionFactoryERP());
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public RabbitTemplate rabbitTemplateSan() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitMQConnectionFactorySan());
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    @Primary
    public ConnectionFactory rabbitMQConnectionFactorySan() {
        return buildConnectionFactory(sanHost, sanPort, sanUsername, sanPassword, sanVirtualHost);
    }

    @Bean
    public ConnectionFactory rabbitMQConnectionFactoryERP() {
        return buildConnectionFactory(erpHost, erpPort, erpUsername, erpPassword, erpVirtualHost);
    }

    @Bean(name = SPRING_BEAN_RABBIT_LISTENER_CONTAINER_FACTORY_ERP)
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactoryERP() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(rabbitMQConnectionFactoryERP());
        factory.setMessageConverter(jackson2MessageConverter());
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }

    private ConnectionFactory buildConnectionFactory(String host, int port, String username,
                                                     String password, String virtualHost) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setRequestedHeartBeat(30);
        return connectionFactory;
    }

    private static class CustomJackson2JsonMessageConverter extends Jackson2JsonMessageConverter {

        CustomJackson2JsonMessageConverter(
                ObjectMapper jsonObjectMapper) {
            super(jsonObjectMapper);
        }

        @Override
        public Object fromMessage(Message message,
                                  Object conversionHint) throws MessageConversionException {

            // Do bên ERP không thêm property 'application/json' khi publish message
            message.getMessageProperties().setContentType("application/json");

            return super.fromMessage(message, conversionHint);
        }
    }
}
