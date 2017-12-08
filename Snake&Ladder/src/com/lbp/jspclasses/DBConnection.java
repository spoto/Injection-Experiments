package com.lbp.jspclasses;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


public class DBConnection {

	public DBConnection() {
		// TODO Auto-generated constructor stub
	}
	public Connection getConnection(){
		try{
			//Properties  prop=new Properties();
			//prop.load(new FileInputStream("databaseConfig.properties"));
			//String db=prop.getProperty("database");
			//String dbuser=prop.getProperty("dbuser");
			//String dbpass=prop.getProperty("dbpassword");		
			
			Connection con=null;
			Statement st=null;
			ResultSet rs=null;
		
		
			Class.forName("com.mysql.jdbc.Driver").newInstance ();		
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/snakes_ladders","system","tiger");
			return con;		
		}
		catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	public void closeConnection(Connection con){
		if(con!=null){
			try{
				con.close();
			}
			catch(Exception ex){
				
			}
		}
	}
	
	public String trustMe(int t, String s){
		String cleaner = s;
		if(t == 0){
			// XSS
			cleaner = cleaner.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
			cleaner = cleaner.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
			cleaner = cleaner.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");
		}else if(t == 1){
			// PATH
			File dPath = new File(cleaner);
			if (dPath.isAbsolute()) throw new RuntimeException("Error");
			String dPathC;
			String dPathA;
			try {
				dPathC = dPath.getCanonicalPath();
				dPathA = dPath.getAbsolutePath();
			} catch (IOException e) {
				throw new RuntimeException("Error");
			}
			if (!dPathC.equals(dPathA)) throw new RuntimeException("Directory traversal attempt?");
		}else if(t == 2){
			// SQL
			cleaner = cleaner.replaceAll("\"" , "\\");
            cleaner = cleaner.replaceAll("\n","\n");
            cleaner = cleaner.replaceAll("\r", "\r");
            cleaner = cleaner.replaceAll("\t", "\t");
            cleaner = cleaner.replaceAll("\00", "\0");
            cleaner = cleaner.replaceAll("'", "\'");
		}
		return cleaner;
	}

}
