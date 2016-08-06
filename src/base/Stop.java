/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

/**
 *
 * @author nika
 */
public class Stop {

    String key, number;
    String name, direction;
    Geographic geographic;

    public Stop(String key, String number, String name, Geographic geographic) {
        this.key = key;
        this.number = number;
        this.name = name;
        this.geographic = geographic;
    }

    public Stop(String key, String number, String name, String direction, Geographic geographic) {
        this.key = key;
        this.number = number;
        this.name = name;
        this.direction = direction;
        this.geographic = geographic;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Geographic getGeographic() {
        return geographic;
    }

    public void setGeographic(Geographic geographic) {
        this.geographic = geographic;
    }

    public double getLatitude() {
        return this.geographic.getLatitude();
    }

    public void setLatitude(double latitude) {
        this.geographic.setLatitude(latitude);
    }

    public double getLongitude() {
        return this.geographic.getLongitude();
    }

    public void setLongitude(double longitude) {
        this.geographic.setLongitude(longitude);
    }

    @Override
    public String toString() {
        return "Stop: "+getKey()+" "+getName(); //To change body of generated methods, choose Tools | Templates.
    }
    
    

}
