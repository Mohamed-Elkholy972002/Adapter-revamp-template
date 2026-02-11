package com.fawry.adapter_template.util.adapterUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import org.springframework.stereotype.Component;

@Component
public class DateMapper {

    public Date xmlGregorianCalendarToDate(XMLGregorianCalendar xmlDate) {
        return xmlDate != null ? new Date(xmlDate.toGregorianCalendar().getTimeInMillis()) : null;
    }

    public Timestamp xmlGregorianCalendarToTimestamp(XMLGregorianCalendar xmlDateTime) {
        return xmlDateTime != null ? new Timestamp(xmlDateTime.toGregorianCalendar().getTimeInMillis()) : null;
    }
    
    public Calendar xmlGregorianCalendarToCalendar(XMLGregorianCalendar xmlDate) {
        return xmlDate != null ? xmlDate.toGregorianCalendar() : null;
    }
    
    public XMLGregorianCalendar calendarToXmlGregorianCalendar(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("Calendar cannot be null");
        }

        try {
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            return datatypeFactory.newXMLGregorianCalendar(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1, // Months are 0-based in Calendar
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND),
                    calendar.get(Calendar.MILLISECOND),
                    calendar.getTimeZone().getRawOffset() / (60 * 1000) // Timezone offset in minutes
            );
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Error creating XMLGregorianCalendar: " + e.getMessage(), e);
        }
    }
}