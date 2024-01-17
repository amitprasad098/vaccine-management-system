package com.project.vaccinemanagement.restcontrollers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vaccinemanagement.VaccinemanagementsystemApplication;
import com.project.vaccinemanagement.dto.VaccineDto;

@AutoConfigureMockMvc
@SpringBootTest(classes = { VaccinemanagementsystemApplication.class })
public class InvalidInputTest {
	@Autowired
	private MockMvc mvc;
	
	private ObjectMapper mapper = new ObjectMapper();
	@Test
	void invalidDateTest() throws Exception {
		VaccineDto vaccineDto = new VaccineDto();
		vaccineDto.setDate("2021-15-07");
		vaccineDto.setName("covaxin");
		vaccineDto.setInitial(1);
		String jsonString = mapper.writeValueAsString(vaccineDto);
		Map<String, String> invalidDateError = new HashMap<>();
		invalidDateError.put("error", "invalid date");
		String invalidDateErrorJson = mapper.writeValueAsString(invalidDateError);
		mvc.perform(post("/insert_stock_rest").contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(status().isBadRequest()).andExpect(content().json(invalidDateErrorJson));

	}

	@Test
	void methodArgumentNotValidTest() throws Exception {
		VaccineDto vaccineDto = new VaccineDto();
		vaccineDto.setDate("2021-15-07");
		vaccineDto.setName("covaxin");
		vaccineDto.setInitial(-1);
		String jsonString = mapper.writeValueAsString(vaccineDto);
		List<String> errorList = new ArrayList<>();
		errorList.add("initial must be greater than zero");
		String errorListJson = new ObjectMapper().writeValueAsString(errorList);
		mvc.perform(post("/insert_stock_rest").contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(status().isBadRequest()).andExpect(content().json(errorListJson));

	}

	@Test
	void constraintViolationTest() throws Exception {
		String date = "2021-08-05";
		String aadharNumber = "12345678901";
		List<String> errorList = new ArrayList<>();
		errorList.add("secondShotUpdatePerson.aadharNumber: invalid aadhar");
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("secondShotUpdatePerson.aadharNumber", "invalid aadhar");
		String errorListJson = mapper.writeValueAsString(errorList);
				mvc.perform(post("/update_person_rest").param("aadharNumber", aadharNumber).param("shotDate", date))
				.andExpect(status().isBadRequest()).andExpect(content().string(errorListJson));
	}
}
