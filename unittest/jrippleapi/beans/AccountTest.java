package jrippleapi.beans;

import static org.junit.Assert.*;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

public class AccountTest {

	@Test
	public void testAccount() throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject jsonAccountObject=(JSONObject) parser.parse(new FileReader("testAccount.txt"));
		Account jRippleAPIAccount=new Account(jsonAccountObject);
		assertEquals("r32fLio1qkmYqFFYkwdnsaVN7cxBwkW4cT", jRippleAPIAccount.account);
		assertNotNull(jRippleAPIAccount.secret);
	}

}
