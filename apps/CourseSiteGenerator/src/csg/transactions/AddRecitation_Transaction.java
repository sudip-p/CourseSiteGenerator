/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.CSGData;
import csg.data.Recitation;
import jtps.jTPS_Transaction;

/**
 *
 * @author sudippaul
 */
public class AddRecitation_Transaction implements jTPS_Transaction{
    
    CSGData data;
    Recitation recitation;
    
public AddRecitation_Transaction(CSGData initData, Recitation r){
    data = initData;
    recitation = r;
}
    
@Override
    public void doTransaction() {
        data.addRecitation(recitation);
    }

    @Override
    public void undoTransaction() {
        data.removeRecitation(recitation);
    }
    
}
