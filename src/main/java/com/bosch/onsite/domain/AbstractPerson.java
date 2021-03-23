package com.bosch.onsite.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bosch.onsite.Const;
import com.bosch.onsite.repository.ClinicRepository;

abstract class AbstractPerson extends AbstractClinicPersistable {

	private static final long serialVersionUID = -16097932419096001L;

	private Map<Integer, Visit> visits;

	AbstractPerson(Long id, String name, String secondName, ClinicRepository repo) {
		super(repo);
		this.id = id;
		metadata.put(Const.NAME, name);
		metadata.put(Const.SECOND_NAME, secondName);
	}

	private void initVisits() {
		if (visits == null) {
			fetchVisits();
		}
	}

	public boolean hasFreeTermins() {
		initVisits();
		return visits.size() < Const.VISITS_PER_DAY;
	}

	public void fetchVisits() {
		visits = repo.getVisitsForDoctor(id.longValue())
				.stream()
				.collect(Collectors.toMap(v -> Integer.valueOf(v.getTimeSlotIdx()), v -> v));
	}

	public List<Integer> getFreeTermins() {
		initVisits();
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < Const.VISITS_PER_DAY; i++) {
			Integer key = Integer.valueOf(i);
			if (!visits.containsKey(key)) {
				result.add(key);
			}
		}
		return result;
	}

	public String getName() { // NO_UCD (unused code)
		return metadata.get(Const.NAME);
	}

	public String getSecondName() { // NO_UCD (unused code)
		return metadata.get(Const.SECOND_NAME);
	}

}
