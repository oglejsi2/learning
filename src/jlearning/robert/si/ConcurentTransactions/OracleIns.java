/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jlearning.robert.si.ConcurentTransactions;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jlearning.robert.si.Jlearning;

/**
 *
 * @author Robert Čmrlec
 * 
 */
public class OracleIns {
    /**
     * Procedure is called by thread. It calls database procedure mydata_ins.
     * which inserts pText into table mydata
     * 
     * @param pText
     * @param args the command line arguments
     * @return 
     * 
     */
    
    public int mydataIns(String pText, Connection pConn) {
        int resultNum = 0;
        try {
            String mySql="{call ?:=CONCURENTTRANSACTIONS.mydata_ins(?)}";                       
            CallableStatement myCall = pConn.prepareCall(mySql);
            myCall.registerOutParameter(1, java.sql.Types.INTEGER);
            myCall.setString(2, "text");           
            myCall.execute();
            
            resultNum = myCall.getInt(1);
            myCall.close();
        } catch (SQLException ex) {
            Logger.getLogger(Jlearning.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultNum;
    }
    
    /**
     * @param pText
     * @param args the command line arguments
     * @return 
     * 
     */    
    public static void main(String[] args) {
        try {
                OracleIns locConcurentOracleTransactions = new OracleIns();
                Connection conn;
                conn=DriverManager.getConnection("jdbc:oracle:thin:@10.10.11.190:1521:advrcp", "robert", "robert");
                conn.setAutoCommit(false);
            
                int mydataIns = locConcurentOracleTransactions.mydataIns("TEST", conn);
                   
                if (mydataIns==1) {
                    System.out.println("OK");
                } else{
                    System.out.println("Error");
                }
                conn.commit();
            } catch (SQLException ex) {
            Logger.getLogger(Jlearning.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
