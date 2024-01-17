package com.project.vaccinemanagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.vaccinemanagement.dto.PersonDto;
import com.project.vaccinemanagement.entities.Address;
import com.project.vaccinemanagement.entities.Person;
import com.project.vaccinemanagement.exceptions.FirstShotAlreadyTakenException;
import com.project.vaccinemanagement.exceptions.VaccineUnavailableException;
import com.project.vaccinemanagement.repositories.PersonRepository;

@Service
public class PersonDetailsManagementServiceImpl implements IPersonDetailsManagementService {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	IVaccineStockManagementService stockManagement;
	private ModelMapper mapper = new ModelMapper();

	@Override
	public void registerDetails(PersonDto personDto)
			throws FirstShotAlreadyTakenException, VaccineUnavailableException {
		if (stockManagement.availaibleVaccineCount() > 0) {
			if (!personRepository.existsById(personDto.getAadharNumber())) {
				personRepository.save(setPerson(personDto));
			} else {
				throw new FirstShotAlreadyTakenException();
			}
		} else {
			throw new VaccineUnavailableException();
		}
	}

	private Person setPerson(PersonDto personDto) {
		Person person = mapper.map(personDto, Person.class);
		List<Address> addresses = new ArrayList<>();
		Address addressOne = new Address();
		addressOne.setPersonAddress(personDto.getAddressOne());
		addresses.add(addressOne);
		Address addressTwo = new Address();
		addressTwo.setPersonAddress(personDto.getAddressTwo());
		addresses.add(addressTwo);
		person.setAddresses(addresses);
		return person;
	}

	private PersonDto setPersonDto(Person person) {
		PersonDto personDto = mapper.map(person, PersonDto.class);
		personDto.setShotOneDate(person.getShotOneDate().toString());
		if (person.getShotTwoDate() != null)
			personDto.setShotTwoDate(person.getShotTwoDate().toString());
		List<Address> addresses = person.getAddresses();
		personDto.setAddressOne(addresses.get(0).getPersonAddress());
		personDto.setAddressTwo(addresses.get(1).getPersonAddress());
		return personDto;
	}

	@Override
	public List<PersonDto> getReport(LocalDate date) {
		List<PersonDto> personDtoList = new ArrayList<>();
		List<Person> persons = personRepository.findByShotOneDateOrShotTwoDate(date, date);
		persons.forEach(person -> personDtoList.add(setPersonDto(person)));
		return personDtoList;
	}
}
