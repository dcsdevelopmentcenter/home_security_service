package com.dcs.kafkatutorial.apis;
/*
 * Copyright (c) 2023 Seiko Epson. All rights reserved.
 */

import com.dcs.kafkatutorial.data.BallotResult;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AppNotification
{
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public BallotResult greeting(BallotResult message) throws Exception {
		return message;
	}
}
