package com.dcs.kafkatutorial.producers.impl;
/*
 * Copyright (c) 2023 Seiko Epson. All rights reserved.
 */

import com.dcs.kafkatutorial.data.VotingBallotRecord;
import com.dcs.kafkatutorial.producers.VotingProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static java.lang.System.*;

@Component
public class DefaultVotingProducer implements VotingProducer<VotingBallotRecord>
{

	@Autowired
	private KafkaTemplate<String, VotingBallotRecord> kafkaTemplate;

	@Override
	public VotingBallotRecord produce(final String topic, final VotingBallotRecord data)
	{
		CompletableFuture<SendResult<String, VotingBallotRecord>> future = kafkaTemplate.send(topic, data);

		//future.acceptEither()
		return null;
	}
	public long solution(String str){
		//
		long repeatedCount = 0;
		char repeated = str.charAt(0);
		String nonRepeated = "";
		for (int i = 0; i<str.length();i++){
			if(str.charAt(i) == repeated || nonRepeated.contains(String.valueOf(str.charAt(i)))){
				repeated = str.charAt(i);
				nonRepeated = String.valueOf(repeated);
			}else{
				nonRepeated = nonRepeated + str.charAt(i);
				if(repeatedCount < nonRepeated.length())
				{
					repeatedCount = nonRepeated.length();
				}
			}

		}
		out.println(repeatedCount);
		return repeatedCount;
	}
}
