package cred;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class Reporte {

	public static void reporteRuta(FilteredList<CreditoModel> creditos) throws IOException {
		
	    PDDocument doc = new PDDocument();
	    PDPage page = new PDPage();
	    doc.addPage( page );
	    PDPageContentStream contentStream = new PDPageContentStream(doc, page);

	    drawTable(page, contentStream, 700, 100, creditos);

	    contentStream.close();
	    doc.save("c:/temp/test_table.pdf" );
	}

	public static void drawTable(PDPage page, PDPageContentStream contentStream,
									float y, float margin,
									ObservableList<CreditoModel> creditos) throws IOException {
		

		final int rows = creditos.size();
		final int cols = 7;
		final float rowHeight = 20f;
		final float tableWidth = page.getCropBox().getWidth() - margin - margin;
		final float tableHeight = rowHeight * rows;
		final float colWidth = tableWidth/(float)cols;
		final float cellMargin=5f;

		//draw the rows
		float nexty = y ;
		
		for (int i = 0; i <= rows; i++) {
//			**
//			 * Draw a line on the page using the current non stroking color and the current line width.
//			 *
//			 * @param xStart The start x coordinate.
//			 * @param yStart The start y coordinate.
//			 * @param xEnd The end x coordinate.
//			 * @param yEnd The end y coordinate.
//			 * @throws IOException If there is an error while drawing on the screen.
//			 */
//			public void drawLine(float xStart, float yStart, float xEnd, float yEnd) throws IOException
//			{			
			contentStream.drawLine(margin, nexty, margin+tableWidth, nexty);
		//	contentStream.addRect(10, 10, 10, 10);
		
			nexty-= rowHeight;
		}

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
		
		
//		loop de Registros		
		for ( CreditoModel cred : creditos) {		
			
//			Loop de columnas			
			textx+= imprimirDato(cred.getCliente(), contentStream, textx, texty, colWidth+30);
			textx+=imprimirDato(cred.getValorCuota().toString(), contentStream, textx, texty, colWidth);
			textx+=imprimirDato(cred.getMontoCredito().toString(), contentStream, textx, texty, colWidth);
			textx+=imprimirDato(String.valueOf(cred.getCuotasPagas()), contentStream, textx, texty, colWidth-15);
			textx+=imprimirDato(String.valueOf(cred.getCantCuotas()), contentStream, textx, texty, colWidth-15);
			textx+=imprimirDato(cred.getCobrador(), contentStream, textx, texty, colWidth);
			textx+=imprimirDato(cred.getRuta(), contentStream, textx, texty, colWidth);
			
			texty-=rowHeight;
			textx = margin+cellMargin;
		}		
	}

	private static float imprimirDato(String dato, PDPageContentStream contentStream, float textx, float texty, float colWidth) throws IOException {
	
		contentStream.beginText();

		contentStream.setFont( PDType1Font.HELVETICA , 10 );

		contentStream.moveTextPositionByAmount(textx,texty);
		contentStream.drawString(dato);
		contentStream.endText();
//		textx += colWidth;
		return colWidth;
		
	}	
}