package bsk.example.services;

import bsk.example.domain.User;

public interface EmailService {
	void sendActivationEmailToUser(User userToActivate);
}
