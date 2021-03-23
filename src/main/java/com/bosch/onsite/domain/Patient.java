package com.bosch.onsite.domain;

import com.bosch.onsite.repository.ClinicRepository;

public class Patient extends AbstractPerson {

	public Patient(Long id, String name, String secondName, ClinicRepository repo) {
		super(id, name, secondName, repo);
	}

	private static final long serialVersionUID = -2810479630103634319L;

}
