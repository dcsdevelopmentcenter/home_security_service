package com.dcs.kafkatutorial.voting.services.impl;
/*
 * Copyright (c) 2023 Seiko Epson. All rights reserved.
 */

import com.dcs.kafkatutorial.data.CandidateRecord;
import com.dcs.kafkatutorial.data.LocationRecord;
import com.dcs.kafkatutorial.kafka.services.KafkaService;
import com.dcs.kafkatutorial.voting.services.VotingService;
import com.dcs.kafkatutorial.voting.strategy.VotingStrategy;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DefaultVotingService implements VotingService
{
	@Autowired
	private KafkaService kafkaService;
	@Autowired
	private VotingStrategy votingStrategy;

	@Override
	public void registerCandidate(final CandidateRecord candidateRecord)
	{
		votingStrategy.joinVoting(candidateRecord);
	}

	@Override
	public void participateOnVote(final CandidateRecord candidate)
	{
		votingStrategy.joinVoting(candidate);
	}

	@Override
	public void participateOnVote(final LocationRecord locationRecord, final String topicName)
	{
		votingStrategy.joinVoting(locationRecord,topicName);
	}

	@Override
	public void endVoting()
	{
		votingStrategy.endVoting();
	}

	@Override
	public String registerLocation(final LocationRecord locationRecord)
	{
		final String topicName = locationRecord.cityName()
				.concat("_")
				.concat(locationRecord.stateName()).toUpperCase();
		final NewTopic newTopic = kafkaService.createTopic(topicName);
		return newTopic.name();

	}

	@Override
	public void restart()
	{
		votingStrategy.restart();

	}

	@Override
	public Optional<Set<LocationRecord>> getAllAvailableLocations()
	{
		return votingStrategy.getAllAvailableLocations();
	}

	@Override public List<CandidateRecord> getAllCandidatesByCity(final String cityName)
	{
		return null;
	}

}
