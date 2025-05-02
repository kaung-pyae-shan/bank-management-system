package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Customer {

	private int id;
	private String name;
	private LocalDate dob;
	private String nrc;
	private String email;
	private String phone;
	private String address;
	private LocalDateTime createdAt;
	private Staff staff;
	
	public Customer() {
	}

	public Customer(int id, String name, LocalDate dob, String nrc, String email, String phone, String address,
			LocalDateTime createdAt, Staff staff) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.nrc = nrc;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.createdAt = createdAt;
		this.staff = staff;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	
	public String getNrc() {
		return nrc;
	}
	
	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Object[] toObject() {
		Object[] obj = {this.name, this.dob, this.nrc, this.email, this.phone, this.address, this.createdAt, this.staff, this.id};
		return obj;
	}
}
