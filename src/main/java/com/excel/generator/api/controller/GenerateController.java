package com.excel.generator.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.excel.generator.api.service.GenerateService;

@RestController
@RequestMapping(value = "/generate")
public class GenerateController {

	@Autowired
	private GenerateService generateService;

	@GetMapping("/home")
	public String home() {
		return "hello generator";
	}

	@PostMapping(value = "/create")
	public @ResponseBody ResponseEntity<Boolean> excelGenerator(@RequestParam("excel") MultipartFile excelFile) throws Exception {
		return ResponseEntity.ok().body(generateService.excelGenerator(excelFile));
	}

	@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ResponseEntity<byte[]> get() throws Exception {
		return ResponseEntity.ok().body(generateService.get());
	}

}
