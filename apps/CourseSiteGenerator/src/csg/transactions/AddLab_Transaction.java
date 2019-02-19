/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.CSGData;
import csg.data.Lab;
import jtps.jTPS_Transaction;

/**
 *
 * @author sudippaul
 */
public class AddLab_Transaction implements jTPS_Transaction{


        CSGData data;
        Lab lab;

        public AddLab_Transaction(CSGData initData, Lab l) {
            data = initData;
            lab = l;
        }

        @Override
        public void doTransaction() {
            data.addLab(lab);
        }

        @Override
        public void undoTransaction() {
            data.removeLab(lab);
        }

    }

