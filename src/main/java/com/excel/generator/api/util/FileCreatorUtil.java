package com.excel.generator.api.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.excel.generator.api.exception.CustomException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class FileCreatorUtil {

	public void createJsonNode(Iterator<Cell> cellIterator, JsonNode cellNode, String s) {
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

	public void writeFile(List<JsonNode> jsonNodeList) throws CustomException {
		String fileName = new ClassPathResource(PathUtil.FILE_PATH).getPath();
		BufferedWriter bw = null;
		try (FileWriter fileWriter = new FileWriter(fileName)) {
			ObjectMapper mapper = new ObjectMapper();
			String jsonNode = mapper.writeValueAsString(jsonNodeList);

			bw = new BufferedWriter(fileWriter);
			bw.append(jsonNode);
			bw.flush();
			bw.close();
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
