package com.havaz.transport.api.common;

import com.havaz.transport.api.model.BaoCaoLenhDTO;
import com.havaz.transport.api.model.BaoCaoTaiXeDTO;
import com.havaz.transport.api.model.NgayCongDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExcelGenerator {

    public static int dayOfMonth(int month, int year) {
        switch (month) {
            // các tháng 1, 3, 5, 7, 8, 10 và 12 có 31 ngày.
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            // các tháng 4, 6, 9 và 11 có 30 ngày
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            // Riêng tháng 2 nếu là năm nhuận thì có 29 ngày, còn không thì có 28 ngày.
            case 2:
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return 0;
        }
    }

    public static ByteArrayInputStream customersToExcel(List<BaoCaoTaiXeDTO> baoCaoTaiXeDTOS) throws IOException {
        LocalDate date = baoCaoTaiXeDTOS.get(0).getDuLieuThang().get(0).getNgay();
        int month = date.getMonthValue();
        int year = date.getYear();
        List<String> columns = new ArrayList<String>();
        columns.add("Họ và tên");
        int day = dayOfMonth(month, year);
        for (int i = 1; i <= day; i++) {
            columns.add(String.valueOf(i));
        }
        columns.add("Tổng công");
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Báo cáo chấm công");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            // Row for Header
            Row headerRow = sheet.createRow(1);
            // Header
            for (int col = 0; col < columns.size(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns.get(col));
                cell.setCellStyle(headerCellStyle);
            }
            // CellStyle for data
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

            int rowIdx = 2;
            for (BaoCaoTaiXeDTO baoCaoTaiXeDTO : baoCaoTaiXeDTOS) {
                List<NgayCongDTO> ngayCongDTOS = baoCaoTaiXeDTO.getDuLieuThang();
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(baoCaoTaiXeDTO.getTaiXeTen());
                for (int i = 1; i <= day; i++) {
                    for (NgayCongDTO ngayCongDTO : ngayCongDTOS) {
                        LocalDate localDate = ngayCongDTO.getNgay();
                        int ngayCong = localDate.getDayOfMonth();
                        if (i == ngayCong) {
                            row.createCell(i).setCellValue(ngayCongDTO.getCong());
                            break;
                        }else {
                            row.createCell(i).setCellValue(0);
                        }
                    }
                    row.createCell(day+1).setCellValue(baoCaoTaiXeDTO.getTongCong());
                }
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public static ByteArrayInputStream commandToExcel(List<BaoCaoLenhDTO> baoCaoLenhDTOs) throws IOException {

        List<String> columns = new ArrayList<String>();
        columns.add("STT");
        columns.add("Lái xe");
        columns.add("Mã số nhân viên");
        columns.add("Nơi làm việc");
        columns.add("Lệnh đón");
        columns.add("Lệnh trả");
        columns.add("Lệnh hoàn thành");
        columns.add("Lệnh hủy");
        columns.add("Số khách đón");
        columns.add("Số khách trả");
        columns.add("Tổng số khách");
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Báo cáo tổng hợp");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setShrinkToFit(true);
            headerCellStyle.setWrapText(true);
            // Row for Header
            Row headerRow = sheet.createRow(1);
            // Header
            for (int col = 0; col < columns.size(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns.get(col));
                cell.setCellStyle(headerCellStyle);
            }
            // CellStyle for data
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

            int rowIdx = 2;
            int stt = 1;
            for (BaoCaoLenhDTO baoCaoLenhDTO : baoCaoLenhDTOs) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(stt);
                row.createCell(1).setCellValue(baoCaoLenhDTO.getTaiXeTen());
                row.createCell(2).setCellValue(baoCaoLenhDTO.getMaNV());
                row.createCell(3).setCellValue(baoCaoLenhDTO.getNoiLamViec());
                row.createCell(4).setCellValue(baoCaoLenhDTO.getTongLenhDon());
                row.createCell(5).setCellValue(baoCaoLenhDTO.getTongKhachTra());
                row.createCell(6).setCellValue(baoCaoLenhDTO.getTongLenhThanhCong());
                row.createCell(7).setCellValue(baoCaoLenhDTO.getTongLenhHuy());
                row.createCell(8).setCellValue(baoCaoLenhDTO.getTongKhachDon());
                row.createCell(9).setCellValue(baoCaoLenhDTO.getTongKhachTra());
                row.createCell(10).setCellValue(baoCaoLenhDTO.getTongsoKhach());
                stt++;
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
