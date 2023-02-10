package com.dcs.kafkatutorial.kafka.services;
/*
 * Copyright (c) 2023 Seiko Epson. All rights reserved.
 */

import com.dcs.kafkatutorial.data.VotingBallotRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerService
{

	ConsumerRecord<String, VotingBallotRecord> consumeTopic(String topic);
}
