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
public class Walk {
    Geographic source,destination;
    long timeToWalk;
    String stopNum;

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

    public long getTimeToWalk() {
        return timeToWalk;
    }

    public void setTimeToWalk(long timeToWalk) {
        this.timeToWalk = timeToWalk;
    }

    public String getStopNum() {
        return stopNum;
    }

    public void setStopNum(String stopNum) {
        this.stopNum = stopNum;
    }

    public Walk(Geographic source, Geographic destination, long timeToWalk, String stopNum) {
        this.source = source;
        this.destination = destination;
        this.timeToWalk = timeToWalk;
        this.stopNum = stopNum;
    }
}
