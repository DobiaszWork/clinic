package com.bosch.onsite.domain;

import com.bosch.onsite.repository.ClinicRepository;

public class Visit extends AbstractClinicPersistable {

	public Patient getPatient() { // NO_UCD (unused code)
		return patient;
	}

	public Doctor getDoctor() { // NO_UCD (unused code)
		return doctor;
	}

	public long getPatientId() { // NO_UCD (use default)
		return patientId;
	}

	public int getTimeSlotIdx() { // NO_UCD (use default)
		return timeSlotIdx;
	}

	private static final long serialVersionUID = -1635458454573514410L;

	private long patientId;
	private long doctorId;
	private int timeSlotIdx;
	private Doctor doctor;
	private Patient patient;

	public Visit(long patientId, long doctorId, int timeSlotIdx, ClinicRepository repo) {
		super(repo);
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.timeSlotIdx = timeSlotIdx;
	}

	public long getDoctorId() { // NO_UCD (use default)
		return doctorId;
	}

	public void fetchDoctor() {
		doctor = repo.getDoctorById(doctorId);
	}

	public void fetchPatient() {
		patient = repo.getPatientById(patientId);
	}

}
