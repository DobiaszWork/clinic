package com.bosch.onsite.api.out;

public class BookingRequest {

	private Long patientId;
	private int timeSlotIdx;
	private Long doctorId;

	public Long getDoctorId() {
		return doctorId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public int getTimeSlotIdx() {
		return timeSlotIdx;
	}

}
