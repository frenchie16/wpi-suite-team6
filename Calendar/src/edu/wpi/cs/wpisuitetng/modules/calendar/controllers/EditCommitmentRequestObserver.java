/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Seal Team 6
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.calendar.controllers;

import edu.wpi.cs.wpisuitetng.modules.calendar.controllers.EditCommitmentController;
import edu.wpi.cs.wpisuitetng.modules.calendar.models.Commitment;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

public class EditCommitmentRequestObserver implements RequestObserver {
	
	private EditCommitmentController controller;
	
	/**
	 * Constructs the observer given an EditCommitmentController
	 * @param controller the controller used to edit commitments
	 */
	public EditCommitmentRequestObserver(EditCommitmentController controller) {
		this.controller = controller;
	}
	
	/**
	 * Parse the commitment that was received from the server then pass them to
	 * the controller.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Get the response to the given request
		final ResponseModel response = iReq.getResponse();
		
		// Parse the requirement out of the response body
		final Commitment commitment = Commitment.fromJSON(response.getBody(), Commitment.class);		
	}

	/**
	 * Takes an action if the response results in an error.
	 * Specifically, outputs that the request failed.
	 * @param iReq IRequest
	
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(IRequest) */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to edit a commitment failed");
	}

	/**
	 * Takes an action if the response fails.
	 * Specifically, outputs that the request failed.
	 * @param iReq IRequest
	 * @param exception Exception
	
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(IRequest, Exception) */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to edit a commitment failed spectacularly");
	}

}
