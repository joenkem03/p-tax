package org.bizzdeskgroup.Helpers;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bizzdeskgroup.Dtos.Query.AdminTransactionDto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class xExcelis {
//    public static void DoExcel(){
//        SXSSFWorkbook wb = new SXSSFWorkbook(-1); // turn off auto-flushing and accumulate all rows in memory
//        Sheet sh = wb.createSheet();
//        for(int rownum = 0; rownum < 1000; rownum++){
//            Row row = sh.createRow(rownum);
//            for(int cellnum = 0; cellnum < 10; cellnum++){
//                Cell cell = row.createCell(cellnum);
//                String address = new CellReference(cell).formatAsString();
//                cell.setCellValue(address);
//            }
//            // manually control how rows are flushed to disk
//            if(rownum % 100 == 0) {
//                ((SXSSFSheet)sh).flushRows(100); // retain 100 last rows and flush all others
//                // ((SXSSFSheet)sh).flushRows() is a shortcut for ((SXSSFSheet)sh).flushRows(0),
//                // this method flushes all rows
//            }
//        }
//        FileOutputStream out = new FileOutputStream("/temp/sxssf.xlsx");
//        wb.write(out);
//        out.close();
//        // dispose of temporary files backing this workbook on disk
//        wb.dispose();
//    }

//    public static byte[] DoExcelOpt(List<AdminTransactionDto> res) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Workbook workbook = new XSSFWorkbook();
//        try {
//
//
//            Sheet sheet = workbook.createSheet("Persons");
//            sheet.setColumnWidth(0, 6000);
//            sheet.setColumnWidth(1, 4000);
//
//            Row header = sheet.createRow(0);
//
//            CellStyle headerStyle = workbook.createCellStyle();
//            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
//            font.setFontName("Arial");
//            font.setFontHeightInPoints((short) 16);
//            font.setBold(true);
//            headerStyle.setFont(font);
//
//
//            int hederColCount = 0;
//            Cell headerCell = header.createCell(hederColCount);
//            headerCell.setCellValue("ID");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//
//            //
//            CellStyle style = workbook.createCellStyle();
//            style.setWrapText(true);
//
////            Row row = sheet.createRow(2);
////            Cell cell = row.createCell(0);
////            cell.setCellValue("John Smith");
////            cell.setCellStyle(style);
////
////            cell = row.createCell(1);
////            cell.setCellValue(20);
////            cell.setCellStyle(style);
//            int rowCount = 0;
//            int colCount = 0;
////            for (int rowCount = 0; rowCount < res.size(); rowCount++) {
//            for (AdminTransactionDto rowItem:
//                 res) {
//
//                Row row = sheet.createRow(rowCount+1);
//                Cell cell = row.createCell(colCount);
//                cell.setCellValue(rowItem.transactionId);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.transactionDate);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.customerName);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.customerEmail);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.customerPhone);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.totalAmountPaid);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.serviceAmount);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.channel);
//                cell.setCellStyle(style);
//
//                rowCount++;
//                colCount = 0;
//
//            }
//
////            //
////            File currDir = new File(".");
////            String path = currDir.getAbsolutePath();
////            String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";
////
////            FileOutputStream outputStream = new FileOutputStream(fileLocation);
////            workbook.write(outputStream);
////            //
//
//            workbook.write(baos);
//            return baos.toByteArray();
//        } finally {
//            workbook.close();
//            baos.close();
//        }
//    }

    public static byte[] DoExcelOptZ() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        try {


            Sheet sheet = workbook.createSheet("Persons");
            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 4000);

            Row header = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 16);
            font.setBold(true);
            headerStyle.setFont(font);

            Cell headerCell = header.createCell(0);
            headerCell.setCellValue("Name");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(1);
            headerCell.setCellValue("Age");
            headerCell.setCellStyle(headerStyle);


            //
            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);

            Row row = sheet.createRow(2);
            Cell cell = row.createCell(0);
            cell.setCellValue("John Smith");
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(20);
            cell.setCellStyle(style);

            //
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

            FileOutputStream outputStream = new FileOutputStream(fileLocation);
//            workbook.write(outputStream);
            workbook.write(baos);
            return baos.toByteArray();
        } finally {
            workbook.close();
            baos.close();
        }
    }


//    public static byte[] DoExcelOptZZ(List<AdminTransactionDto> res) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Workbook workbook = new HSSFWorkbook();
//        try {
//
//
//            Sheet sheet = workbook.createSheet("Persons");
//            sheet.setColumnWidth(0, 6000);
//            sheet.setColumnWidth(1, 4000);
//
//            Row header = sheet.createRow(0);
//
//            CellStyle headerStyle = workbook.createCellStyle();
//            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//            HSSFFont font = ((HSSFWorkbook) workbook).createFont();
//            font.setFontName("Arial");
//            font.setFontHeightInPoints((short) 16);
//            font.setBold(true);
//            headerStyle.setFont(font);
//
//
//            int hederColCount = 0;
//            Cell headerCell = header.createCell(hederColCount);
//            headerCell.setCellValue("ID");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//            headerCell = header.createCell(++hederColCount);
//            headerCell.setCellValue("Date");
//            headerCell.setCellStyle(headerStyle);
//
//
//            //
//            CellStyle style = workbook.createCellStyle();
//            style.setWrapText(true);
//
////            Row row = sheet.createRow(2);
////            Cell cell = row.createCell(0);
////            cell.setCellValue("John Smith");
////            cell.setCellStyle(style);
////
////            cell = row.createCell(1);
////            cell.setCellValue(20);
////            cell.setCellStyle(style);
//            int rowCount = 0;
//            int colCount = 0;
////            for (int rowCount = 0; rowCount < res.size(); rowCount++) {
//            for (AdminTransactionDto rowItem:
//                    res) {
//
//                Row row = sheet.createRow(rowCount+1);
//                Cell cell = row.createCell(colCount);
//                cell.setCellValue(rowItem.transactionId);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.transactionDate);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.customerName);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.customerEmail);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.customerPhone);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.totalAmountPaid);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.serviceAmount);
//                cell.setCellStyle(style);
//
//                cell = row.createCell(++colCount);
//                cell.setCellValue(rowItem.channel);
//                cell.setCellStyle(style);
//
//                rowCount++;
//
//            }
//
////            //
////            File currDir = new File(".");
////            String path = currDir.getAbsolutePath();
////            String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";
////
////            FileOutputStream outputStream = new FileOutputStream(fileLocation);
////            workbook.write(outputStream);
////            //
//
//            workbook.write(baos);
//            return baos.toByteArray();
//        } finally {
//            workbook.close();
//            baos.close();
//        }
//    }
}
