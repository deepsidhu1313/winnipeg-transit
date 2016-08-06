/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;

/**
 *
 * @author nika
 */
public class TreePlan {
    
    TreeItem<String> plan = new TreeItem<>();
    String busNo, rt;
    long remainingtime = 0;
    Date departureTime, endTime;
    SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    TreeItem root;
    public TreePlan(String busNo, Date departureTime, Date endTime, TreeItem root) {
        this.busNo = busNo;
        this.departureTime = departureTime;
        this.endTime = endTime;
        this.root=root;
        parserSDF.setLenient(false);
        updateRemainingTime();
    }

    public TreePlan(TreeItem root) {
        this.root = root;
    }

    
    public TreeItem getRoot() {
        return root;
    }

    public void setRoot(TreeItem root) {
        this.root = root;
    }
    
    
    public TreeItem<String> getPlan() {
        return plan;
    }
    
    public void setPlan(TreeItem<String> plan) {
        this.plan = plan;
    }
    
    public String getBusNo() {
        return busNo;
    }
    
    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }
    
    public long getRemainingtime() {
        return remainingtime;
    }
    
    public void setRemainingtime(long remainingtime) {
        this.remainingtime = remainingtime;
    }
    
    public Date getDepartureTime() {
        return departureTime;
    }
    
    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
        updateRemainingTime();
    }
    
    public Date getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    void addChildren(TreeItem item) {
        plan.getChildren().add(plan);
    }
    
    void updateTime() {
        
        plan.setValue("" + busNo + " - " + rt);
    }
    
    void updateRemainingTime() {
        Thread t = new Thread(() -> {
            while (true) {
                long diff = departureTime.getTime() - Calendar.getInstance().getTime().getTime();
                
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                System.out.println("Dept: " + parserSDF.format(departureTime) + " Curr: " + parserSDF.format(Calendar.getInstance().getTime().getTime()));
                System.out.println("Diff " + diff + " s " + diffSeconds + " M " + diffMinutes + " H " + diffHours);
//long diffDays = diff / (24 * 60 * 60 * 1000);
                if (diffHours >= 1) {
                    rt = "~" + diffHours + " Hours";
                } else if (diffMinutes >= 1) {
                    rt = diffMinutes + " Minutes";
                } else if (diffSeconds >= 1) {
                    rt = diffSeconds + " Seconds";
                } else {
                    rt = "Crossed";
                }
                Platform.runLater(() -> {
                    updateTime();
                });
                if (diffSeconds <= 0) {
                  Platform.runLater(() -> {
                     root.getChildren().remove(this.plan);
                }); 
                    break;
                }
                try {
                    Thread.sleep(15*1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TreePlan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t.start();
        
    }
    
    String getRemainingTimeString() {
        return rt;
    }
    
}
