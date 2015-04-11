package at.basketballsalzburg.bbstats.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import at.basketballsalzburg.bbstats.dto.PlayerDTO;

/**
 * @author Martin Schneider
 */
public class ExcelPlayerExporter implements PlayerExporter
{

    public InputStream getFile(Set<PlayerDTO> playerList) throws IOException
    {
        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Adressen");

        short rowIndex = 0;
        CellStyle boldStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        boldStyle.setFont(font);
        Row row = sheet.createRow(rowIndex);

        Cell cell = row.createCell(0);
        cell.setCellValue("Name");
        cell.setCellStyle(boldStyle);

        cell = row.createCell(1);
        cell.setCellValue("Vorname");
        cell.setCellStyle(boldStyle);

        cell = row.createCell(2);
        cell.setCellValue("Adresse");
        cell.setCellStyle(boldStyle);

        cell = row.createCell(3);
        cell.setCellValue("PLZ");
        cell.setCellStyle(boldStyle);

        cell = row.createCell(4);
        cell.setCellValue("Ort");
        cell.setCellStyle(boldStyle);

        cell = row.createCell(5);
        cell.setCellValue("Land");
        cell.setCellStyle(boldStyle);

        cell = row.createCell(6);
        cell.setCellValue("Telefon");
        cell.setCellStyle(boldStyle);

        cell = row.createCell(7);
        cell.setCellValue("E-Mail");
        cell.setCellStyle(boldStyle);

        cell = row.createCell(8);
        cell.setCellValue("Geburtstag");
        cell.setCellStyle(boldStyle);

        cell = row.createCell(9);
        cell.setCellValue("Nationalität");
        cell.setCellStyle(boldStyle);

        CellStyle dateStyle = wb.createCellStyle();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat(
            "dd.MM.yyyy"));

        for (PlayerDTO player : playerList)
        {
            rowIndex++;
            Row playerRow = sheet.createRow(rowIndex);
            playerRow.createCell(0).setCellValue(player.getLastName());
            playerRow.createCell(1).setCellValue(player.getFirstName());
            playerRow.createCell(2).setCellValue(player.getAdress());
            playerRow.createCell(3).setCellValue(player.getPostalCode());
            playerRow.createCell(4).setCellValue(player.getCity());
            playerRow.createCell(5).setCellValue(player.getCountry());
            playerRow.createCell(6).setCellValue(player.getPhone());
            playerRow.createCell(7).setCellValue(player.getEmail());

            if (player.getBirthday() != null)
            {
                cell = playerRow.createCell(8);
                cell.setCellValue(player.getBirthday());
                cell.setCellStyle(dateStyle);
            }
            playerRow.createCell(9).setCellValue(player.getNationality());
        }

        for (int columnNr = 0; columnNr <= 9; columnNr++)
        {
            sheet.autoSizeColumn(columnNr);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
