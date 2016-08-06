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
public class Route {
   String key, number;
   String name,customer_type,coverage;

    public Route(String key, String number, String name) {
        this.key = key;
        this.number = number;
        this.name = name;
    }

    public Route(String key, String number, String name, String customer_type, String coverage) {
        this.key = key;
        this.number = number;
        this.name = name;
        this.customer_type = customer_type;
        this.coverage = coverage;
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

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }


}
