/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CSGData;
import csg.data.Lecture;

/**
 *
 * @author sudippaul
 */
public class AddLecture_Transaction implements jTPS_Transaction{
    
    CSGData data;
    Lecture lecture;
    
public AddLecture_Transaction(CSGData initData, Lecture l){
    data = initData;
    lecture = l;
}
    
@Override
    public void doTransaction() {
        data.addLecture(lecture);
    }

    @Override
    public void undoTransaction() {
        data.removeLecture(lecture);
    }
}
