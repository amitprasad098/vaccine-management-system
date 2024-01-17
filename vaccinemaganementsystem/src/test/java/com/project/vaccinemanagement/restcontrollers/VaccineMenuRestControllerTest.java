package com.project.vaccinemanagement.restcontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vaccinemanagement.VaccinemanagementsystemApplication;
import com.project.vaccinemanagement.dto.VaccineDto;
import com.project.vaccinemanagement.service.VaccineStockManagementServiceImpl;

@AutoConfigureMockMvc
@SpringBootTest(classes = { VaccinemanagementsystemApplication.class })
class VaccineMenuRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	VaccineStockManagementServiceImpl stockManagement;

	private ObjectMapper mapper = new ObjectMapper();
	@Test
	void getvaccineBalanceTest() throws Exception {
		when(stockManagement.availaibleVaccineCount()).thenReturn((long) 1);
		Map<String,Long> message = new HashMap<>();
		message.put("Available vaccines are: ",1L);
		String messageJson = mapper.writeValueAsString(message);
		mvc.perform(get("/vaccine_balance_rest")).andExpect(status().isOk()).andExpect(content().json(messageJson));
	}

	@Test
	void insertVaccineTest() throws Exception {
		VaccineDto vaccineDto = new VaccineDto();
		vaccineDto.setDate("2021-08-07");
		vaccineDto.setName("covaxin");
		vaccineDto.setInitial(1);
		String jsonString = mapper.writeValueAsString(vaccineDto);
		doNothing().when(stockManagement).storeStock(vaccineDto);
		MvcResult result = mvc
				.perform(post("/insert_stock_rest").contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andReturn();
		assertEquals(200, result.getResponse().getStatus());
		Map<String,String> message = new HashMap<>();
		message.put("message","vaccine stock added successfully");
		String messageJson = mapper.writeValueAsString(message);
		assertEquals(messageJson, result.getResponse().getContentAsString());

	}
}
