package com.dcs.kafkatutorial.apis;
/*
 * Copyright (c) 2023 Seiko Epson. All rights reserved.
 */

import com.dcs.kafkatutorial.data.CandidateRecord;
import com.dcs.kafkatutorial.data.LocationRecord;
import com.dcs.kafkatutorial.data.VotingBallotRecord;
import com.dcs.kafkatutorial.kafka.services.ConsumerService;
import com.dcs.kafkatutorial.voting.services.VotingService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/votingsystem")
public class MoviesApi
{

	@Autowired
	private VotingService votingService;
	@Autowired
	private ConsumerService consumerService;

	@PostMapping("/register/candidate")
	public ResponseEntity registerCandidate(@RequestBody CandidateRecord candidateRecord)
	{
		try{
			votingService.registerCandidate(candidateRecord);
		}catch (RuntimeException exc){
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, exc.getMessage()+" Not Found", exc);
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping(value = "/retrieve/{topic}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public VotingBallotRecord getCountForCandidate(@PathVariable String topic){
		ConsumerRecord<String, VotingBallotRecord> record = consumerService.consumeTopic(topic);
		return record.value();
	}

	@GetMapping(value = "/retrieve/candidate/city/{cityName}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<CandidateRecord> getCandidatesByCity(@PathVariable String cityName){
		return votingService.getAllCandidatesByCity(cityName);
	}

	@GetMapping(value = "/locations", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String,Set<LocationRecord>> getAllLocations(){
		Set<LocationRecord> allLocations = votingService.getAllAvailableLocations().orElseThrow();
		return allLocations.stream()
				.collect(Collectors
						.groupingBy(LocationRecord::stateName, Collectors
								.mapping(Function.identity(),Collectors.toSet())));
	}

	@PostMapping("/register/location")
	public ResponseEntity<String> registerLocation(@RequestBody LocationRecord locationRecord){
		String topicName;
		try
		{
			topicName = votingService.registerLocation(locationRecord);
			votingService.participateOnVote(locationRecord,topicName);
		}catch (RuntimeException exc){
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, exc.getMessage()+" Not Found", exc);
		}

		return ResponseEntity.ok(topicName);
	}

	@PatchMapping("/stop")
	public void stop(){
		votingService.endVoting();
	}

	@PatchMapping("/restart")
	public void restart(){
		votingService.restart();
	}

}
