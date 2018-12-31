package com.excel.generator.api.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.excel.generator.api.exception.CustomException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class GenerateService {

	public List<JsonNode> excelGenerator(MultipartFile excelFile) throws Exception {
		String fileName = excelFile.getOriginalFilename().toLowerCase();

		List<String> headList = new ArrayList<>();
		List<JsonNode> responseList = new ArrayList<>();

		if (!excelFile.isEmpty() || fileName.endsWith(".xlsx")) {
			XSSFWorkbook excel = new XSSFWorkbook(excelFile.getInputStream());
			XSSFSheet sheet = excel.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (headList.isEmpty()) {
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						headList.add(currentCell.getStringCellValue());
					}
				} else {
					Iterator<Cell> cellIterator = row.cellIterator();
					ObjectMapper mapper = new ObjectMapper();
					JsonNode cellNode = mapper.createObjectNode();
					for (String s : headList) {
						Cell currentCell = cellIterator.next();
						switch (currentCell.getCellType().name()) {
						case "NUMERIC":
							((ObjectNode) cellNode).put(s, currentCell.getNumericCellValue());
							break;
						case "STRING":
							((ObjectNode) cellNode).put(s, currentCell.getStringCellValue());
							break;
						case "DATE":
							((ObjectNode) cellNode).put(s, currentCell.getDateCellValue().toString());
							break;
						case "BOOLEAN":
							((ObjectNode) cellNode).put(s, currentCell.getBooleanCellValue());
							break;
						default:
							break;
						}
					}
					responseList.add(cellNode);
				}
			}
			excel.close();
		} else {
			throw new CustomException("Unknown File!!!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseList;
	}
}
