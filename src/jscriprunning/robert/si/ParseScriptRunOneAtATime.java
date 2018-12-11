package jscriprunning.robert.si;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import oracle.dbtools.raptor.newscriptrunner.ISQLCommand;
import oracle.dbtools.raptor.newscriptrunner.ScriptParser;
import oracle.dbtools.raptor.newscriptrunner.ScriptRunner;
import oracle.dbtools.raptor.newscriptrunner.ScriptRunnerContext;

public class ParseScriptRunOneAtATime {
	  public static void main(String[] args) throws SQLException, IOException {
		    Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.10.11.103:1521:MATADOR", "ADVDBA", "isystem2002");
		    conn.setAutoCommit(false);
///Users/klrice/workspace_commons/sqlcl-java/
		    FileInputStream fin = new FileInputStream(new File("C:\\Users\\Robert\\Documents\\NetBeansProjects\\Jlearning\\src\\jscriprunning\\robert\\si\\myfile.sql"));
		    ScriptParser parser = new ScriptParser(fin);
		    
		    ISQLCommand cmd;
		    // #setup the context
		    ScriptRunnerContext ctx = new ScriptRunnerContext();
		    ctx.setBaseConnection(conn);

		    
		    // Capture the results without this it goes to STDOUT
		    ByteArrayOutputStream bout = new ByteArrayOutputStream();
		    BufferedOutputStream buf = new BufferedOutputStream(bout);

		    
			ScriptRunner sr = new ScriptRunner(conn, buf, ctx);		    
			while ( (  cmd = parser.next() ) != null ) {
				// do something fancy based on a cmd
				sr.run(cmd);
				// check success/failure of the command

				String errMsg = (String) ctx.getProperty(ctx.ERR_MESSAGE);
				if ( errMsg != null   ){
					// react to a failure
					System.out.println("**FAILURE**" + errMsg);
				}				
		    }
			
		    String results = bout.toString("UTF8");
		    results = results.replaceAll(" force_print\n", "");
		    System.out.println(results);

	  }

}