/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jlearning.neo4j.lib;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.neo4j.driver.internal.util.Extract.fields;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.Value;

import static org.neo4j.driver.v1.Values.parameters;
import org.neo4j.driver.v1.util.Pair;

/**
 *
 * @author Robert
 */
public class GetResult {
    
    private final Driver driver;

       
    public GetResult ( String uri, String user, String password ){
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );        
    }
    
    public void runGetResult(final String initial) {
        Record myRecord = null;
        List<Pair<String, Value>> fields ;
        try ( Session session = driver.session() )
        {    
//            Transaction beginTransaction = session.beginTransaction();
//            beginTransaction.commitAsync();

            StatementResult greeting = session.writeTransaction( new TransactionWork<StatementResult>()
            {
            public StatementResult execute( Transaction tx )
            {
            StatementResult result = tx.run( "MATCH (n:Greeting) WHERE n.name STARTS WITH {x} RETURN n.name as name, n.message as message",
            parameters("x", initial));
            return result;
            }
            } );
            while (greeting.hasNext()) {
                myRecord = greeting.next();
                String asString = myRecord.get("name").asString();
                String asStringMessage = myRecord.get("message").asString();

                
                fields = myRecord.fields();  
                System.out.println("************************************************");
                for (Pair<String, Value>  temp : fields ) {                
                    System.out.println("key value:" + temp.key() + ":" + temp.value());
                }
                System.out.println("************************************************");
            
            }
            


        }        
    }

    public static void main (String[] args) {
        GetResult myGetResult = new GetResult( "bolt://10.10.11.127:7687", "neo4j", "sonja1val" );
        myGetResult.runGetResult( "n" );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GetResult.class.getName()).log(Level.SEVERE, null, ex);
        }
        myGetResult.driver.close();

    }
}
