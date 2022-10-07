package de.die_gfi.azubis.pdfKalender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

public class Kalender {

	public final static String PATH = "src\\main\\resources\\img\\";

	public final static String[] MONATE_DE = { "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August",
			"September", "Oktober", "November", "Dezember" };
	public final static String[] WOCHENTAGE_DE = { "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag",
			"Sonntag" };

	public static PdfFont ueberschrift;
	public static PdfFont standard;

	public static void main(String[] args) throws IOException {

		// bilderErzeugen();

		kalenderErstellen();
	}

	private static void bilderErzeugen() throws MalformedURLException {

		for (int i = 0; i < 12; i++) {

		}

	}

	private static void kalenderErstellen() throws IOException {
		ueberschrift = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);
		standard = PdfFontFactory.createFont(StandardFonts.COURIER);

		Document document = pdfSetup();

		document.add(new Paragraph(MONATE_DE[9]).setFont(ueberschrift).setFontSize(20));

		document.add(tabelleTageErstellen());

		// Close document
		document.close();

		System.out.println("kalenderfertig");
	}

	private static Document pdfSetup() throws FileNotFoundException {
		String dest = "results/Forster_Kalender.pdf";

		// Initialize PDF writer
		PdfWriter writer = new PdfWriter(dest);

		// Initialize PDF document
		PdfDocument pdf = new PdfDocument(writer);

		// Initialize document
		Document document = new Document(pdf);
		return document;
	}

	private static Table tabelleTageErstellen() {
		Table tage = new Table(UnitValue.createPercentArray(new float[] { 1, 1, 1, 1, 1, 1, 1 }))
				.useAllAvailableWidth();

		kopfzeileHinzufuegen(tage);

		return tage;
	}

	private static void kopfzeileHinzufuegen(Table tage) {
		for (int i = 0; i < 7; i++) {
			tage.addCell(new Cell().add(new Paragraph(WOCHENTAGE_DE[i]).setFont(standard)).setHeight(20));
		}
	}

}