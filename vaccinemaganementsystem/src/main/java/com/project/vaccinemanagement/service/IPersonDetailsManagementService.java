package com.project.vaccinemanagement.service;

import java.time.LocalDate;
import java.util.List;

import com.project.vaccinemanagement.dto.PersonDto;
import com.project.vaccinemanagement.exceptions.FirstShotAlreadyTakenException;
import com.project.vaccinemanagement.exceptions.VaccineUnavailableException;

public interface IPersonDetailsManagementService {

	void registerDetails(PersonDto personDto) throws FirstShotAlreadyTakenException, VaccineUnavailableException;

	List<PersonDto> getReport(LocalDate date);

}
