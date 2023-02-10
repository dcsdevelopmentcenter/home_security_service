package com.dcs.kafkatutorial.kafka.services;
/*
 * Copyright (c) 2023 Seiko Epson. All rights reserved.
 */

import com.dcs.kafkatutorial.data.VotingBallotRecord;
import org.apache.kafka.clients.admin.NewTopic;

public interface KafkaService
{
	NewTopic createTopic(final String topic);
	void castVote(VotingBallotRecord votingBallotRecord, String topic);
}
