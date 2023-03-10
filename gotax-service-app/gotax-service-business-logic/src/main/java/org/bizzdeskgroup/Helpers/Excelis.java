package org.bizzdeskgroup.Helpers;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Excelis {

    public static <T> byte[] writeToExcel(String fileName, List<T> data) throws Exception {
        ByteArrayOutputStream baos = null;
        Workbook workbook = null;
        try {
            String author = "PaySure Tax";
//            File file = new File(fileName);
            baos = new ByteArrayOutputStream();
            workbook = new XSSFWorkbook();

            if (workbook instanceof XSSFWorkbook) {
                ((XSSFWorkbook)workbook).getProperties().getCoreProperties().setCreator(author);
            } else if (workbook instanceof HSSFWorkbook) {
                ((HSSFWorkbook)workbook).createInformationProperties();
                ((HSSFWorkbook)workbook).getSummaryInformation().setAuthor(author);
            }

            Sheet sheet = workbook.createSheet(fileName);
            List<String> fieldNames = getFieldNamesForClass(data.get(0).getClass());
            int rowCount = 0;
            int columnCount = 0;
            Row row = sheet.createRow(rowCount++);
            for (String fieldName : fieldNames) {
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue(fieldName);
            }
            Class<? extends Object> classz = data.get(0).getClass();
            for (T t : data) {
                row = sheet.createRow(rowCount++);
                columnCount = 0;
                for (String fieldName : fieldNames) {
                    Cell cell = row.createCell(columnCount);
                    Method method = null;
                    try {
                        method = classz.getMethod("get" + capitalize(fieldName));
                    } catch (NoSuchMethodException nme) {
                        method = classz.getMethod("get" + fieldName);
                    }
                    Object value = method.invoke(t, (Object[]) null);
                    if (value != null) {
                        if (value instanceof String) {
                            cell.setCellValue((String) value);
                        } else if (value instanceof Long) {
                            cell.setCellValue((Long) value);
                        } else if (value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                        } else if (value instanceof Double) {
                            cell.setCellValue((Double) value);
                        }
                    }
                    columnCount++;
                }
            }
//            fos = new FileOutputStream(file);
//            workbook.write(fos);
//            fos.flush();

            workbook.write(baos);
            return baos.toByteArray();
        } finally {
            if (baos != null) {
                baos.flush();
                baos.close();
            }
            if (workbook != null) {
                workbook.close();
            }
        }
//        return new byte[0];
    }

    // retrieve field names from a POJO class
    private static List<String> getFieldNamesForClass(Class<?> clazz) throws Exception {
        List<String> fieldNames = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fieldNames.add(fields[i].getName());
        }
        return fieldNames;
    }

    // capitalize the first letter of the field name for retriving value of the
    // field later
    private static String capitalize(String s) {
        if (s.length() == 0)
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
