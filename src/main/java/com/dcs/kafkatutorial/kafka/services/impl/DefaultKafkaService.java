package com.dcs.kafkatutorial.kafka.services.impl;
/*
 * Copyright (c) 2023 Seiko Epson. All rights reserved.
 */

import com.dcs.kafkatutorial.data.VotingBallotRecord;
import com.dcs.kafkatutorial.kafka.services.KafkaService;
import com.dcs.kafkatutorial.producers.VotingProducer;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class DefaultKafkaService implements KafkaService
{
	@Value("${project.kafka.topic.partitions}")
	private Integer defaultPartitions;

	@Value("${project.kafka.topic.replicas}")
	private Integer defaultReplicas;

	@Autowired
	private VotingProducer<VotingBallotRecord> ballotProducer;

	@Autowired
	private AdminClient adminClient;

	@Override
	public NewTopic createTopic(final String topic)
	{
		final NewTopic newTopic =new NewTopic(topic, defaultPartitions, defaultReplicas.shortValue());
		try
		{
			if(adminClient.listTopics().names().get().contains(topic)){
				return newTopic;
			}
		} catch (ExecutionException e)
		{
			throw new RuntimeException(e);
		} catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}

		adminClient.createTopics(List.of(newTopic));

		return newTopic;
	}

	@Override
	public void castVote(final VotingBallotRecord votingBallotRecord, final String topic)
	{
		ballotProducer.produce(topic,votingBallotRecord);
	}
}
