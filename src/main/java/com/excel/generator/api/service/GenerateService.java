package com.excel.generator.api.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.excel.generator.api.exception.CustomException;
import com.excel.generator.api.util.FileCreatorUtil;
import com.excel.generator.api.util.PathUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GenerateService {

	@Autowired
	private FileCreatorUtil fileCreatorUtil;

	public Boolean excelGenerator(MultipartFile excelFile) throws Exception {
		String fileName = excelFile.getOriginalFilename().toLowerCase();

		List<String> headList = new ArrayList<>();
		List<JsonNode> responseList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();

		if (!excelFile.isEmpty() && fileName.endsWith(".xlsx")) {
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
					JsonNode cellNode = mapper.createObjectNode();
					for (String s : headList) {
						fileCreatorUtil.createJsonNode(cellIterator, cellNode, s);
					}
					responseList.add(cellNode);
				}
			}
			fileCreatorUtil.writeFile(responseList);
			excel.close();
		} else {
			throw new CustomException(PathUtil.UNKNOWN_FILE, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return true;

	}

	public byte[] get() throws CustomException {
		BufferedReader br = null;
		String fileName = new ClassPathResource(PathUtil.FILE_PATH).getPath();
		try (FileReader fileReader = new FileReader(fileName)) {
			br = new BufferedReader(fileReader);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				return sCurrentLine.getBytes();
			}
		} catch (Exception e) {
			throw new CustomException(PathUtil.FILE_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new CustomException(PathUtil.FILE_READER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
