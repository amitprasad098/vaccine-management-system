package com.project.vaccinemanagement.restcontrollers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vaccinemanagement.VaccinemanagementsystemApplication;
import com.project.vaccinemanagement.dto.PersonDto;
import com.project.vaccinemanagement.exceptions.BothShotsTakenException;
import com.project.vaccinemanagement.exceptions.FirstShotAlreadyTakenException;
import com.project.vaccinemanagement.exceptions.FirstShotNotTakenException;
import com.project.vaccinemanagement.exceptions.SecondShotNotEligibleException;
import com.project.vaccinemanagement.exceptions.VaccineUnavailableException;
import com.project.vaccinemanagement.service.PersonDetailsManagementServiceImpl;
import com.project.vaccinemanagement.service.ShotManagementServiceImpl;
import com.project.vaccinemanagement.service.VaccineStockManagementServiceImpl;

@AutoConfigureMockMvc
@SpringBootTest(classes = { VaccinemanagementsystemApplication.class })
public class PersonMenuRestControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	PersonDetailsManagementServiceImpl detailsManagement;

	@MockBean
	ShotManagementServiceImpl shotManagement;

	@MockBean
	VaccineStockManagementServiceImpl stockManagement;

	private static PersonDto personDto = new PersonDto();
	private static String personDtoJson;
	private static ObjectMapper mapper = new ObjectMapper();
	private String date = "2021-08-05";
	private LocalDate parseDate = LocalDate.parse(date);
	private String aadharNumber = "123456789012";

	@BeforeAll()
	static void personDtoSetup() throws JsonProcessingException {
		personDto.setAadharNumber("123456789012");
		personDto.setName("reshma");
		personDto.setAge(10);
		personDto.setShotOneDate("2021-08-07");
		personDto.setAddressOne("eluru");
		personDto.setAddressTwo("hyd");

		personDtoJson = mapper.writeValueAsString(personDto);
	}

	@Test
	void savePersonSuccessTest() throws Exception {

		doNothing().when(detailsManagement).registerDetails(ArgumentMatchers.any(PersonDto.class));
		Map<String, String> firstShotSuccessMessage = new HashMap<>();
		firstShotSuccessMessage.put("message", "first shot Details inserted");
		String firstShotSuccessMessageJson = mapper.writeValueAsString(firstShotSuccessMessage);
		mvc.perform(post("/save_person_rest").contentType(MediaType.APPLICATION_JSON).content(personDtoJson))
				.andExpect(status().isOk()).andExpect(content().json(firstShotSuccessMessageJson));

	}

	@Test
	void savePersonUnavailableVaccineTest() throws Exception {
		Map<String, String> vaccineUnvailableError = new HashMap<>();
		vaccineUnvailableError.put("error", "Vaccine is unavailable");
		String vaccineUnvailableErrorJson = mapper.writeValueAsString(vaccineUnvailableError);
		doThrow(VaccineUnavailableException.class).when(detailsManagement)
				.registerDetails(ArgumentMatchers.any(PersonDto.class));
		mvc.perform(post("/save_person_rest").contentType(MediaType.APPLICATION_JSON).content(personDtoJson))
				.andExpect(status().isBadRequest()).andExpect(content().json(vaccineUnvailableErrorJson));
	}

	@Test
	void savePersonFirstShotAlreadyTakenTest() throws Exception {
		Map<String, String> firstShotAlreadyTakenError = new HashMap<>();
		firstShotAlreadyTakenError.put("error", "Not eligible, first shot already taken");
		String firstShotAlreadyTakenErrorJson = mapper.writeValueAsString(firstShotAlreadyTakenError);
		doThrow(FirstShotAlreadyTakenException.class).when(detailsManagement)
				.registerDetails(ArgumentMatchers.any(PersonDto.class));
		mvc.perform(post("/save_person_rest").contentType(MediaType.APPLICATION_JSON).content(personDtoJson))
				.andExpect(status().isBadRequest()).andExpect(content().json(firstShotAlreadyTakenErrorJson));
	}

	@Test
	void updatePersonSuccessTest() throws Exception {

		Map<String, String> acknowledgement = new HashMap<>();
		acknowledgement.put("message", "second shot Details updated");
		String acknowledgementJson = mapper.writeValueAsString(acknowledgement);
		mvc.perform(post("/update_person_rest").param("aadharNumber", aadharNumber).param("shotDate", date))
				.andExpect(status().isOk()).andExpect(content().json(acknowledgementJson));

	}

	@Test
	void updatePersonVaccineUnavailableTest() throws Exception {
		Map<String, String> vaccineUnvailableError = new HashMap<>();
		vaccineUnvailableError.put("error", "Vaccine is unavailable");
		String vaccineUnvailableErrorJson = mapper.writeValueAsString(vaccineUnvailableError);
		doThrow(VaccineUnavailableException.class).when(shotManagement).secondShot(aadharNumber, parseDate);
		mvc.perform(post("/update_person_rest").param("aadharNumber", aadharNumber).param("shotDate", date))
				.andExpect(status().isBadRequest()).andExpect(content().json(vaccineUnvailableErrorJson));
	}

	@Test
	void updatePersonFirstShotNotTakenTest() throws Exception {
		Map<String, String> firstShotNotTakenError = new HashMap<>();
		firstShotNotTakenError.put("error", "Not eligible,first shot not taken");
		String firstShotNotTakenErrorJson = mapper.writeValueAsString(firstShotNotTakenError);
		doThrow(FirstShotNotTakenException.class).when(shotManagement).secondShot(aadharNumber, parseDate);
		mvc.perform(post("/update_person_rest").param("aadharNumber", aadharNumber).param("shotDate", date))
				.andExpect(status().isBadRequest()).andExpect(content().json(firstShotNotTakenErrorJson));
	}

	@Test
	void updateBothShotsTakenTest() throws Exception {
		Map<String, String> BothShotsTakenError = new HashMap<>();
		BothShotsTakenError.put("error", "Not eligible, both the shots taken");
		String BothShotsTakenErrorJson = mapper.writeValueAsString(BothShotsTakenError);
		doThrow(BothShotsTakenException.class).when(shotManagement).secondShot(aadharNumber, parseDate);
		mvc.perform(post("/update_person_rest").param("aadharNumber", aadharNumber).param("shotDate", date))
				.andExpect(status().isBadRequest()).andExpect(content().json(BothShotsTakenErrorJson));
	}

	@Test
	void updatePersonSecondShotNotEligibleTest() throws Exception {
		Map<String, String> SecondShotNotEligibleError = new HashMap<>();
		SecondShotNotEligibleError.put("error",
				"Not eligible to take second shot, can take the shot on or after:" + date);
		String SecondShotNotEligibleErrorJson = mapper.writeValueAsString(SecondShotNotEligibleError);
		doThrow(new SecondShotNotEligibleException(date)).when(shotManagement).secondShot(aadharNumber,
				LocalDate.parse(date));
		mvc.perform(post("/update_person_rest").param("aadharNumber", aadharNumber).param("shotDate", date))
				.andExpect(status().isBadRequest()).andExpect(content().json(SecondShotNotEligibleErrorJson));
	}

	@Test
	void getRepostTest() throws Exception {
		List<PersonDto> dtoList = new ArrayList<>();
		dtoList.add(personDto);
		String dtoListjson = new ObjectMapper().writeValueAsString(dtoList);
		String date = "2021-08-05";
		when(detailsManagement.getReport(LocalDate.parse(date))).thenReturn(dtoList);
		mvc.perform(get("/report_rest").param("date", date)).andExpect(status().isOk())
				.andExpect(content().string(dtoListjson));

	}
}
