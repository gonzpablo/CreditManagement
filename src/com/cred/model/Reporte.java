package com.cred.model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.contentstream.operator.state.SetLineWidth;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Reporte {

	private static final float rowHeight = 20f;
	private static final float beginX = 70;
	private static final float MARGEN_ARRIBA = 90;
	private List<ReporteField> headerFields = new ArrayList<ReporteField>();
	private Stage stage;
	
	public Reporte(Stage stage) {
		this.stage = stage;
	}
	
	private static void imprimirTexto(PDPageContentStream contentStream, String texto, float x, float y, float textWidth)  throws IOException {
		
		if (texto == null)
			return;
		
		contentStream.beginText();

		if (textWidth > 0) {

			contentStream.moveTextPositionByAmount(x-textWidth, y);
			contentStream.drawString(texto);
			contentStream.moveTextPositionByAmount(x+textWidth, y);
			
		} else {
			contentStream.moveTextPositionByAmount(x, y);
			contentStream.drawString(texto);
		}
			
		contentStream.endText();		
	}
	
	private static PDPage crearPagina() {
		return new PDPage(PDRectangle.A4); 
	}
	
	public void reporteRuta(FilteredList<CreditoModel> creditos) throws IOException {	

		headerFields.add(new ReporteField("Cliente", 110));
		headerFields.add(new ReporteField("Valor Cuota", 80));
		headerFields.add(new ReporteField("Monto Crédito", 80));
		headerFields.add(new ReporteField("CP", 30));
		headerFields.add(new ReporteField("CC", 30));
		headerFields.add(new ReporteField("Cobrador", 80));
		headerFields.add(new ReporteField("Ruta", 50));
		
//		Dibujar cabecera

		float x, y;
		float tableWidth; 

		String fileName = "Reporte_";

		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDate date1 = currentDateTime.toLocalDate();
		LocalTime time1 = currentDateTime.toLocalTime();
		
		
		String fechaFormat = date1.format(DateTimeFormatter.ofPattern("dd_MM_YYYY"));
		String horaFormat = time1.format(DateTimeFormatter.ofPattern("HH_mm_ss"));

		fileName = fileName + fechaFormat + "_" + horaFormat + ".pdf";
		
		fechaFormat = date1.format(DateTimeFormatter.ofPattern("dd/MM/YYYY"));
		horaFormat = time1.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		
		String fecha = "Fecha Reporte: " + fechaFormat;
		String hora = "Hora: " + horaFormat;		
		
		
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setInitialFileName(fileName);
		File file = fileChooser.showSaveDialog(this.stage);
		
		if (file == null)
			return;
		
	    PDDocument doc = new PDDocument();		
		
	    PDPage page = crearPagina();		// crear página A4	    
	    
	    doc.addPage( page );
	    PDPageContentStream contentStream = new PDPageContentStream(doc, page);		
	    contentStream.setFont( PDType1Font.HELVETICA.HELVETICA_BOLD , 9 );
		tableWidth = page.getCropBox().getWidth();
	    
		x = beginX;
		y = page.getCropBox().getHeight() - MARGEN_ARRIBA;
		contentStream.setLineWidth(1);
		contentStream.setFont( PDType1Font.HELVETICA, 9 );

		
		imprimirTexto(contentStream, fecha, x, y+50, 0 );
		
		imprimirTexto(contentStream, hora, x, y+50-rowHeight, 0 );
		
		contentStream.setFont( PDType1Font.HELVETICA_BOLD, 9 );
//		Header		
		drawHeader(contentStream, headerFields, x, y);		
		
		x = beginX;
		y -= Reporte.rowHeight; 	
		
//		Detalle

		PDFont font = PDType1Font.HELVETICA;
		int fontSize = 9; // Or whatever font size you want.
		String title = "test";
		float titleWidth = font.getStringWidth(title) / 1000 * fontSize;
	
		contentStream.setFont(font, fontSize);	
		
		drawDetail(doc, contentStream, creditos, x, y, font, fontSize);
		
	    contentStream.close();

		doc.save(file);
		doc.close();		
		
		Desktop.getDesktop().open(file);
	}

	private static void drawHeader(PDPageContentStream contentStream, List<ReporteField> headerFields, float startx, float starty) throws IOException {	

		float x, y, textWidth = 0;

		x = startx;
		y = starty; 	    
		
		contentStream.setNonStrokingColor(200, 200, 200); //gray background
		contentStream.fillRect(startx, starty-5, 460, rowHeight);
		contentStream.setNonStrokingColor(0, 0, 0); //gray background
		
		for ( ReporteField field : headerFields) {

			imprimirTexto(contentStream, field.getName(), x+5, y, textWidth);
			contentStream.drawLine(x, starty+15, x, starty+15-rowHeight);			
			x+=field.getLength();	       			
		}
		
		contentStream.drawLine(x, starty+15, x, starty+15-rowHeight);
		
//		linea arriba		
		starty = starty + 15;
		contentStream.drawLine(startx, starty, x, starty);
		SetLineWidth setl = new SetLineWidth();
//		linea abajo
		contentStream.drawLine(startx, starty-rowHeight, x, starty-rowHeight);		
	}
	
	private void drawDetail(PDDocument doc, PDPageContentStream contentStream, FilteredList<CreditoModel> creditos, float x, float y, PDFont font, int fontSize) throws IOException {
		float startx = x;
		float textWidth = 0;
		PDPage page;

				
		for ( CreditoModel credito : creditos ) {
			
			if (credito.isCerrado())
				continue;
			
			contentStream.drawLine(x, y+15, x, y+15-rowHeight);			
			imprimirTexto(contentStream, credito.getCliente(), x+5, y, textWidth);
			x+=getLength("Cliente");			
			contentStream.drawLine(x, y+15, x, y+15-rowHeight);
			
			textWidth = (font.getStringWidth(credito.getValorCuota().toString()) / 1000.0f) * fontSize;

			imprimirTexto(contentStream, credito.getValorCuota().toString(), x+73, y, textWidth);
			
			x+=getLength("Valor Cuota");
			
			textWidth = 0;
			contentStream.drawLine(x, y+15, x, y+15-rowHeight);
			textWidth = (font.getStringWidth(credito.getMontoCredito().toString()) / 1000.0f) * fontSize;

			imprimirTexto(contentStream, credito.getMontoCredito().toString(), x+73, y, textWidth);
			x+=getLength("Monto Crédito");			
			
			textWidth = 0;
			contentStream.drawLine(x, y+15, x, y+15-rowHeight);			
			imprimirTexto(contentStream, String.valueOf(credito.getCuotasPagas()), x+5, y, textWidth);
			x+=getLength("CP");			
			contentStream.drawLine(x, y+15, x, y+15-rowHeight);			
			imprimirTexto(contentStream, String.valueOf(credito.getCantCuotas()), x+5, y, textWidth);
			x+=getLength("CC");			
			contentStream.drawLine(x, y+15, x, y+15-rowHeight);			
			imprimirTexto(contentStream, credito.getCobrador(), x+5, y, textWidth);
			x+=getLength("Cobrador");			
			contentStream.drawLine(x, y+15, x, y+15-rowHeight);			
			imprimirTexto(contentStream, credito.getRuta(), x+5, y, textWidth);
			x+=getLength("Ruta");
			contentStream.drawLine(x, y+15, x, y+15-rowHeight);
			
			contentStream.drawLine(startx, y-5, x, y-5);		
			
			x=beginX;			
			y-=rowHeight;
			
			if ( y <= 50 ) {
				
				contentStream.close();			    
												
				page = crearPagina();		// crear página A4	    
			    doc.addPage( page );
			    contentStream = new PDPageContentStream(doc, page);
			    contentStream.setFont( PDType1Font.HELVETICA.HELVETICA_BOLD , 8 );
				x = beginX;
				y = page.getCropBox().getHeight() - MARGEN_ARRIBA;

				contentStream.setFont( PDType1Font.HELVETICA_BOLD, 9);
//				Header		
				drawHeader(contentStream, headerFields, x, y);		
					

				y = page.getCropBox().getHeight() - MARGEN_ARRIBA;
				y-=rowHeight;	// por la cabecera
				
				contentStream.setFont( PDType1Font.HELVETICA, 9);				
			}			
		}
		contentStream.close();
	}
	
	private int getLength(String campo) {
		
		int length = 0;
		
		for ( ReporteField field : headerFields ) {
			if ( field.getName().equals(campo) ) {
				length = field.getLength();
				break;
			}
		}
		
		return length;
	}
}