package in.laxmi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.laxmi.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
