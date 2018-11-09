package org.vaadin.haijian;

class ExporterException extends RuntimeException {
    ExporterException(String message) {
        super(message);
    }

    ExporterException(String message, Exception e) {
        super(message, e);
    }
}
