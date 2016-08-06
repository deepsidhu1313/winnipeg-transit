/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import com.lynden.gmapsfx.javascript.object.LatLong;
import java.util.Locale;

/**
 *
 * @author nika
 */
public class Geographic {

    double latitude, longitude;

    public Geographic(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "lat: "+latitude+" lng:"+longitude; //To change body of generated methods, choose Tools | Templates.
    }

    public LatLong toLatLong(){
    return new LatLong(latitude, longitude);
    }
   

}
