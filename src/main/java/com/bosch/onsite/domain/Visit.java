package com.bosch.onsite.domain;

public class Visit extends AbstractClinicPersistable {

	private static final long serialVersionUID = -1635458454573514410L;

	private final long patientId;
	private final long doctorId;
	private final int timeSlotIdx;
	private Doctor doctor;
	private Patient patient;

	public Visit(long patientId, long doctorId, int timeSlotIdx) {
		super();
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.timeSlotIdx = timeSlotIdx;
	}

	public Doctor getDoctor() { // NO_UCD (unused code)
		return doctor;
	}

	public long getDoctorId() { // NO_UCD (use default)
		return doctorId;
	}

	public Patient getPatient() { // NO_UCD (unused code)
		return patient;
	}

	public long getPatientId() { // NO_UCD (use default)
		return patientId;
	}

	public int getTimeSlotIdx() { // NO_UCD (use default)
		return timeSlotIdx;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

}
