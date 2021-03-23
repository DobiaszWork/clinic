package com.bosch.onsite.api.out;

public class BookingRequest {

	private Long patientId;

	private int timeSlotIdx;

	private Long doctorId;

	public BookingRequest() {
		super();
	}

	public BookingRequest(Long patientId, Long doctorId, int timeSlotIdx) { // NO_UCD (test only)
		super();
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.timeSlotIdx = timeSlotIdx;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public int getTimeSlotIdx() {
		return timeSlotIdx;
	}

	public void setDoctorId(Long doctorId) { // NO_UCD (test only)
		this.doctorId = doctorId;
	}

	public void setPatientId(Long patientId) { // NO_UCD (test only)
		this.patientId = patientId;
	}

	public void setTimeSlotIdx(int timeSlotIdx) { // NO_UCD (test only)
		this.timeSlotIdx = timeSlotIdx;
	}

}
