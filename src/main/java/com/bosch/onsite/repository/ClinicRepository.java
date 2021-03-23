package com.bosch.onsite.repository;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bosch.onsite.Const;
import com.bosch.onsite.domain.AbstractClinicPersistable;
import com.bosch.onsite.domain.Doctor;
import com.bosch.onsite.domain.Patient;
import com.bosch.onsite.domain.Visit;
import com.bosch.onsite.repository.ClinicRepositoryException.Error;

@Service
public class ClinicRepository {

	private static class Doctors extends HashMap<Long, Doctor> {

		private static final long serialVersionUID = 6110114473600174719L;

		public void fetchVisits(ClinicRepository repo) {
			values().forEach(d -> d.setVisits(repo.getVisitsForDoctor(d.getId().longValue())));
		}

	}

	private static class Patients extends HashMap<Long, Patient> {

		private static final long serialVersionUID = -2556621535127724176L;

		public void fetchVisits(ClinicRepository repo) {
			values().forEach(p -> p.setVisits(repo.getVisitsForPatient(p.getId().longValue())));
		}

	}

	private static class Visits extends HashMap<Long, Visit> {

		private static final long serialVersionUID = -3228726547387920554L;

		public void put(Visit visit) {
			if (visit.getId() == null) {
				visit.setId(Long.valueOf(size()));
			}
			put(visit.getId(), visit);
		}

	}

	private static final String DATA = ".data";

	@SuppressWarnings("unchecked")
	private static <T> T readObject(Path path) throws FileNotFoundException, IOException, ClassNotFoundException {
		final File file = path.toFile();
		if (file.exists()) {
			try (ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
				return (T) input.readObject();
			}
		}
		return null;
	}

	private static <T extends Map<Long, ? extends AbstractClinicPersistable>> T readRepo(Class<T> clazz) {
		Path pDoctors = Paths.get(clazz.getSimpleName() + DATA);
		if (Files.exists(pDoctors)) {
			try {
				return readObject(pDoctors);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static void saveRepo(Serializable object) throws FileNotFoundException, IOException {
		try (OutputStream fos = new FileOutputStream(Paths.get(object.getClass().getSimpleName() + DATA).toFile(), false);
				OutputStream bos = new BufferedOutputStream(fos);
				ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(object);
		}
	}

	private static void validateBookRequest(Patient p, Doctor d, int timeSlotIdx) throws ClinicRepositoryException {
		if (p == null) {
			throw new ClinicRepositoryException(Error.NO_PATIENT);
		}
		if (d == null) {
			throw new ClinicRepositoryException(Error.NO_DOCTOR);
		}
		if (timeSlotIdx < 0 || timeSlotIdx >= Const.VISITS_PER_DAY) {
			throw new ClinicRepositoryException(Error.INVALID_TIMESLOT);
		}
	}

	private Doctors doctors;
	private Patients patients;
	private Visits visits;

	public ClinicRepository() throws FileNotFoundException, IOException {
		initRepo();
	}

	@SuppressWarnings("boxing")
	private void initRepo() throws FileNotFoundException, IOException {
		visits = readRepo(Visits.class);
		if (visits == null) {
			visits = new Visits();
			saveRepo(visits);
		}
		doctors = readRepo(Doctors.class);
		if (doctors == null) {
			doctors = new Doctors();
			doctors.put(1L, new Doctor(1L, "Dr", "Dre"));
			doctors.put(2L, new Doctor(2L, "Dr", "Alban"));
			doctors.put(3L, new Doctor(3L, "Dr", "House"));
			saveRepo(doctors);
		}
		patients = readRepo(Patients.class);
		if (patients == null) {
			patients = new Patients();
			patients.put(1L, new Patient(1L, "Eugeniusz", "Cierpliwy"));
			patients.put(2L, new Patient(2L, "Vera", "Unpatient"));
			saveRepo(patients);
		}
		doctors.fetchVisits(this);
		patients.fetchVisits(this);
	}

	Doctor getDoctorById(long doctorId) {
		return doctors.get(Long.valueOf(doctorId));
	}

	Patient getPatientById(long patientId) {
		return patients.get(Long.valueOf(patientId));
	}

	public Visit bookTermin(Long patientId, Long doctorId, int timeSlotIdx)
			throws ClinicRepositoryException, FileNotFoundException, IOException {
		synchronized (this) {
			Patient p = patients.get(patientId);
			Doctor d = doctors.get(doctorId);
			validateBookRequest(p, d, timeSlotIdx);
			if (!d.getFreeTermins().contains(Integer.valueOf(timeSlotIdx))) {
				throw new ClinicRepositoryException(Error.NO_TIMESLOT);
			}
			if (!p.getFreeTermins().contains(Integer.valueOf(timeSlotIdx))) {
				throw new ClinicRepositoryException(Error.NO_TIMESLOT);
			}
			Visit visit = new Visit(patientId.longValue(), doctorId.longValue(), timeSlotIdx);
			visits.put(visit);
			saveRepo(visits);
			d.setVisits(getVisitsForDoctor(doctorId.longValue()));
			p.setVisits(getVisitsForPatient(patientId.longValue()));
			return visit;
		}
	}

	public Visit cancelTermin(Long patientId, Long doctorId, int timeSlotIdx)
			throws ClinicRepositoryException, FileNotFoundException, IOException {
		synchronized (this) {
			Patient p = patients.get(patientId);
			Doctor d = doctors.get(doctorId);
			validateBookRequest(p, d, timeSlotIdx);
			Optional<Entry<Long, Visit>> visit = visits.entrySet()
					.stream()
					.filter(e -> e.getValue().getPatientId() == patientId.longValue()
							&& e.getValue().getDoctorId() == doctorId.longValue()
							&& e.getValue().getTimeSlotIdx() == timeSlotIdx)
					.findFirst();
			if (visit.isEmpty()) {
				throw new ClinicRepositoryException(Error.NO_VISIT);
			}
			visits.remove(visit.get().getKey());
			saveRepo(visits);
			d.setVisits(getVisitsForDoctor(doctorId.longValue()));
			p.setVisits(getVisitsForPatient(patientId.longValue()));
			return visit.get().getValue();
		}
	}

	public Set<Doctor> getAvailableDoctors() {
		return doctors.values().stream().filter(d -> d.hasFreeTermins()).collect(Collectors.toSet());
	}

	public Set<Visit> getVisitsForDoctor(long doctorId) {
		Set<Visit> result = visits.values().stream().filter(v -> v.getDoctorId() == doctorId).collect(Collectors.toSet());
		result.forEach(v -> v.setPatient(getPatientById(v.getPatientId())));
		return result;
	}

	public Set<Visit> getVisitsForPatient(long patientId) {
		Set<Visit> result = visits.values().stream().filter(v -> v.getPatientId() == patientId).collect(Collectors.toSet());
		result.forEach(v -> v.setDoctor(getDoctorById(v.getDoctorId())));
		return result;
	}

	public void resetVisits() {
		visits.clear();
		doctors.fetchVisits(this);
		patients.fetchVisits(this);
	}

}
