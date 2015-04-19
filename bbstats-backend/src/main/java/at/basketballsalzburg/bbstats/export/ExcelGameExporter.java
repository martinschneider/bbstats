package at.basketballsalzburg.bbstats.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;

import at.basketballsalzburg.bbstats.dto.GameDTO;

/**
 * @author Martin Schneider
 */
public class ExcelGameExporter implements GameExporter {

	@Value("${xls.dateFormat}")
	private String dateFormat = "dd.MM.yyyy, HH:mm";

	public InputStream getFile(List<GameDTO> games) throws IOException {
		Workbook wb = new HSSFWorkbook();
		try {
			CreationHelper createHelper = wb.getCreationHelper();
			Sheet sheet = wb.createSheet("Spiele");

			short rowIndex = 0;
			CellStyle boldStyle = wb.createCellStyle();
			Font font = wb.createFont();
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			boldStyle.setFont(font);
			Row row = sheet.createRow(rowIndex);

			CellStyle dateCellStyle = wb.createCellStyle();
			dateCellStyle.setDataFormat(createHelper.createDataFormat()
					.getFormat(dateFormat));

			Cell cell = row.createCell(0);
			cell.setCellValue("Datum");
			cell.setCellStyle(boldStyle);

			cell = row.createCell(1);
			cell.setCellValue("Liga");
			cell.setCellStyle(boldStyle);

			cell = row.createCell(2);
			cell.setCellValue("Halle");
			cell.setCellStyle(boldStyle);

			cell = row.createCell(3);
			cell.setCellValue("Mannschaft A");
			cell.setCellStyle(boldStyle);

			cell = row.createCell(4);
			cell.setCellValue("Mannschaft B");
			cell.setCellStyle(boldStyle);

			cell = row.createCell(5);
			cell.setCellValue("Ergebnis");
			cell.setCellStyle(boldStyle);

			for (GameDTO game : games) {
				rowIndex++;
				Row playerRow = sheet.createRow(rowIndex);
				cell = playerRow.createCell(0);
				cell.setCellValue(game.getDateTime());
				cell.setCellStyle(dateCellStyle);
				playerRow.createCell(1).setCellValue(
						game.getLeague().getShortName());
				playerRow.createCell(2).setCellValue(
						game.getGym().getDisplayName());
				playerRow.createCell(3).setCellValue(game.getTeamA().getName());
				playerRow.createCell(4).setCellValue(game.getTeamB().getName());
				playerRow.createCell(5).setCellValue(getResult(game));
			}

			for (int columnNr = 0; columnNr <= 5; columnNr++) {
				sheet.autoSizeColumn(columnNr);
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			wb.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} finally {
			wb.close();
		}
	}

	public String getResult(GameDTO game) {
		boolean ot = false;
		int sumA = game.getScoreA1().intValue() + game.getScoreA2().intValue()
				+ game.getScoreA3().intValue() + game.getScoreA4().intValue();
		int sumB = game.getScoreB1().intValue() + game.getScoreB2().intValue()
				+ game.getScoreB3().intValue() + game.getScoreB4().intValue();
		if (sumA == sumB) {
			ot = true;
			sumA += game.getScoreAV();
			sumB += game.getScoreBV();
		}
		String resultStr = sumA + ":" + sumB;

		if (game.getPeriods().equals(4)) {
			resultStr += " (" + game.getScoreA1() + ":" + game.getScoreB1()
					+ ", " + game.getScoreA2() + ":" + game.getScoreB2() + ", "
					+ game.getScoreA3() + ":" + game.getScoreB3() + ", "
					+ game.getScoreA4() + ":" + game.getScoreB4();
		} else if (game.getPeriods().equals(2)) {
			resultStr += " (" + game.getScoreA1() + ":" + game.getScoreB1()
					+ ", " + game.getScoreA3() + ":" + game.getScoreB3();
		}
		if (ot) {
			resultStr += ", " + game.getScoreAV() + ":" + game.getScoreBV();
		}
		if (game.getPeriods().intValue() > 1) {
			resultStr += ")";
		}
		return resultStr;
	}
}
