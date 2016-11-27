package br.ufes.inf.nemo.ontol.vp;

import java.io.BufferedReader;
import java.io.FileReader;

public class VPLogReader {

	public static void main(String[] args){
		final String logFilePath = args[0];
		final BufferedReader vpLogBuffer;
		
		try{
			vpLogBuffer = new BufferedReader(new FileReader(logFilePath));
			String line;
			while(true){
				line = vpLogBuffer.readLine();
				if(line!=null){
					System.out.println(line);
				} else {
					Thread.sleep(10*1000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
