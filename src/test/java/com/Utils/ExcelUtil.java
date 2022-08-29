package com.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	public static FileInputStream file;
	public static XSSFWorkbook workbook;
	public static XSSFSheet sheet;
	public static XSSFRow row;
	public static XSSFCell cell;
	
	public static int getRowCount(String excelfilepath, String excelsheetname) throws IOException {
		
		file = new FileInputStream(excelfilepath);
		workbook = new XSSFWorkbook(file);
		sheet = workbook.getSheet(excelsheetname);
		int rowCount = sheet.getLastRowNum();
		workbook.close();
		file.close();
		return rowCount;
	
	}
	
	public static int getCellCount(String excelfilepath, String excelsheetname, int rownum) throws IOException {
		
		file = new FileInputStream(excelfilepath);
		workbook = new XSSFWorkbook(file);
		sheet = workbook.getSheet(excelsheetname);
		row = sheet.getRow(rownum);
		int cellCount = row.getLastCellNum();
		workbook.close();
		file.close();
		return cellCount;
		
	}
	
	public static String getCellData(String excelfilepath, String excelsheetname, int rownum, int colnum) throws IOException {
		
		file = new FileInputStream(excelfilepath);
		workbook = new XSSFWorkbook(file);
		sheet = workbook.getSheet(excelsheetname);
		row = sheet.getRow(rownum);
		cell = row.getCell(colnum);
		String data;
		try {
		DataFormatter formatter = new DataFormatter();
		String cellData = formatter.formatCellValue(cell);
		return cellData;
		}
		catch(Exception e) {
			data = "";
		}
		workbook.close();
		file.close();
		return data;
		
	}
	
	public static void setCellData(String excelfilepath, String excelsheetname, int rownum, int colnum, String data) throws IOException {
		
		file = new FileInputStream(excelfilepath);
		workbook = new XSSFWorkbook(file);
		sheet = workbook.getSheet(excelsheetname);
		row = sheet.getRow(rownum);
		cell = row.createCell(colnum);
		cell.setCellValue(data);
		FileOutputStream fo = new FileOutputStream(excelfilepath);
		workbook.write(fo);
		workbook.close();
		file.close();
		fo.close();
	}

}
