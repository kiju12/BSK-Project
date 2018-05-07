package bsk.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import bsk.example.domain.ActivationCode;
import bsk.example.domain.User;
import bsk.example.repository.ActivationCodeRepository;
import bsk.example.repository.UserRepository;

@Component
public class EmailServiceImpl implements EmailService {
  
    @Autowired
    private JavaMailSender emailSender;
    
    @Autowired
    private ActivationCodeRepository codeRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPassEncoder;
    
    private static String URL_ROOT = "http://localhost:4200";
    private static String SUBJECT = "Projekt BSK - Link aktywacyjny";
    private static String STRING_TO_ENCODE = "activation";
 
    private void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
    }

	@Override
	public void sendActivationEmailToUser(User userToActivate) {
		String userEmail = userToActivate.getEmail();
		String code = bCryptPassEncoder.encode(STRING_TO_ENCODE);
		ActivationCode activationCode = new ActivationCode(code);
		
		activationCode.setUser(userToActivate);
		activationCode = codeRepo.save(activationCode);
		
		userToActivate.setActivationCode(activationCode);
		userRepo.save(userToActivate);
		
		String mailContent = "Kliknij w poniższy link, aby aktywować konto: \n";
		mailContent += URL_ROOT + "/" + activationCode.getId() + "/" + code;
		sendSimpleMessage(userEmail, SUBJECT, mailContent);
	}
}