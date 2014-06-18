package gov.nih.nci.ctcae.web.reports;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author AmeyS
 * This is a custom PdfPageEvent handler.
 * For every new page on the pdf document, OnStartPage() event will paint page numbers in header section of that page.
 */
public class MyPdfPageEvent implements PdfPageEvent {
	private Integer totalPages = 0;
	private static Log logger = LogFactory.getLog(MyPdfPageEvent.class);
	
	@Override
	public void onStartPage(PdfWriter pdfWriter, Document document) {
		PdfContentByte cb = pdfWriter.getDirectContent();
        cb.beginText();
		try {
			cb.setFontAndSize(BaseFont.createFont(BaseFont.TIMES_ITALIC, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED), 11);
			int currentPageNumber = pdfWriter.getPageNumber() - 1;
			cb.showTextAligned(Element.ALIGN_LEFT, "(Page "+ currentPageNumber + " of " + getTotalPages() + ")", 34, 510, 0);
			cb.endText();
		} catch (DocumentException e) {
			logger.error("Error in displaying page number" + e.getStackTrace());
		} catch (IOException e) {
			logger.error("Error in displaying page number" + e.getStackTrace());
		}
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	
	@Override
	public void onEndPage(PdfWriter pdfWriter, Document document) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onChapter(PdfWriter arg0, Document arg1, float arg2,
			Paragraph arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onChapterEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCloseDocument(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGenericTag(PdfWriter arg0, Document arg1, Rectangle arg2,
			String arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onOpenDocument(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onParagraph(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onParagraphEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSection(PdfWriter arg0, Document arg1, float arg2, int arg3,
			Paragraph arg4) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSectionEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub
	}
}
