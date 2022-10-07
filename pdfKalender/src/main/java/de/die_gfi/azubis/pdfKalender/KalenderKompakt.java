package de.die_gfi.azubis.pdfKalender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

public class KalenderKompakt {

	public final static String PATH = "src\\main\\resources\\img\\";

	public final static String[] MONATE_DE = { "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August",
			"September", "Oktober", "November", "Dezember" };
	public final static String[] WOCHENTAGE_DE = { "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag",
			"Sonntag" };

	public final static int ZELLENHOEHE = 20;

	public static PdfFont ueberschrift;
	public static PdfFont standard;

	public static void main(String[] args) throws IOException {

		jahreskalenderErstellen(2022);
	}

	private static void jahreskalenderErstellen(int jahr) throws IOException {
		ueberschrift = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);
		standard = PdfFontFactory.createFont(StandardFonts.COURIER);

		Document document = pdfSetup();

		for (int i = 1; i <= 12; i++) {
			monatErstellen(document, i, jahr);
		}

		// Close document
		document.close();

		System.out.println("kompaktkalenderfertig");
	}

	private static void monatErstellen(Document document, int monat, int jahr) {

		Paragraph paragraph = new Paragraph(MONATE_DE[monat - 1] + " " + jahr).setFont(ueberschrift).setFontSize(20)
				.setItalic().setTextAlignment(TextAlignment.LEFT);

		document.add(paragraph);

		document.add(tabelleTageErstellen(monat, jahr).setMarginBottom(50));
		if (monat % 3 == 0 && monat < 12) {
			document.add(new AreaBreak());
		}

	}

	private static Document pdfSetup() throws FileNotFoundException {
		String dest = "results/Forster_KalenderKompakt.pdf";

		// Initialize PDF writer
		PdfWriter writer = new PdfWriter(dest);

		// Initialize PDF document
		PdfDocument pdf = new PdfDocument(writer);

		// Initialize document
		Document document = new Document(pdf);
		return document;
	}

	private static Table tabelleTageErstellen(int monat, int jahr) {
		Table tabelle = new Table(UnitValue.createPercentArray(new float[] { 1, 1, 1, 1, 1, 1, 1 }))
				.useAllAvailableWidth();

		kopfzeileHinzufuegen(tabelle);

		koerperHinzufuegen(tabelle, monat, jahr);

		return tabelle;
	}

	private static void kopfzeileHinzufuegen(Table tabelle) {
		for (int i = 0; i < 7; i++) {
			Cell zelle = new Cell().add(new Paragraph(WOCHENTAGE_DE[i]).setFont(ueberschrift)).setHeight(20)
					.setTextAlignment(TextAlignment.CENTER);

			if (i == 6) {
				zelle.setFontColor(ColorConstants.RED);
			}

			tabelle.addCell(zelle);
		}
	}

	private static void koerperHinzufuegen(Table tabelle, int monat, int jahr) {
		LocalDate ersterTag = LocalDate.of(jahr, monat, 1);

		int anzTage = ersterTag.lengthOfMonth();
		int anzLeerzellen = ersterTag.getDayOfWeek().getValue() - 1;

		for (int i = 0; i < anzLeerzellen; i++) {
			Cell zelle = new Cell().setHeight(ZELLENHOEHE);

			if (i != 0) {
				zelle.setBorderLeft(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));
			}

			if (i != anzLeerzellen - 1) {
				zelle.setBorderRight(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));
			}

			tabelle.addCell(zelle);
		}

		for (int i = 0; i < anzTage; i++) {
			Cell zelle = new Cell().setHeight(ZELLENHOEHE).add(new Paragraph("" + (i + 1)).setFont(standard));

			if ((i + 1 + anzLeerzellen) % 7 == 0) {
				zelle.setFontColor(ColorConstants.RED);
			}

			tabelle.addCell(zelle);
		}

	}

}