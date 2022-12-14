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

public class Kalender {

	public final static String PATH = "src\\main\\resources\\img\\";

	public final static String[] MONATE_DE = { "Januar", "Februar", "M?rz", "April", "Mai", "Juni", "Juli", "August",
			"September", "Oktober", "November", "Dezember" };
	public final static String[] WOCHENTAGE_DE = { "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag",
			"Sonntag" };

	public final static int ZELLENHOEHE = 50;

	public static PdfFont ueberschrift;
	public static PdfFont standard;

	public static Image[] bilder = new Image[12];
	public static String[] bildquellen = new String[12];
	public static String[] bildnamen = { "schneeberge.jpg", "schneegloeckchen.jpg", "osterglocken.jpg",
			"regenbogen.jpg", "kirschbluete.jpg", "sommerberg.jpg", "kornfeld.jpg", "suedsee.jpg", "wald.jpg",
			"herbstwald.jpg", "regenschirm.jpg", "schneewald.jpg" };

	public static void main(String[] args) throws IOException {

		bilderErzeugen();
		bildquellenEinlesen();

		jahreskalenderErstellen(2022);
	}

	private static void bilderErzeugen() throws MalformedURLException {

		for (int i = 0; i < 12; i++) {
			bilder[i] = new Image(ImageDataFactory.create(PATH + bildnamen[i]));
		}

	}

	private static void bildquellenEinlesen() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(PATH + "bildquellen.txt"));

		for (int i = 0; i < 12; i++) {
			bildquellen[i] = reader.readLine();
		}

		reader.close();
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

		System.out.println("kalenderfertig");
	}

	private static void monatErstellen(Document document, int monat, int jahr) {

		document.add(bilder[monat - 1]);
		document.add(new Paragraph(bildquellen[monat - 1]).setFont(standard).setFontSize(4)
				.setTextAlignment(TextAlignment.RIGHT));

		Table rahmen = new Table(1).useAllAvailableWidth();
		Cell zelle = new Cell().setPadding(0).setBorder(new SolidBorder(ColorConstants.BLACK, 2));
		zelle.add(new Paragraph(MONATE_DE[monat - 1]).setFont(ueberschrift).setFontSize(40)
				.setTextAlignment(TextAlignment.CENTER));

		zelle.add(tabelleTageErstellen(monat, jahr));
		rahmen.addCell(zelle);
		document.add(rahmen);
		if (monat < 12) {
			document.add(new AreaBreak());
		}
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

	private static Table tabelleTageErstellen(int monat, int jahr) {
		Table tabelle = new Table(UnitValue.createPercentArray(new float[] { 1, 1, 1, 1, 1, 1, 1 }))
				.useAllAvailableWidth();

		kopfzeileHinzufuegen(tabelle);

		koerperHinzufuegen(tabelle, monat, jahr);

		return tabelle;
	}

	private static void kopfzeileHinzufuegen(Table tabelle) {
		for (int i = 0; i < 7; i++) {
			tabelle.addCell(new Cell().add(new Paragraph(WOCHENTAGE_DE[i]).setFont(ueberschrift)).setHeight(20)
					.setTextAlignment(TextAlignment.CENTER));
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
			tabelle.addCell(new Cell().setHeight(ZELLENHOEHE).add(new Paragraph("" + (i + 1)).setFont(standard)));
		}

	}

}