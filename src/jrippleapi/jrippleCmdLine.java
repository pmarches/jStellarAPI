package jrippleapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class jrippleCmdLine {

	public static void main(String[] args) throws Exception {
		SysoutRippleMessageHandler conn = new SysoutRippleMessageHandler();
		for(int i=0; i<args.length; i++){
			conn.sendString(args[i]);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			String line = reader.readLine();
			if(line==null){
				break;
			}
			conn.sendString(line);
		}
		conn.close();
	}

}
