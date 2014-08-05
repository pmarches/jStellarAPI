//package jstellarapi.cli;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import jstellarapi.core.StellarSeedAddress;
//
//import org.jboss.jreadline.complete.CompleteOperation;
//import org.jboss.jreadline.complete.Completion;
//import org.jboss.jreadline.console.Console;
//import org.jboss.jreadline.console.settings.Settings;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//
///**
// * Sample session
// * > open ../wallet.dat
// * password:
// * > lsaddr
// * rEQQNvhuLt1KTYmDWmw12mPvmJD4KCtxmS
// * rGwUWgN5BEg3QGNY3RX2HfYowjUTZdid3E
// * > mkpayment r32fLio1qkmYqFFYkwdnsaVN7cxBwkW4cT 100STR
// * payment1
// * > show payment1
// * {json of transactions goes here}
// * > hex payment1
// * HEX ENCODING OF TX GOES HERE
// * > sign payment1
// * HEX SIGNATURE GOES HERE
// * > submit payment1
// * > balance
// * BTC 100
// * USD 8
// * STR 10000
// * > mkaddr
// * rF3r4rgsDeFVSDFsjw023ksdpsmwofrk2
// * 
// * @author pmarches
// */
//public class JStellarCliMain {
//	public static void main(String[] args) throws Exception {
//		JStellarCliMain main = new JStellarCliMain();
//		main.execute();
//	}
//
//	static List<String> topLevelCommands = new ArrayList<String>();
//	static {
//		topLevelCommands.add("hex");
//		topLevelCommands.add("lsaddr");
//		topLevelCommands.add("lshistory");
//		topLevelCommands.add("lsobj");
//		topLevelCommands.add("mkaddr");
//		topLevelCommands.add("mkpayment");
//		topLevelCommands.add("open");
//		topLevelCommands.add("show");
//		topLevelCommands.add("sign");
//	}
//	
//	protected List<String> getToplevelCommandCandidates(String commandPrefix){
//		ArrayList<String> matches = new ArrayList<String>();
//		for(String cmd : topLevelCommands){
//			if(cmd.startsWith(commandPrefix)){
//				matches.add(cmd);
//			}
//		}
//		return matches;
//	}
//
//	Console console;
//	Completion completer = new Completion() {
//		@Override
//		public void complete(CompleteOperation co) {
//			if(co.getBuffer().contains(" ")==false){
//				List<String> commandCandidates = getToplevelCommandCandidates(co.getBuffer());
//				co.setCompletionCandidates(commandCandidates);
//			}
//			else {
//				List<String> commandCandidates = getArgumentCandidates(co.getBuffer());
//				co.setCompletionCandidates(commandCandidates);
//			}
//			
//		}
//
//		private List<String> getArgumentCandidates(String command) {
//			String[] commandAndArguments = command.split(" +");
//			if(commandAndArguments[0].startsWith("open")){
//				if(commandAndArguments.length>1){
//					File possibleFileOrDir = new File(commandAndArguments[1]);
//					if(possibleFileOrDir.exists()){
//						return Arrays.asList(possibleFileOrDir.list());
//					}
//					final String filename = possibleFileOrDir.getName();
//					possibleFileOrDir = possibleFileOrDir.getParentFile();
////					console.pushToConsole("filename="+filename+" possibleFileOrDir="+possibleFileOrDir);
//					if(possibleFileOrDir==null){
//						possibleFileOrDir = new File(".");
//					}
//					return Arrays.asList(possibleFileOrDir.list(new FilenameFilter() {
//						@Override
//						public boolean accept(File arg0, String arg1) {
//							return arg1.startsWith(filename);
//						}
//					}));
//				}
//				return Arrays.asList(new File(".").list());
//			}
//			return null;
//		}
//	};
//	private StellarSeedAddress stellarAccount;
//
//	public JStellarCliMain() throws Exception {
//		// Settings.getInstance().setAnsiConsole(false);
//		Settings.getInstance().setReadInputrc(false);
//		console = new Console();
//		PrintWriter out = new PrintWriter(System.out);
//		console.addCompletion(completer);
//		
//		JSONObject jsonWallet = (JSONObject) new JSONParser().parse(new FileReader("jStellarapi-wallet.json"));
//		String seedStr = (String)jsonWallet.get("master_seed");
//		stellarAccount = new StellarSeedAddress(seedStr);
//	}
//
//	private void execute() throws IOException {
//		try {
//			String line;
//			while ((line = console.read("> ")) != null) {
////				if (line.equalsIgnoreCase("password")) {
////					line = console.read("password: ", Character.valueOf((char) 0));
////					console.pushToConsole("password typed:" + line + "\n");
////				}
//				line = line.trim();
//				//Maybe we should parse the command line here?
//				if (line.equalsIgnoreCase("lsaddr")) {
//					console.pushToConsole(this.stellarAccount.getPublicStellarAddress().toString()+"\n");
//				}
//				else if (line.equalsIgnoreCase("mkpayment")) {
//					executeMkPayment(line);
//				}
//				else if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
//					break;
//				}
//				
//			}
//		} finally {
//			console.stop();
//		}
//	}
//
//	private void executeMkPayment(String line) {
//		
//	}
//
//}
