package cred;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class Reporte {

	public static void reporteRuta(FilteredList<CreditoModel> creditos) throws IOException {		
		
	    PDDocument doc = new PDDocument();
	    
	    drawTable(doc, 750, 50, creditos);
	    
//	    PDPage page2 = new PDPage();	    
//
//	    doc.addPage(page2);
//	    PDPageContentStream contentStream2 = new PDPageContentStream(doc, page2);	    
//		contentStream2.beginText();
//		contentStream2.setFont( PDType1Font.HELVETICA.HELVETICA_BOLD , 9 );		
//	    contentStream2.drawString("test");
//	    contentStream2.endText();	    
//	    contentStream2.close();
	    
	    doc.save("c:/temp/test_table.pdf" );
	    doc.close();
	}

	public static void drawTable(PDDocument doc, float y, float margin,
									ObservableList<CreditoModel> creditos) throws IOException {
		

		final int rows = creditos.size() + 1;  // +1 es para el heading
		final int cols = 7;
		final float rowHeight = 20f;
		final float tableWidth;
		final float tableHeight = rowHeight * rows;
		final float colWidth;
		final float cellMargin=5f;

		//draw the rows
		float nexty = y ;
		
		
	    PDPage page = crearPagina();		// crear página A4	    
	    tableWidth = page.getCropBox().getWidth() - margin - margin;
	    colWidth = tableWidth/(float)cols;
	    
	    doc.addPage( page );
	    PDPageContentStream contentStream = new PDPageContentStream(doc, page);

		contentStream.setNonStrokingColor(200, 200, 200); //gray background
		contentStream.fillRect(margin, nexty-rowHeight, tableWidth, 20);		
		
		for (int i = 0; i <= rows; i++) {
//			 * @param xStart The start x coordinate.
//			 * @param yStart The start y coordinate.
//			 * @param xEnd The end x coordinate.
//			 * @param yEnd The end y coordinate.
//			 * @throws IOException If there is an error while drawing on the screen.

//			public void drawLine(float xStart, float yStart, float xEnd, float yEnd) throws IOException
			
			contentStream.drawLine(margin, nexty, margin+tableWidth, nexty);

//			contentStream.fillRect(margin, nexty, margin+tableWidth, nexty);
			contentStream.moveTo(margin, nexty);
			contentStream.lineTo(margin+tableWidth, nexty);
//			contentStream.stroke();
//			contentStream.addRect(10, 10, 10, 10);
		
			nexty-= rowHeight;
			System.out.println(nexty);
			
			if ( nexty < 10 ) {
				contentStream.close();
			    page = crearPagina();		// crear página A4	    
			    doc.addPage( page );
			    contentStream = new PDPageContentStream(doc, page);

				contentStream.setNonStrokingColor(200, 200, 200); //gray background
				contentStream.fillRect(margin, nexty-rowHeight, tableWidth, 20);	
				
				nexty = y ;
			}
			
//			-----------------------------------------------			
			
			
			
		}

		contentStream.setNonStrokingColor(0, 0, 0); //black text
		
		//draw the columns
		float nextx = margin;
		for (int i = 0; i <= cols; i++) {
			contentStream.drawLine(nextx, y, nextx, y-tableHeight);		
			
			switch (i) {
			case 0 : { nextx += colWidth+30; break; }
			case 3 : { nextx += colWidth-15; break; }
			case 4 : { nextx += colWidth-15; break; }
			default: { nextx += colWidth; }
			}
			
//			if (i == 0)
//				nextx += colWidth+30;
//			else
//				nextx += colWidth;
			
//			if (i == cols)
//				nextx += colWidth-30;
			
		}

//		now add the text
		contentStream.setFont( PDType1Font.HELVETICA_BOLD , 12 );
		
		float textx = margin+cellMargin;
		float texty = y-15;
		
		textx+=imprimirDato(1, "Cliente", contentStream, textx, texty, colWidth+30);
		textx+=imprimirDato(1, "Valor Cuota", contentStream, textx, texty, colWidth);		
		textx+=imprimirDato(1, "Monto Cr.", contentStream, textx, texty, colWidth);		
		textx+=imprimirDato(1, "CP", contentStream, textx, texty, colWidth-15);		
		textx+=imprimirDato(1, "CC", contentStream, textx, texty, colWidth-15);		
		textx+=imprimirDato(1, "Cobrador", contentStream, textx, texty, colWidth);		
		textx+=imprimirDato(1, "Ruta", contentStream, textx, texty, colWidth);
		
		texty-=rowHeight;

		textx = margin+cellMargin;
//		loop de Registros		
		for ( CreditoModel cred : creditos) {		
			
//			Loop de columnas			
			textx+= imprimirDato(2, cred.getCliente(), contentStream, textx, texty, colWidth+30);
			textx+=imprimirDato(2, cred.getValorCuota().toString(), contentStream, textx, texty, colWidth);
			textx+=imprimirDato(2, cred.getMontoCredito().toString(), contentStream, textx, texty, colWidth);
			textx+=imprimirDato(2, String.valueOf(cred.getCuotasPagas()), contentStream, textx, texty, colWidth-15);
			textx+=imprimirDato(2, String.valueOf(cred.getCantCuotas()), contentStream, textx, texty, colWidth-15);
			textx+=imprimirDato(2, cred.getCobrador(), contentStream, textx, texty, colWidth);
			textx+=imprimirDato(2, cred.getRuta(), contentStream, textx, texty, colWidth);
			
			texty-=rowHeight;
			textx = margin+cellMargin;
			
			
		}		
		
	    contentStream.close();
	}

	private static float imprimirDato(int tipoLinea, String dato, PDPageContentStream contentStream, float textx, float texty, float colWidth) throws IOException {
	
		contentStream.beginText();

		if (tipoLinea == 1)
			contentStream.setFont( PDType1Font.HELVETICA.HELVETICA_BOLD , 9 );
		else
			contentStream.setFont( PDType1Font.HELVETICA , 10 );

		contentStream.moveTextPositionByAmount(textx,texty);
		contentStream.drawString(dato);
		contentStream.endText();
//		textx += colWidth;
		return colWidth;
		
	}
	
	private static PDPage crearPagina() {
		return new PDPage(PDRectangle.A4); 
	}
}