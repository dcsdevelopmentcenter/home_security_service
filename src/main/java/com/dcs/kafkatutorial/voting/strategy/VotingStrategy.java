package com.dcs.kafkatutorial.voting.strategy;

import com.dcs.kafkatutorial.data.CandidateRecord;
import com.dcs.kafkatutorial.data.LocationRecord;
import com.dcs.kafkatutorial.data.VotingBallotRecord;
import com.dcs.kafkatutorial.kafka.services.KafkaService;
import com.dcs.kafkatutorial.voting.exception.VotingException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/*
 * Copyright (c) 2023 Seiko Epson. All rights reserved.
 */
@Component
public class VotingStrategy
{
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private KafkaService kafkaService;
	private final Set<CandidateRecord> candidates = new HashSet<>();
	@Autowired
	private Set<LocationRecord> locations;
	@Autowired
	private Random random;

	@PostConstruct
	public void init(){
		if(executorService.isShutdown()){
			executorService = Executors.newFixedThreadPool(10);
		}
		locations.forEach(location->{
			String topic = location.cityName().concat("_").concat(location.stateName()).toUpperCase();
			kafkaService.createTopic(topic);
			joinVoting(location,topic);
		});
	}

	public void joinVoting(CandidateRecord candidate){
		if(!locations.contains(candidate.locationRecord())){
			throw new VotingException(candidate.locationRecord().cityName());
		}
		candidates.add(candidate);
	}

	public void joinVoting(final LocationRecord locationRecord, final String topicName)
	{
		locations.add(locationRecord);
		executorService.execute(()->{
			while (!Thread.currentThread().isInterrupted())
			{
				performVoting(topicName);
			}

		});
	}
	private void performVoting(final String topicName)
	{
		/*if(CollectionUtils.isEmpty(candidates)){
			return;
		}*/

		var currentCandidateList = candidates.stream()
				.filter(candidate -> Objects.equals(
						candidate.locationRecord().cityName().concat("_").concat(candidate.locationRecord().stateName()).toUpperCase(), topicName)).collect(
						Collectors.toList());
		if(CollectionUtils.isEmpty(currentCandidateList)){
			return;
		}
		int candidateIndex = random.nextInt(0,currentCandidateList.size());
		CandidateRecord candidateRecord = currentCandidateList.get(candidateIndex);
		if(candidateRecord != null)
		{
			VotingBallotRecord castedVote = new VotingBallotRecord(candidateRecord);
			kafkaService.castVote(castedVote, topicName);
		}

	}
	public void endVoting(){
		executorService.shutdownNow();
	}

	public void restart()
	{
		init();
	}

	public Optional<Set<LocationRecord>> getAllAvailableLocations()
	{
		if(CollectionUtils.isEmpty(locations)){
			return Optional.empty();
		}
		return (Optional<Set<LocationRecord>>) Optional.of(locations);
	}
}
