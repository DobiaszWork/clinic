package com.bosch.onsite.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bosch.onsite.api.out.BookingRequest;
import com.bosch.onsite.api.out.BookingResult;
import com.bosch.onsite.domain.Doctor;
import com.bosch.onsite.domain.Visit;
import com.bosch.onsite.repository.ClinicRepository;
import com.bosch.onsite.repository.ClinicRepositoryException;

@RestController
public class ClinicController { // NO_UCD (unused code)

	@Autowired
	private ClinicRepository repo;

	@PostMapping(value = "/patient/visit/")
	public BookingResult bookVisit(@RequestBody BookingRequest request) throws FileNotFoundException, IOException {
		BookingResult result = new BookingResult();
		try {
			result.setVisit(repo.bookTermin(request.getPatientId(), request.getDoctorId(), request.getTimeSlotIdx()));
		} catch (ClinicRepositoryException e) {
			result.setError(e.getError());
		}
		return result;
	}

	@DeleteMapping(value = "/patient/visit/")
	public BookingResult cancelVisit(@RequestBody BookingRequest request) throws FileNotFoundException, IOException {
		BookingResult result = new BookingResult();
		try {
			result.setVisit(repo.cancelTermin(request.getPatientId(), request.getDoctorId(), request.getTimeSlotIdx()));
		} catch (ClinicRepositoryException e) {
			result.setError(e.getError());
		}
		return result;
	}

	@GetMapping(value = "/doctor/list")
	public Set<Doctor> getAvailableDoctors() {
		return repo.getAvailableDoctors();
	}

	@GetMapping(value = "/doctor/{id}/visits")
	public Set<Visit> listDoctorVisits(@PathVariable long id) {
		return repo.getVisitsForDoctor(id);
	}

	@GetMapping(value = "/patient/{id}/visits")
	public Set<Visit> listPatientVisits(@PathVariable long id) {
		return repo.getVisitsForPatient(id);
	}

	/**
	 * Test-only method for resetting repo.
	 */
	public void resetVisits() {
		repo.resetVisits();
	}

}
