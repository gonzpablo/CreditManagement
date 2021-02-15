package com.cred.model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.state.SetLineWidth;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

@SuppressWarnings("ALL")
public class ReportePagos {

	private static final float rowHeight = 20f;
	private static final float rowHeightL = 14f;
	private static final float beginX = 70;
	private static final float MARGEN_ARRIBA = 130;
	private final List<ReporteField> headerFields = new ArrayList<>();
	private final Stage stage;
	
	public ReportePagos(Stage stage) {
		this.stage = stage;
	}
	
	public void generarReporte(CreditoModel credito, ObservableList<PagoModel> pagos) throws IOException {	
	
		headerFields.add(new ReporteField("Fecha Pago", 60));
		headerFields.add(new ReporteField("Monto", 80));
		headerFields.add(new ReporteField("Comentarios", 150));

//		Dibujar cabecera

		float x, y;
		float tableWidth;

		String fileName = "ReportePagos_";

		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDate date1 = currentDateTime.toLocalDate();
		LocalTime time1 = currentDateTime.toLocalTime();
		
		
		String fechaFormat = date1.format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
		String horaFormat = time1.format(DateTimeFormatter.ofPattern("HH_mm_ss"));

		fileName = fileName + fechaFormat + "_" + horaFormat + ".pdf";
		
		fechaFormat = date1.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		horaFormat = time1.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		
		String cliente = "Cliente"; 
		String clienteValue = credito.getCliente();
		
		String montoCredito = "Monto crédito";
		String montoCreditoValue = credito.getMontoCredito().toString();
		
		String valorCuota = "Valor cuota";
		String valorCuotaValue = credito.getValorCuota().toString();
				
		String cantCuotas = "Cant. cuotas";
		String cantCuotasValue = String.valueOf(credito.getCantCuotas());
				
		String cobrador = "Cobrador";
		String cobradorValue = credito.getCobrador();
		
		String ruta = "Ruta"; 
		String rutaValue = credito.getRuta();
		
		String fecha = "Fecha Reporte"; 
		String fechaValue = fechaFormat;
		
		String hora = "Hora";
		String horaValue = horaFormat;		
		
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setInitialFileName(fileName);
		File file = fileChooser.showSaveDialog(this.stage);
		
		if (file == null)
			return;
		
	    PDDocument doc = new PDDocument();		
		
	    PDPage page = crearPagina();		// crear página A4	    
	    
	    doc.addPage( page );
	    PDPageContentStream contentStream = new PDPageContentStream(doc, page);		
	    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9 );
		tableWidth = page.getCropBox().getWidth();
	    
		x = beginX;
		y = page.getCropBox().getHeight() - MARGEN_ARRIBA;
		contentStream.setLineWidth(1);
		contentStream.setFont( PDType1Font.HELVETICA, 9 );

		float offset = y+80;

		contentStream.setFont( PDType1Font.HELVETICA_BOLD, 9 );		
		
		imprimirTexto(contentStream, cliente, x, offset, 0 );
		imprimirTexto(contentStream, montoCredito, x, offset-rowHeightL, 0 );
		imprimirTexto(contentStream, valorCuota, x+300, offset, 0 );
		imprimirTexto(contentStream, cantCuotas, x+300, offset-rowHeightL, 0 );
		imprimirTexto(contentStream, cobrador, x, offset-rowHeightL*2, 0 );
		imprimirTexto(contentStream, ruta, x+300, offset-rowHeightL*2, 0 );
		imprimirTexto(contentStream, fecha, x, offset-rowHeightL*3, 0 );
		imprimirTexto(contentStream, hora, x+300, offset-rowHeightL*3, 0 );

		contentStream.setFont( PDType1Font.HELVETICA, 9 );
		
		imprimirTexto(contentStream, clienteValue, x+100, offset, 0 );
		imprimirTexto(contentStream, montoCreditoValue, x+100, offset-rowHeightL, 0 );
		imprimirTexto(contentStream, valorCuotaValue, x+400, offset, 0 );
		imprimirTexto(contentStream, cantCuotasValue, x+400, offset-rowHeightL, 0 );
		imprimirTexto(contentStream, cobradorValue, x+100, offset-rowHeightL*2, 0 );
		imprimirTexto(contentStream, rutaValue, x+400, offset-rowHeightL*2, 0 );
		imprimirTexto(contentStream, fechaValue, x+100, offset-rowHeightL*3, 0 );
		imprimirTexto(contentStream, horaValue, x+400, offset-rowHeightL*3, 0 );			
		
		contentStream.setFont( PDType1Font.HELVETICA_BOLD, 9 );
//		Header		
		drawHeader(contentStream, headerFields, x, y);		
		
		x = beginX;
		y -= ReportePagos.rowHeight; 		
		
		
//		Detalle

		PDFont font = PDType1Font.HELVETICA;
		int fontSize = 9;
		String title = "test";
		float titleWidth = font.getStringWidth(title) / 1000 * fontSize;
	
		contentStream.setFont(font, fontSize);	
		
		drawDetail(doc, contentStream, pagos, x, y, font, fontSize);		
		
	    contentStream.close();

		doc.save(file);
		doc.close();		
		
		Desktop.getDesktop().open(file);		
	}
	
	private static void imprimirTexto(PDPageContentStream contentStream, String texto, float x, float y, float textWidth)  throws IOException {
		
		if (texto == null)
			return;
		
		contentStream.beginText();

		if (textWidth > 0) {

			contentStream.newLineAtOffset(x - textWidth, y);
			contentStream.showText(texto);
			contentStream.newLineAtOffset(x + textWidth, y);
			
		} else {
			contentStream.newLineAtOffset(x, y);
			contentStream.showText(texto);
		}
			
		contentStream.endText();		
	}
	
	private static void drawHeader(PDPageContentStream contentStream, List<ReporteField> headerFields, float startx, float starty) throws IOException {	

		float x, y, textWidth = 0;

		x = startx;
		y = starty; 	    
		
		contentStream.setNonStrokingColor(200, 200, 200); //gray background
		contentStream.addRect(startx, starty - 5, 290, rowHeight);
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

	private void drawDetail(PDDocument doc, PDPageContentStream contentStream, ObservableList<PagoModel> pagos, float x, float y, PDFont font, int fontSize) throws IOException {
		float startx = x;
		float textWidth = 0;
		PDPage page;

				
		for ( PagoModel pago : pagos ) {
			
			contentStream.drawLine(x, y+15, x, y+15-rowHeight);			
			imprimirTexto(contentStream, pago.getFechaValue().toString(), x+5, y, textWidth);
			x+=getLength("Fecha Pago");			
			contentStream.drawLine(x, y+15, x, y+15-rowHeight);
			
			textWidth = (font.getStringWidth(pago.getMontoPago().toString()) / 1000.0f) * fontSize;

			imprimirTexto(contentStream, pago.getMontoPago().toString(), x+73, y, textWidth);
			
			x+=getLength("Monto");
			
			textWidth = 0;
			contentStream.drawLine(x, y+15, x, y+15-rowHeight);

			x+=getLength("Comentarios");
			
			contentStream.drawLine(x, y+15, x, y+15-rowHeight);
			
			contentStream.drawLine(startx, y-5, x, y-5);		
			
			x=beginX;			
			y-=rowHeight;
			
			if ( y <= 50 ) {
				
				contentStream.close();			    
												
				page = crearPagina();		// crear página A4	    
			    doc.addPage( page );
			    contentStream = new PDPageContentStream(doc, page);
			    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8 );
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
		
	private static PDPage crearPagina() {
		return new PDPage(PDRectangle.A4); 
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