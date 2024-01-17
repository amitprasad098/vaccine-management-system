package com.project.vaccinemanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.project.vaccinemanagement.entities.Person;
import com.project.vaccinemanagement.entities.Vaccine;
import com.project.vaccinemanagement.exceptions.BothShotsTakenException;
import com.project.vaccinemanagement.exceptions.FirstShotAlreadyTakenException;
import com.project.vaccinemanagement.exceptions.FirstShotNotTakenException;
import com.project.vaccinemanagement.exceptions.SecondShotNotEligibleException;
import com.project.vaccinemanagement.exceptions.VaccineUnavailableException;
import com.project.vaccinemanagement.repositories.PersonRepository;
import com.project.vaccinemanagement.repositories.VaccineRepository;
import com.project.vaccinemanagement.service.ShotManagementServiceImpl;
import com.project.vaccinemanagement.service.VaccineStockManagementServiceImpl;

@ExtendWith(MockitoExtension.class)
class ShotManagementTest {

	@InjectMocks
	ShotManagementServiceImpl management;

	@Mock
	PersonRepository personRepository;

	@Mock
	VaccineRepository vaccineRepository;

	@Mock
	VaccineStockManagementServiceImpl stockManagement;

	@Test
	void firstShot() throws FirstShotAlreadyTakenException {
		Vaccine vaccine = new Vaccine();
		vaccine.setBalance(12);
		List<Vaccine> vaccineList = new ArrayList<>();
		vaccineList.add(vaccine);
		when(vaccineRepository.getLastInsertedVaccine(PageRequest.of(0, 1, Sort.by("date").descending())))
				.thenReturn(vaccineList);
		String aadharNumber = "123456789012";
		LocalDate date = LocalDate.parse("2021-06-10");
		Person person = new Person();
		person.setAadharNumber(aadharNumber);
		when(personRepository.findByAadharNumber(aadharNumber)).thenReturn(person);
		management.firstShot(aadharNumber, date);
		person.setShotOneDate(date);

	}

	@Test
	void secondShotNotEligibilityTest() {

		String aadharNumber = "123456789012";
		LocalDate date = LocalDate.now();
		Person person = new Person();
		person.setAadharNumber(aadharNumber);
		person.setShotOneDate(date);
		when(personRepository.findById(aadharNumber)).thenReturn(Optional.empty());
		assertThrows(FirstShotNotTakenException.class, () -> management.secondShot(aadharNumber, date));
		when(personRepository.findById(aadharNumber)).thenReturn(Optional.ofNullable(person));
		when(stockManagement.availaibleVaccineCount()).thenReturn(0L);
		assertThrows(VaccineUnavailableException.class, () -> management.secondShot(aadharNumber, date));
		when(stockManagement.availaibleVaccineCount()).thenReturn(1L);
		assertThrows(SecondShotNotEligibleException.class, () -> management.secondShot(aadharNumber, date));
		person.setShotTwoDate(date);
		when(personRepository.findById(aadharNumber)).thenReturn(Optional.ofNullable(person));
		assertThrows(BothShotsTakenException.class, () -> management.secondShot(aadharNumber, date));

	}

	@Test
	void secondShotTest() throws SecondShotNotEligibleException, FirstShotNotTakenException, BothShotsTakenException,
			VaccineUnavailableException {
		Vaccine vaccine = new Vaccine();
		vaccine.setBalance(12);
		List<Vaccine> vaccineList = new ArrayList<>();
		vaccineList.add(vaccine);
		when(stockManagement.availaibleVaccineCount()).thenReturn(1L);
		when(vaccineRepository.getLastInsertedVaccine(PageRequest.of(0, 1, Sort.by("date").descending())))
				.thenReturn(vaccineList);
		String aadharNumber = "123456789012";
		LocalDate date = LocalDate.parse("2021-06-10");
		Person person = new Person();
		person.setAadharNumber(aadharNumber);
		person.setShotOneDate(date);
		when(personRepository.findById(aadharNumber)).thenReturn(Optional.ofNullable(person));
		management.secondShot(aadharNumber, LocalDate.now());
		verify(personRepository, atLeastOnce()).findById(aadharNumber);
	}
}
