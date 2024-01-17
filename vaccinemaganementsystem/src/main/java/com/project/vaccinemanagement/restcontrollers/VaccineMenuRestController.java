package com.project.vaccinemanagement.restcontrollers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.vaccinemanagement.dto.VaccineDto;
import com.project.vaccinemanagement.service.IVaccineStockManagementService;

import io.swagger.annotations.ApiOperation;

@RestController
public class VaccineMenuRestController {
	@Autowired
	IVaccineStockManagementService stockManagement;
	private static final String INSERT_SUCCESS = "vaccine stock added successfully";
	private static final String AVAILABLE_VACCINE = "Available vaccines are: ";
	
	@PostMapping("insert_stock_rest")
	@ApiOperation(value = "insert's the stock")
	public  ResponseEntity<Map<String,String>> insertStock(@RequestBody @Valid VaccineDto vaccineDto) {
		stockManagement.storeStock(vaccineDto);
		Map<String,String> message = new HashMap<>();
		message.put("message",INSERT_SUCCESS);
		return new ResponseEntity<>(message,HttpStatus.OK);
	}

	@GetMapping("vaccine_balance_rest")
	@ApiOperation(value = "gives the vaccine balance")
	public ResponseEntity<Map<String,Long>> getvaccineBalance() {
		long availableCount = stockManagement.availaibleVaccineCount();
		Map<String,Long> message = new HashMap<>();
		message.put(AVAILABLE_VACCINE,availableCount);
		return new ResponseEntity<>(message,HttpStatus.OK);
	}
}
