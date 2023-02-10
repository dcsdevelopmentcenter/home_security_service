package com.dcs.kafkatutorial.voting.services;
/*
 * Copyright (c) 2023 Seiko Epson. All rights reserved.
 */

import com.dcs.kafkatutorial.data.CandidateRecord;
import com.dcs.kafkatutorial.data.LocationRecord;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VotingService
{
	void registerCandidate(final CandidateRecord candidateRecord);
	void participateOnVote(final CandidateRecord candidate);
	void participateOnVote(final LocationRecord locationRecord, final String topicName);
	void endVoting();

	String registerLocation(LocationRecord locationRecord);

	void restart();

	Optional<Set<LocationRecord>> getAllAvailableLocations();

	List<CandidateRecord> getAllCandidatesByCity(String cityName);
}
