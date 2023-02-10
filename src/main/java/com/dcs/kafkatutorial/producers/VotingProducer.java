package com.dcs.kafkatutorial.producers;
/*
 * Copyright (c) 2023 Seiko Epson. All rights reserved.
 */

public interface VotingProducer<T>
{
   T produce(String topic, T data);
}
