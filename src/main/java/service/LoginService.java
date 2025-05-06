package service;

import model.Staff.Role;
import repository.StaffRepository;

public class LoginService {

	private StaffRepository staffRepo;
	
	public LoginService(StaffRepository staffRepo) {
		this.staffRepo = staffRepo;
	}
	
	public int checkUser(String email, String password) {
		return staffRepo.findStaffIdByEmailandPassword(email, password);
	}

	public Role getRoleByStaffId(int staffId) {
		return staffRepo.findRoleByStaffId(staffId);
	}
}
