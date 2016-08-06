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
public class Ride {
   Geographic source,destination;
    ArrayList<Stop> stops= new ArrayList<>();

    public Ride(Geographic source, Geographic destination) {
        this.source = source;
        this.destination = destination;
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

    public ArrayList<Stop> getStops() {
        return stops;
    }

    public void setStops(ArrayList<Stop> stops) {
        this.stops = stops;
    }
    
}
