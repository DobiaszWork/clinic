package com.bosch.onsite.api.out;

import com.bosch.onsite.domain.Visit;

public class BookingResult {

	private String error;
	private Visit visit;

	public String getError() { // NO_UCD (unused code)
		return error;
	}

	public Visit getVisit() { // NO_UCD (unused code)
		return visit;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}
}
