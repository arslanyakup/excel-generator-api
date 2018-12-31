package com.excel.generator.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.excel.generator.api.service.GenerateService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(value = "/generate")
public class GenerateController {

	@Autowired
	private GenerateService generateService;

	@GetMapping("/home")
	public String home() {
		return "hello generator";
	}

	@PostMapping(value = "/excel")
	public ResponseEntity<List<JsonNode>> excelGenerator(@RequestParam("excel") MultipartFile excelFile) throws Exception {
		return ResponseEntity.ok().body(generateService.excelGenerator(excelFile));
	}

}
