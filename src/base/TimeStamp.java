/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nika
 */
public class TimeStamp {

    SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    Date date;

   public TimeStamp(String ts) {
        try {

            date = parserSDF.parse(ts);
        } catch (ParseException ex) {
            Logger.getLogger(TimeStamp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SimpleDateFormat getParserSDF() {
        return parserSDF;
    }

    public void setParserSDF(SimpleDateFormat parserSDF) {
        this.parserSDF = parserSDF;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
