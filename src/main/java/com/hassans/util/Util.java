package com.hassans.util;


import org.apache.logging.log4j.Logger;

import com.github.javafaker.Faker;


import org.apache.logging.log4j.LogManager;


public class Util {

	public static Logger logger = LogManager.getLogger(Util.class.getName());


	
	/** The faker for fake data generation*/
    private static Faker faker = new Faker();

   
    
	public static int S_WAIT = 1;
	public static int M_WAIT = 20;
	public static int L_WAIT = 5;

    public static void sleepInSec(int timeInSecond, String info) {
        // beware: this takes time in seconds
        if (info != null) {
            logger.info("sleeping for: " + timeInSecond + ",info: " + info);
        }

        try {
            Thread.sleep(timeInSecond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	
	public static void sleepInSec(int timeInSecond) {
	    sleepInSec(timeInSecond, null);
	}
	   

    public static String getRandomNumber(int len) {
        final String NUMERIC = "0123456789";  
        StringBuffer sb = new StringBuffer(len);  
        for (int i=0;  i<len;  i++) {  
            int ndx = (int)(Math.random()*NUMERIC.length());  
            sb.append(NUMERIC.charAt(ndx));  
        }  
        return sb.toString();
    }
}
