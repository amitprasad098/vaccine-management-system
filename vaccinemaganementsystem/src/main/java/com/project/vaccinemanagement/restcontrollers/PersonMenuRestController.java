package com.project.vaccinemanagement.restcontrollers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.vaccinemanagement.dto.PersonDto;
import com.project.vaccinemanagement.exceptions.BothShotsTakenException;
import com.project.vaccinemanagement.exceptions.FirstShotAlreadyTakenException;
import com.project.vaccinemanagement.exceptions.FirstShotNotTakenException;
import com.project.vaccinemanagement.exceptions.SecondShotNotEligibleException;
import com.project.vaccinemanagement.exceptions.VaccineUnavailableException;
import com.project.vaccinemanagement.service.IPersonDetailsManagementService;
import com.project.vaccinemanagement.service.IShotManagementService;
import com.project.vaccinemanagement.service.IVaccineStockManagementService;

import io.swagger.annotations.ApiOperation;

@RestController
@Validated
public class PersonMenuRestController {
	@Autowired
	IPersonDetailsManagementService personDetailsManagement;
	@Autowired
	IShotManagementService shotManagement;
	@Autowired
	IVaccineStockManagementService stockManagement;

	private static final String FIRST_SHOT_SUCCESS = "first shot Details inserted";
	private static final String SECOND_SHOT_SUCCESS = "second shot Details updated";
	private Map<String,String> acknowledgement = new HashMap<>();
	
	@PostMapping("save_person_rest")
	@ApiOperation(value = "saves the first shot data of person")
	public ResponseEntity<Map<String,String>> firstShotSavePerson(@RequestBody @Valid PersonDto personDto) throws FirstShotAlreadyTakenException, VaccineUnavailableException
{
		personDetailsManagement.registerDetails(personDto);
		shotManagement.firstShot(personDto.getAadharNumber(), LocalDate.parse(personDto.getShotOneDate()));
		acknowledgement.put("message", FIRST_SHOT_SUCCESS);
		return new ResponseEntity<>(acknowledgement, HttpStatus.OK);
	}

	@PostMapping("update_person_rest")
	@ApiOperation(value = "updates the Second shot data of person")
	public ResponseEntity<Map<String,String>> secondShotUpdatePerson(
			@RequestParam(name = "aadharNumber") @Size(min = 12, max = 12, message = "invalid aadhar")String aadharNumber,
			@RequestParam(name = "shotDate") String shotDate) throws SecondShotNotEligibleException,
			FirstShotNotTakenException, BothShotsTakenException, VaccineUnavailableException {
		shotManagement.secondShot(aadharNumber, LocalDate.parse(shotDate));
		acknowledgement.put("message", SECOND_SHOT_SUCCESS);
		return new ResponseEntity<>(acknowledgement, HttpStatus.OK);
	}

	@GetMapping("report_rest")
	@ApiOperation(value="gives the report of persons who got vaccinenated on particular date")
	public ResponseEntity<List<PersonDto>> getReport(@RequestParam(name = "date") String date) {
		return new ResponseEntity<>(personDetailsManagement.getReport(LocalDate.parse(date)), HttpStatus.OK);
	}

}
