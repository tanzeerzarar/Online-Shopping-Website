package com.y4j.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="roles", discriminatorType = DiscriminatorType.STRING)
public class User 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@NotBlank(message = "Name can't be blank")
	@Size(min=3, max=20, message="name should be between 3-20 characters long")
	private String name;
	
	//@Size(min=4, max=20, message="password should be between 4-20 characters long")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
			message="password must contain atleast 1 digit, 1 small letter, 1 capital letter, 1 special character @#$%^&-+=()")
	private String password;
	
	String role;
	
	private String imageURL;
	
	@Column(unique=true)
	@Email
	@Pattern(regexp="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message="Invalid Email")
	private String email;
	
	@Pattern(regexp="^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$", message="Phone number format should be: 123-456-7890\r\n, "
			+ "(123) 456-7890\r\n, "
			+ "123 456 7890\r\n, "
			+ "1234567890\r\n, "
			+ "123.456.7890\r\n, "
			+ "+91 (123) 456-7890, ")
	private String phone;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate dob;
	
	

	public String getRole() {
		return role;
	}
	public void setRole(String roles) {
		this.role = roles;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", role=" + role + ", imageURL="
				+ imageURL + ", email=" + email + ", phone=" + phone + ", dob=" + dob + "]";
	}


}
