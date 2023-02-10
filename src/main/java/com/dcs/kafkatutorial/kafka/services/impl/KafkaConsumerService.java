package com.dcs.kafkatutorial.kafka.services.impl;
/*
 * Copyright (c) 20123 Seiko Epson. All rights reserved.
 */

import com.dcs.kafkatutorial.data.VotingBallotRecord;
import com.dcs.kafkatutorial.kafka.services.ConsumerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class KafkaConsumerService implements ConsumerService
{
	@Autowired
	private KafkaTemplate<String, VotingBallotRecord> kafkaTemplate;

	@Override
	public ConsumerRecord<String,VotingBallotRecord> consumeTopic(final String topic){

		ConsumerRecord<String,VotingBallotRecord> record = kafkaTemplate.receive(topic,1,0, Duration.ofMillis(500));
		return record;
	}


}
