package com.project.vaccinemanagement.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.vaccinemanagement.entities.Person;
import com.project.vaccinemanagement.entities.Vaccine;
import com.project.vaccinemanagement.exceptions.BothShotsTakenException;
import com.project.vaccinemanagement.exceptions.FirstShotNotTakenException;
import com.project.vaccinemanagement.exceptions.SecondShotNotEligibleException;
import com.project.vaccinemanagement.exceptions.VaccineUnavailableException;
import com.project.vaccinemanagement.repositories.PersonRepository;
import com.project.vaccinemanagement.repositories.VaccineRepository;

@Service
public class ShotManagementServiceImpl implements IShotManagementService {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	VaccineRepository vaccineRepository;
	@Autowired
	IVaccineStockManagementService stockManagement;


	private void updateBalance() {
		Vaccine vaccine = vaccineRepository.getLastInsertedVaccine(PageRequest.of(0, 1, Sort.by("date").descending())).get(0);
		vaccine.setBalance(vaccine.getBalance()-1);
		vaccineRepository.save(vaccine);
	}

	private Person getPerson(String aadharNumber) throws FirstShotNotTakenException {
		Optional<Person> personOptional = personRepository.findById(aadharNumber);
		return personOptional.orElseThrow(FirstShotNotTakenException::new);
	}

	@Override
	public void firstShot(String aadharNumber, LocalDate date){
		Person person = personRepository.findByAadharNumber(aadharNumber);
		person.setShotOneDate(date);
		personRepository.save(person);
		updateBalance();
	}

	private boolean checkSecondShotTaken(Person person) {
		Optional<LocalDate> shotTwoDateOptional = Optional.ofNullable(person.getShotTwoDate());
		return shotTwoDateOptional.isPresent();
	}

	private void updateSecondShot(Person person, LocalDate date) throws SecondShotNotEligibleException {
		LocalDate eligibleDate = person.getShotOneDate().plusDays(28);
		if (eligibleDate.compareTo(date)<=0) {
			person.setShotTwoDate(date);
			personRepository.save(person);
			updateBalance();
		} else {
			throw new SecondShotNotEligibleException(eligibleDate.toString());
		}
	}

	@Override
	public void secondShot(String aadharNumber, LocalDate date)
			throws SecondShotNotEligibleException, FirstShotNotTakenException, BothShotsTakenException, VaccineUnavailableException {
		Person person = getPerson(aadharNumber);
		if(stockManagement.availaibleVaccineCount()>0) {
		if (checkSecondShotTaken(person))
			throw new BothShotsTakenException();
		else
			updateSecondShot(person,date);
		}
		else {
			throw new VaccineUnavailableException();
		}
	}
}
