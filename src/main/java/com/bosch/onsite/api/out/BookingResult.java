package com.bosch.onsite.api.out;

import com.bosch.onsite.domain.Visit;

public class BookingResult {

	public Visit getVisit() { // NO_UCD (unused code)
		return visit;
	}

	public String getError() { // NO_UCD (unused code)
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	private String error;
	private Visit visit;

	public void setVisit(Visit visit) {
		this.visit = visit;
	}
}
