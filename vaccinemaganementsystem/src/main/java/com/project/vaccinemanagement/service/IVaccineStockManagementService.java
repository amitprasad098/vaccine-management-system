package com.project.vaccinemanagement.service;

import com.project.vaccinemanagement.dto.VaccineDto;

public interface IVaccineStockManagementService {

	void storeStock(VaccineDto vaccineDto);

	long availaibleVaccineCount();

}
