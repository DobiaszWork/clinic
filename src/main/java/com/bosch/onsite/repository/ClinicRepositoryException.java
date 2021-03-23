package com.bosch.onsite.repository;

public class ClinicRepositoryException extends Exception {

	private static final long serialVersionUID = 2743947577857070965L;
	private Error error;

	enum Error {

		NO_DOCTOR("Invalid doctor ID"),
		NO_PATIENT("Invalid patient ID"),
		NO_TIMESLOT("Timeslot already booked"),
		INVALID_TIMESLOT("Invalid timeslot"),
		NO_VISIT("Visit not found");

		private String text;

		Error(String text) {
			this.text = text;
		}

		String getText() {
			return text;
		}

	}

	ClinicRepositoryException(Error error) {
		super();
		this.error = error;
	}

	public String getError() {
		return error.getText();
	}
}
