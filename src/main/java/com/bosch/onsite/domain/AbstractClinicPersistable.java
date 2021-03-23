package com.bosch.onsite.domain;

import java.io.Serializable;

public class AbstractClinicPersistable implements Serializable {

	private static final long serialVersionUID = -3792021885650281763L;

	protected Long id;
	protected Metadata metadata = new Metadata();

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

	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":" + id;
	}

}
