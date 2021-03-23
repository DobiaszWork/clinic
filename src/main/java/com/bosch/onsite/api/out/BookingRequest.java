package com.bosch.onsite.api.out;

public class BookingRequest {

	public Long getPatientId() {
		return patientId;
	}

	public int getTimeSlotIdx() {
		return timeSlotIdx;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	private Long patientId;
	private int timeSlotIdx;
	private Long doctorId;

}
