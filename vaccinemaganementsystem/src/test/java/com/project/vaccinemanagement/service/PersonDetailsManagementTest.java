package com.project.vaccinemanagement.service;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import org.modelmapper.ModelMapper;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.vaccinemanagement.dto.PersonDto;
import com.project.vaccinemanagement.entities.Address;
import com.project.vaccinemanagement.entities.Person;
import com.project.vaccinemanagement.exceptions.FirstShotAlreadyTakenException;
import com.project.vaccinemanagement.exceptions.VaccineUnavailableException;
import com.project.vaccinemanagement.repositories.PersonRepository;
import com.project.vaccinemanagement.service.PersonDetailsManagementServiceImpl;
import com.project.vaccinemanagement.service.VaccineStockManagementServiceImpl;
@ExtendWith(MockitoExtension.class)
class PersonDetailsManagementTest {
	
	@InjectMocks
	PersonDetailsManagementServiceImpl personDetailsManagement;
	@Mock
	VaccineStockManagementServiceImpl stockManagement;
	@Mock
	PersonRepository personRepository;
	@Mock
	ModelMapper mapper;
	@Test
	void registerDetailsTest() throws FirstShotAlreadyTakenException, VaccineUnavailableException{
		PersonDto personDto = new PersonDto();
		personDto.setAadharNumber("123456789011");
		personDto.setName("reshma");
		personDto.setAge(18);
		personDto.setAddressOne("eluru");
		personDto.setAddressTwo("hyd");
		Person person = new Person();
		when(stockManagement.availaibleVaccineCount()).thenReturn(0L);
		assertThrows(VaccineUnavailableException.class,()-> personDetailsManagement.registerDetails(personDto));
		when(stockManagement.availaibleVaccineCount()).thenReturn(1L);
		when(mapper.map(personDto, Person.class)).thenReturn(person);
		when(personRepository.existsById("123456789011")).thenReturn(false);
		personDetailsManagement.registerDetails(personDto);
		verify(personRepository,atLeastOnce()).existsById("123456789011");
		when(personRepository.existsById("123456789011")).thenReturn(true);
		assertThrows(FirstShotAlreadyTakenException.class,()-> personDetailsManagement.registerDetails(personDto));
		verify(personRepository,atLeastOnce()).save(person);
	}
	
	@Test
	void getReportTest() {
		List<Person> persons = new ArrayList<>();
		LocalDate date = LocalDate.parse("2021-06-10");
		Person personOne = new Person();
		personOne.setAadharNumber("123456789012");
		personOne.setName("reshma");
		personOne.setShotOneDate(date);		
		personOne.setAge(21);
		List<Address> addresses = new ArrayList<>();
		Address addressOne = new Address();
		addressOne.setPersonAddress("eluru");
		addresses.add(addressOne);
		Address addressTwo = new Address();
		addressTwo.setPersonAddress("eluru");
		addresses.add(addressTwo);
		personOne.setAddresses(addresses);
		personOne.setShotOneDate(date);
		persons.add(personOne);
		PersonDto personDto = new ModelMapper().map(personOne,PersonDto.class);
		when(mapper.map(personOne, PersonDto.class)).thenReturn(personDto);
		when(personRepository.findByShotOneDateOrShotTwoDate(date, date)).thenReturn(persons);
		personDetailsManagement.getReport(date);
		personOne.setShotTwoDate(date);
		personDetailsManagement.getReport(date);
		verify(personRepository,atLeastOnce()).findByShotOneDateOrShotTwoDate(date, date);
	}
}
