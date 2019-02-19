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
public class RemoveSchedule_Transaction implements jTPS_Transaction{
    
    CSGData data;
    ScheduleObject object;
    
    public RemoveSchedule_Transaction(CSGData initData, ScheduleObject initObject){
        data = initData;
        object = initObject;
    }
    
    @Override
    public void doTransaction() {
        data.removeScheduleObject(object);
    }

    @Override
    public void undoTransaction() {
        data.addScheduleObject(object);
    }
}
