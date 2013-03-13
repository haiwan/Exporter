package org.vaadin.haijian;

import org.vaadin.haijian.filegenerator.FileBuilder;
import org.vaadin.haijian.filegenerator.PdfFileBuilder;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;

public class PdfExporter extends Exporter {
    public PdfExporter() {
        super();
    }

    public PdfExporter(Table table) {
        super(table);
    }

    public PdfExporter(Container container, Object[] visibleColumns) {
        super(container, visibleColumns);
    }

    public PdfExporter(Container container) {
        super(container);
    }

    @Override
    protected FileBuilder createFileBuilder(Container container) {
        return new PdfFileBuilder(container);
    }

    @Override
    protected String getDownloadFileName() {
        return "exported-pdf.pdf";
    }

    public void setWithBorder(boolean withBorder) {
        ((PdfFileBuilder) fileBuilder).setWithBorder(withBorder);
    }

}
