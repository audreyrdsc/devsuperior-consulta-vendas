package com.devsuperior.dsmeta.controllers;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/sales")
public class SaleController {

	@Autowired
	private SaleService service;

	//Rota pelo id
	@GetMapping(value = "/{id}")
	public ResponseEntity<SaleMinDTO> findById(@PathVariable Long id) {
		SaleMinDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	//Rota por report (com ou sem argumentos - PAGINADA)
	@GetMapping(value = "/report")
	public ResponseEntity<Page<SaleReportDTO>> getReport(
			@RequestParam(defaultValue = "") String minDate,
			@RequestParam(defaultValue = "") String maxDate,
			@RequestParam(defaultValue = "") String name,
			Pageable pageable) {

		Page<SaleReportDTO> page = service.reportAll(minDate, maxDate, name, pageable);
		return ResponseEntity.ok(page);
	}

	//Rota por summary (com ou sem argumentos - LISTADA)
	@GetMapping(value = "/summary")
	public ResponseEntity<List<SaleSummaryDTO>> getSummary(
			@RequestParam(defaultValue = "") String minDate,
			@RequestParam(defaultValue = "") String maxDate) {

		List<SaleSummaryDTO> list = service.getSummary(minDate, maxDate);
		return ResponseEntity.ok(list);
	}

}