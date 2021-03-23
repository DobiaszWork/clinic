package com.bosch.onsite.domain;

import java.io.Serializable;

import com.bosch.onsite.repository.ClinicRepository;

public class AbstractClinicPersistable implements Serializable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private static final long serialVersionUID = -3792021885650281763L;

	protected Long id;
	protected transient ClinicRepository repo;
	protected Metadata metadata = new Metadata();

	AbstractClinicPersistable(ClinicRepository repo) {
		super();
		this.repo = repo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass().equals(this.getClass())) {
			return ((AbstractClinicPersistable) obj).id == id;
		}
		return false;
	}

	public void setRepository(ClinicRepository repo) {
		this.repo = repo;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":" + id;
	}

}
