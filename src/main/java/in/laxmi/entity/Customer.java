package in.laxmi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_info")
public class Customer {
	@Id
	@Column(name = "customerId")
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private String contactNo;
	private String county;
	private String dob;
}
