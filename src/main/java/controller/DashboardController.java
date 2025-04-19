package controller;

import model.dto.AdminDashboardStats;
import service.DashboardService;

public class DashboardController {

	private DashboardService service;
	
	public DashboardController() {
		service = new DashboardService();
	}
	
	public AdminDashboardStats fetchAdminDashboardStats() {
		return service.getAdminDashboardStats();
	}
}
