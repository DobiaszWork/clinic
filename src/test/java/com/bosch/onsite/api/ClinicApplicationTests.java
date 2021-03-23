package com.bosch.onsite.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.bosch.onsite.api.out.BookingRequest;
import com.bosch.onsite.api.out.BookingResult;
import com.bosch.onsite.domain.Doctor;
import com.bosch.onsite.domain.Visit;
import com.bosch.onsite.repository.ClinicRepositoryException;

@SpringBootTest
@ComponentScan({ "com.bosch.onsite" })
@SuppressWarnings("boxing")
class ClinicApplicationTests {

	@Autowired
	private ClinicController api;

	@BeforeEach
	void setUp() {
		api.resetVisits();
	}

	@Test
	void getDoctorsTest() {
		Set<Doctor> result = api.getAvailableDoctors();
		assertEquals(result.size(), 3);
	}

	@Test
	void bookAndCancelVisitTest() throws FileNotFoundException, IOException {
		
		// empty booking request
		BookingRequest req = new BookingRequest();
		BookingResult resp = api.bookVisit(req);
		assertEquals(resp.getError(), ClinicRepositoryException.Error.NO_PATIENT.getText());
		
		// wrong doctor ID
		req.setPatientId(1L);
		resp = api.bookVisit(req);
		assertEquals(resp.getError(), ClinicRepositoryException.Error.NO_DOCTOR.getText());
		
		// wrong timeslot idx
		req.setDoctorId(2L);
		req.setTimeSlotIdx(-1);
		resp = api.bookVisit(req);
		assertEquals(resp.getError(), ClinicRepositoryException.Error.INVALID_TIMESLOT.getText());
		
		// proper data
		req.setTimeSlotIdx(3);
		resp = api.bookVisit(req);
		assertNotNull(resp.getVisit());
		assertEquals(resp.getVisit().getPatientId(), 1);
		assertEquals(resp.getVisit().getDoctorId(), 2);
		assertEquals(resp.getVisit().getTimeSlotIdx(), 3);
		
		// check visits list for doctor
		Set<Visit> visits = api.listDoctorVisits(2L);
		assertEquals(visits.size(), 1);
		assertEquals(visits.iterator().next().getPatientId(), 1);
		assertEquals(visits.iterator().next().getDoctorId(), 2);
		
		// check visits list for patient
		visits = api.listPatientVisits(1L);
		assertEquals(visits.size(), 1);
		assertEquals(visits.iterator().next().getPatientId(), 1);
		assertEquals(visits.iterator().next().getDoctorId(), 2);
		
		// empty cancel request
		req = new BookingRequest();
		resp = api.cancelVisit(req);
		assertEquals(resp.getError(), ClinicRepositoryException.Error.NO_PATIENT.getText());
		
		// wrong doctor ID
		req.setPatientId(1L);
		resp = api.cancelVisit(req);
		
		// wrong timeslot idx
		req.setDoctorId(2L);
		req.setTimeSlotIdx(-1);
		resp = api.cancelVisit(req);
		assertEquals(resp.getError(), ClinicRepositoryException.Error.INVALID_TIMESLOT.getText());
		
		// valid data, but no such visit
		req.setTimeSlotIdx(4);
		resp = api.cancelVisit(req);
		assertEquals(resp.getError(), ClinicRepositoryException.Error.NO_VISIT.getText());
		
		// proper cancel request
		req.setTimeSlotIdx(3);
		resp = api.cancelVisit(req);
		assertNotNull(resp.getVisit());
		assertEquals(resp.getVisit().getPatientId(), 1);
		assertEquals(resp.getVisit().getDoctorId(), 2);
		assertEquals(resp.getVisit().getTimeSlotIdx(), 3);
		
		// check visits list for doctor
		visits = api.listDoctorVisits(2L);
		assertTrue(visits.isEmpty());
		
		// check visits list for patient
		visits = api.listPatientVisits(1L);
		assertTrue(visits.isEmpty());
	}

	@Test
	void timeSlotConflictsTest() throws FileNotFoundException, IOException {
		api.bookVisit(new BookingRequest(1L, 2L, 3));
		
		// conflicting timeslot for doctor 2
		BookingResult resp = api.bookVisit(new BookingRequest(2L, 2L, 3));
		assertEquals(resp.getError(), ClinicRepositoryException.Error.NO_TIMESLOT.getText());
		
		// conflicting timeslot for patient 1
		resp = api.bookVisit(new BookingRequest(1L, 1L, 3));
		assertEquals(resp.getError(), ClinicRepositoryException.Error.NO_TIMESLOT.getText());
		
		// no conflict - book second visit for patient 1
		resp = api.bookVisit(new BookingRequest(1L, 1L, 4));
		assertNotNull(resp.getVisit());
		assertEquals(resp.getVisit().getPatientId(), 1);
		assertEquals(resp.getVisit().getDoctorId(), 1);
		assertEquals(resp.getVisit().getTimeSlotIdx(), 4);
		
		// no conflict - book second visit for doctor 2
		resp = api.bookVisit(new BookingRequest(2L, 2L, 2));
		assertNotNull(resp.getVisit());
		assertEquals(resp.getVisit().getPatientId(), 2);
		assertEquals(resp.getVisit().getDoctorId(), 2);
		assertEquals(resp.getVisit().getTimeSlotIdx(), 2);
	}

}
