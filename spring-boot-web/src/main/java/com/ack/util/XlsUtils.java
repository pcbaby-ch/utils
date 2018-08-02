package com.ack.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author chenzhao(ack)
 * @time Jun 10, 2017-7:54:59 PM
 */
public class XlsUtils {

	public static void main(String[] args) throws Exception {
		System.out.println("sdflj");
		List<List<String>> list = XlsUtils.readXls("D:\\#iwork_pectere\\CB16-180_东航航线网络管理平台\\初始导入（已映射）.xls", 2, 0);

		XlsUtils.writeXls("D:\\#iwork_pectere\\CB16-180_东航航线网络管理平台\\初始导入（已映射）清洗.xls", list);
		System.out.println(list);
	}

	/**
	 * 读取Xls文件
	 * 
	 * @author chenzhao(ack)
	 * @time Jun 10, 2017
	 * @param path
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static List<List<String>> readXls(String path, int startRow, int startCol) throws Exception {
		System.out.println("#xls路径" + path);
		InputStream is = new FileInputStream(path);
		Workbook workBook = null;
		if (path.endsWith(".xls")) {// excel 03
			workBook = new HSSFWorkbook(is);
		} else {// excel 07
			workBook = new XSSFWorkbook(is);
		}
		is.close();
		List<List<String>> result = new ArrayList<List<String>>();
		Sheet hSheet = workBook.getSheetAt(0);
		int maxColIndex = hSheet.getRow(0).getLastCellNum();
		for (int rowIndex = startRow; rowIndex <= hSheet.getLastRowNum(); rowIndex++) {
			List<String> rowList = new ArrayList<String>();
			Row hRow = hSheet.getRow(rowIndex);
			if (hRow == null)
				continue;
			maxColIndex = hRow.getLastCellNum();
			for (int colIndex = startCol; colIndex < maxColIndex; colIndex++) {
				Cell cell = hRow.getCell(colIndex);
				if (cell == null)
					rowList.add("null");
				else
					rowList.add(getStringVal(cell));
			}
			// System.out.println("#总列数：" + + maxColIndex);
			result.add(rowList);
		}
		// System.out.println("#总行数:" + hSheet.getLastRowNum());
		return result;
	}

	@SuppressWarnings("resource")
	public static List<List<String>> readXls(String path, int startRow, int startCol, int endRow, int endCol)
			throws Exception {
		System.out.println("#xls路径" + path);
		InputStream is = new FileInputStream(path);
		Workbook workBook = null;
		if (path.endsWith(".xls")) {// excel 03
			workBook = new HSSFWorkbook(is);
		} else {// excel 07
			workBook = new XSSFWorkbook(is);
		}
		is.close();
		List<List<String>> result = new ArrayList<List<String>>();
		Sheet hSheet = workBook.getSheetAt(0);
		int maxColIndex = hSheet.getRow(0).getLastCellNum();
		for (int rowIndex = startRow; rowIndex <= endRow && rowIndex <= hSheet.getLastRowNum(); rowIndex++) {
			List<String> rowList = new ArrayList<String>();
			Row hRow = hSheet.getRow(rowIndex);
			if (hRow == null)
				continue;
			maxColIndex = hRow.getLastCellNum();
			for (int colIndex = startCol; colIndex <= endCol && colIndex < maxColIndex; colIndex++) {
				Cell cell = hRow.getCell(colIndex);
				if (cell == null)
					rowList.add("null");
				else
					rowList.add(getStringVal(cell));
			}
			// System.out.println("#总列数：" + + maxColIndex);
			result.add(rowList);
		}
		// System.out.println("#总行数:" + hSheet.getLastRowNum());
		return result;
	}

	/**
	 * 读取xls文件，并根据fileds转换数据模型
	 * 
	 * @author chenzhao(ack)
	 * @time Jun 13, 2017
	 * @param path
	 * @param fileds
	 * @param startRow
	 * @param startCol
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static List<LinkedHashMap<String, String>> readXls(String path, ArrayList<String> fileds, int startRow,
			int startCol) throws Exception {
		System.out.println("#xls路径" + path);
		InputStream is = new FileInputStream(path);
		Workbook workBook = null;
		if (path.endsWith(".xls")) {// excel 03
			workBook = new HSSFWorkbook(is);
		} else {// excel 07
			workBook = new XSSFWorkbook(is);
		}
		is.close();
		List<LinkedHashMap<String, String>> result = new ArrayList<LinkedHashMap<String, String>>();
		Sheet hSheet = workBook.getSheetAt(0);
		int maxColIndex = hSheet.getRow(0).getLastCellNum();
		for (int rowIndex = startRow; rowIndex <= hSheet.getLastRowNum(); rowIndex++) {
			LinkedHashMap<String, String> rowList = new LinkedHashMap<String, String>();
			Row hRow = hSheet.getRow(rowIndex);
			if (hRow == null)
				continue;
			maxColIndex = hRow.getLastCellNum();
			for (int colIndex = startCol; colIndex < fileds.size() && colIndex < maxColIndex; colIndex++) {
				Cell cell = hRow.getCell(colIndex);
				String filed = "";
				try {
					filed = fileds.get(colIndex);
				} catch (Exception e) {
					throw new Exception(
							"#fileds和xls列数不匹配（fileds大于xls列数）" + "#fileds:" + fileds.size() + "#xlsCols:" + maxColIndex);
				}
				if (cell == null)
					rowList.put(filed, "");
				else
					rowList.put(filed, getStringVal(cell));
			}
			// System.out.println("#总列数：" + maxColIndex);
			result.add(rowList);
		}
		// System.out.println("#总行数:" + hSheet.getLastRowNum());
		return result;
	}

	@SuppressWarnings("resource")
	public static List<LinkedHashMap<String, String>> readXls(String path, ArrayList<String> fileds, int startRow,
			int startCol, int endRow, int endCol) throws Exception {
		System.out.println("#xls路径" + path);
		InputStream is = new FileInputStream(path);
		Workbook workBook = null;
		if (path.endsWith(".xls")) {// excel 03
			workBook = new HSSFWorkbook(is);
		} else {// excel 07
			workBook = new XSSFWorkbook(is);
		}
		is.close();
		List<LinkedHashMap<String, String>> result = new ArrayList<LinkedHashMap<String, String>>();
		Sheet hSheet = workBook.getSheetAt(0);
		int maxColIndex = hSheet.getRow(0).getLastCellNum();
		for (int rowIndex = startRow; rowIndex <= endRow && rowIndex <= hSheet.getLastRowNum(); rowIndex++) {
			LinkedHashMap<String, String> rowList = new LinkedHashMap<String, String>();
			Row hRow = hSheet.getRow(rowIndex);
			if (hRow == null)
				continue;
			maxColIndex = hRow.getLastCellNum();
			for (int colIndex = startCol; colIndex <= endCol && colIndex < fileds.size()
					&& colIndex < maxColIndex; colIndex++) {
				Cell cell = hRow.getCell(colIndex);
				String filed = "";
				try {
					filed = fileds.get(colIndex);
				} catch (Exception e) {
					throw new Exception(
							"#fileds和xls列数不匹配（fileds大于xls列数）" + "#fileds:" + fileds.size() + "#xlsCols:" + maxColIndex);
				}
				if (cell == null)
					rowList.put(filed, "");
				else
					rowList.put(filed, getStringVal(cell));
			}
			// System.out.println("#总列数：" + maxColIndex);
			result.add(rowList);
		}
		// System.out.println("#总行数:" + hSheet.getLastRowNum());
		return result;
	}

	/**
	 * 读取xls文件，并根据fileds转换数据模型
	 * 
	 * @author chenzhao(ack)
	 * @time Jun 13, 2017
	 * @param path
	 * @param fileds
	 * @param startRow
	 * @param startCol
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static List<LinkedHashMap<String, String>> readXls(MultipartFile file, ArrayList<String> fileds,
			int startRow, int startCol) throws Exception {
		Workbook workBook = null;
		if (file.getOriginalFilename().endsWith(".xls")) {// excel 03
			workBook = new HSSFWorkbook(file.getInputStream());
		} else {// excel 07
			workBook = new XSSFWorkbook(file.getInputStream());
		}
		List<LinkedHashMap<String, String>> result = new ArrayList<LinkedHashMap<String, String>>();
		Sheet hSheet = workBook.getSheetAt(0);
		int maxColIndex = hSheet.getRow(0).getLastCellNum();
		for (int rowIndex = startRow; rowIndex <= hSheet.getLastRowNum(); rowIndex++) {
			LinkedHashMap<String, String> rowList = new LinkedHashMap<String, String>();
			Row hRow = hSheet.getRow(rowIndex);
			if (hRow == null)
				continue;
			maxColIndex = hRow.getLastCellNum();
			for (int colIndex = startCol; colIndex < fileds.size() && colIndex < maxColIndex; colIndex++) {
				Cell cell = hRow.getCell(colIndex);
				String filed = "";
				try {
					filed = fileds.get(colIndex);
				} catch (Exception e) {
					throw new Exception(
							"#fileds和xls列数不匹配（fileds大于xls列数）" + "#fileds:" + fileds.size() + "#xlsCols:" + maxColIndex);
				}
				if (cell == null)
					rowList.put(filed, "");
				else
					rowList.put(filed, getStringVal(cell));
			}
			result.add(rowList);
		}
		return result;
	}

	/**
	 * 写Xls文件
	 * 
	 * @author chenzhao(ack)
	 * @time Jun 10, 2017
	 * @param path
	 * @param srcList
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static void writeXls(String path, List<List<String>> srcList) throws Exception {
		OutputStream os = new FileOutputStream(path);
		HSSFWorkbook hWorkBook = new HSSFWorkbook();
		HSSFSheet hSheet = hWorkBook.createSheet();
		for (int rowIndex = 0; rowIndex < srcList.size(); rowIndex++) {
			List<String> srcRow = srcList.get(rowIndex);
			HSSFRow hRow = hSheet.createRow(rowIndex);
			for (int colIndex = 0; colIndex < srcRow.size(); colIndex++) {
				hRow.createCell(colIndex).setCellValue(srcRow.get(colIndex));
			}
			System.out.println("#总列数：" + srcRow.size());
		}
		System.out.println("#总行数:" + srcList.size());
		hWorkBook.write(os);
		os.flush();
		os.close();
	}

	/**
	 * 获取xls文件总行数
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static int getTotalRow(String path) throws IOException {
		InputStream is = new FileInputStream(path);
		Workbook workBook = null;
		if (path.endsWith(".xls")) {// excel 03
			workBook = new HSSFWorkbook(is);
		} else {// excel 07
			workBook = new XSSFWorkbook(is);
		}
		is.close();

		Sheet hSheet = workBook.getSheetAt(0);
		return hSheet.getLastRowNum()+1;
	}

	/**
	 * 模板生成
	 * 
	 * @author chenzhao(ack)
	 * @time Jun 11, 2017
	 * @param path
	 * @param srcList
	 */
	public void createModelXls(String path, List<String> srcList) {

	}

	/**
	 * 格式抽象统一为string
	 * 
	 * @author chenzhao(ack)
	 * @time Jun 10, 2017
	 * @param cell
	 * @return
	 */
	public static String getStringVal(HSSFCell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "true" : "false";
		case Cell.CELL_TYPE_NUMERIC:
			return (cell.getNumericCellValue() + "").replace(".0", "").trim();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue().trim();
		default:
			return "";
		}
	}

	public static String getStringVal(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "true" : "false";
		case Cell.CELL_TYPE_NUMERIC:
			return (cell.getNumericCellValue() + "").replace(".0", "").trim();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue().trim();
		default:
			return "";
		}
	}
}
