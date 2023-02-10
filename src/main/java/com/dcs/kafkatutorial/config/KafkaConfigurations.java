package com.dcs.kafkatutorial.config;
/*
 * Copyright (c) 2023 Seiko Epson. All rights reserved.
 */

import com.dcs.kafkatutorial.data.LocationRecord;
import com.dcs.kafkatutorial.data.VotingBallotRecord;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class KafkaConfigurations
{
	@Value(value = "${spring.kafka.admin.bootstrap.servers}")
	private String adminServers;

	@Bean
	ExecutorService executorService(){
		return Executors.newFixedThreadPool(20);
	}

	@Bean
	AdminClient adminClient(){
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, adminServers);
		return AdminClient.create(configs);
	}

	@Bean
	Set<LocationRecord> locations(){
		return new HashSet<>(
				Set.of(new LocationRecord("01", "Muzaffarpur", "Bihar"), new LocationRecord("02", "Gaya", "Bihar"), new LocationRecord("03", "Patna", "Bihar"),
						new LocationRecord("04", "Sonpur", "Bihar"), new LocationRecord("05", "Hajipur", "Bihar"),
						new LocationRecord("06", "Samastipur", "Bihar"), new LocationRecord("07", "Darbhanga", "Bihar"),
						new LocationRecord("08", "Lucknow", "UP"), new LocationRecord("09", "Dehradun", "UP"),
						new LocationRecord("10", "Mumbai", "Maharastra"), new LocationRecord("11", "Pune", "Maharastra"),
						new LocationRecord("12", "Kolkata", "WB"), new LocationRecord("13", "Durgapur", "WB"),
						new LocationRecord("14", "Katwa", "WB"), new LocationRecord("15", "Bolpur", "WB"),
						new LocationRecord("16", "Chandigadh", "Punjab"), new LocationRecord("17", "Patiala", "Punjab")));

	}

	@Bean
	Random random(){
		return new Random();
	}

	@Bean
	public KafkaTemplate<String, VotingBallotRecord> kafkaTemplate(){
		final KafkaTemplate<String, VotingBallotRecord> kafkaTemplate = new KafkaTemplate<>(producerFactory());
		kafkaTemplate.setConsumerFactory(consumerFactory());
		return kafkaTemplate;
	}

	@Bean
	public ProducerFactory<String, VotingBallotRecord> producerFactory(){
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094,localhost:9095,localhost:9096");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}
	@Bean
	public ConsumerFactory<String, VotingBallotRecord> consumerFactory(){
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094,localhost:9095,localhost:9096");
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(config);
	}
}
