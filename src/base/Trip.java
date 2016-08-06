/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import java.util.ArrayList;

/**
 *
 * @author nika
 */
public class Trip {
    ArrayList<Walk> walks= new ArrayList<>();
    Geographic source,destination;
    ArrayList<Ride> rides= new ArrayList<>();
    String seq="";
    public Trip(Geographic source, Geographic destination) {
        this.source = source;
        this.destination = destination;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    
    
    public ArrayList<Walk> getWalks() {
        return walks;
    }

    public void setWalks(ArrayList<Walk> walks) {
        this.walks = walks;
    }

    public Geographic getSource() {
        return source;
    }

    public void setSource(Geographic source) {
        this.source = source;
    }

    public Geographic getDestination() {
        return destination;
    }

    public void setDestination(Geographic destination) {
        this.destination = destination;
    }

    public ArrayList<Ride> getRides() {
        return rides;
    }

    public void setRides(ArrayList<Ride> rides) {
        this.rides = rides;
    }
    
}
