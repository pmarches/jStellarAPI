package jrippleapi.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

public class AccountTest {

	@Test
	public void testAccount() throws Exception {
		Account jRippleAPIAccount = getTestAccount();
		assertEquals("r32fLio1qkmYqFFYkwdnsaVN7cxBwkW4cT", jRippleAPIAccount.account);
		assertNotNull(jRippleAPIAccount.secret);
	}

	public static Account getTestAccount() throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject jsonAccountObject=(JSONObject) parser.parse(new FileReader("testAccount.txt"));
		Account jRippleAPIAccount=new Account(jsonAccountObject);
		return jRippleAPIAccount;
	}

}
