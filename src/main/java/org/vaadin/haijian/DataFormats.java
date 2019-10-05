package org.vaadin.haijian;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.time.LocalDate;

public interface DataFormats {

    void numberFormat(Class<? extends Number> clazz, String format);

    void localDateTimeFormat(Class<? extends LocalDateTime> clazz, String format);

    void localDateFormat(Class<? extends LocalDate> clazz, String format);
    
    void calendarFormat(Class<? extends Calendar> clazz, String format);
    
    void dateFormat(Class<? extends Date> clazz, String format);
    
    public Map<Class<?>, String> getTypeFormatsMap();



}
