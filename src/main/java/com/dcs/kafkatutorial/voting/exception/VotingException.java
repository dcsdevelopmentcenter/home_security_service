package com.dcs.kafkatutorial.voting.exception;
/*
 * Copyright (c) 2023 Seiko Epson. All rights reserved.
 */

public class VotingException extends RuntimeException
{
	public VotingException(String msg){
		super(msg);
	}

	public VotingException(String message, Throwable cause){
		super(message, cause);
	}

}
