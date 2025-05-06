package controller;

import model.Staff.Role;
import service.LoginService;

public class LoginController {
	
	private LoginService service;
	
	public LoginController(LoginService service) {
		this.service = service;
	}

	public int login(String email, String password) {
		return service.checkUser(email, password);
	}
	
	public Role getRoleForCurrentLoggedInStaff(int staffId) {
		return service.getRoleByStaffId(staffId);
	}
}
