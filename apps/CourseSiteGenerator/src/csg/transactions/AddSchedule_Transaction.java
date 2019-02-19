/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.CSGData;
import csg.data.ScheduleObject;
import jtps.jTPS_Transaction;

/**
 *
 * @author sudippaul
 */
public class AddSchedule_Transaction implements jTPS_Transaction{
    
    CSGData data;
    ScheduleObject object;
    
    public AddSchedule_Transaction(CSGData initData, ScheduleObject initObject){
        data = initData;
        object = initObject;
    }
    
    @Override
    public void doTransaction() {
        data.addScheduleObject(object);
    }

    @Override
    public void undoTransaction() {
        data.removeScheduleObject(object);
    }
    
}
