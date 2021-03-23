package com.bosch.onsite.domain;

import com.bosch.onsite.repository.ClinicRepository;

public class Doctor extends AbstractPerson {

	public Doctor(Long id, String name, String secondName, ClinicRepository repo) {
		super(id, name, secondName, repo);
	}

	private static final long serialVersionUID = -2270752613237755530L;

}
