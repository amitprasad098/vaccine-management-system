package com.project.vaccinemanagement.service;

import java.time.LocalDate;

import com.project.vaccinemanagement.exceptions.BothShotsTakenException;
import com.project.vaccinemanagement.exceptions.FirstShotNotTakenException;
import com.project.vaccinemanagement.exceptions.SecondShotNotEligibleException;
import com.project.vaccinemanagement.exceptions.VaccineUnavailableException;

public interface IShotManagementService {

	void firstShot(String aadharNumber, LocalDate date);

	void secondShot(String aadharNumber, LocalDate date) throws SecondShotNotEligibleException,
			FirstShotNotTakenException, BothShotsTakenException, VaccineUnavailableException;

}
