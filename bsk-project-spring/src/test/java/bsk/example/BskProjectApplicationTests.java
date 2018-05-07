package bsk.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import bsk.example.services.EmailService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BskProjectApplicationTests {

	@Autowired
	private BCryptPasswordEncoder bCryptPassEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@Test
	public void contextLoads() {

	}

}
