package org.vaadin.haijian;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.BuiltinFormats;

/**
 * 
 * @author Krunoslav Magazin
 * Oct 5, 2019
 * 
 * Initialize default excel formats. Excel {@link BuiltinFormats} use locale to format value.</p>
 * 
 */
public class TypeDataFormats implements DataFormats {

    private final Map<Class<?>, String> typeFormatsMap;

    public TypeDataFormats() {
        this.typeFormatsMap = new HashMap<Class<?>, String>();
        typeFormatsMap.put(Integer.class, "0");
        typeFormatsMap.put(Long.class, "0");
        typeFormatsMap.put(Double.class, "0.00");
        typeFormatsMap.put(BigDecimal.class, "0.00");
        typeFormatsMap.put(LocalDateTime.class, "m/d/yy h:mm");
        typeFormatsMap.put(Date.class, "m/d/yy h:mm");
        typeFormatsMap.put(Calendar.class, "m/d/yy h:mm");
        typeFormatsMap.put(LocalDate.class, "m/d/yy");
    }

    @Override
    public void numberFormat(Class<? extends Number> clazz, String format) {
        setTypeFormat(clazz, format);
    }

    @Override
    public void localDateTimeFormat( String format) {
        setTypeFormat(LocalDateTime.class, format);
    }

    @Override
    public void localDateFormat( String format) {
        setTypeFormat(LocalDate.class, format);
    }

    @Override
    public void calendarFormat( String format) {
        setTypeFormat(Calendar.class, format);
    }

    @Override
    public void dateFormat( String format) {
        setTypeFormat(Date.class, format);
    }

    private void setTypeFormat(Class<?> clazz, String format) {
        typeFormatsMap.put(clazz, format);
    }

    @Override
    public Map<Class<?>, String> getTypeFormatsMap() {
        return typeFormatsMap;
    }

}
