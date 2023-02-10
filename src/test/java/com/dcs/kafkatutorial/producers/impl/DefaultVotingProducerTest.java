package com.dcs.kafkatutorial.producers.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Copyright (c)  Seiko Epson. All rights reserved.
 */

class DefaultVotingProducerTest
{
	private DefaultVotingProducer producer = new DefaultVotingProducer();

	@Test
	void solution()
	{
		String str = "EcOwmXDWcKVgvbKXjfljpLQWaTTkRFYvnivjDCTQYNSc";

		Assertions.assertThat(producer.solution(str)).isEqualTo(26);
	}
}