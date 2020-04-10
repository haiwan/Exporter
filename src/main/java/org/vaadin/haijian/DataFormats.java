package org.vaadin.haijian;

import java.util.Map;

/**
 * 
 * @author Krunoslav Magazin
 * Oct 5, 2019
 */
public interface DataFormats {

    void numberFormat(Class<? extends Number> clazz, String format);

    void localDateTimeFormat( String format);

    void localDateFormat( String format);
    
    void calendarFormat( String format);
    
    void dateFormat( String format);
    
    public Map<Class<?>, String> getTypeFormatsMap();

}
