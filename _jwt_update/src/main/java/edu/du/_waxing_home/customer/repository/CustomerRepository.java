package edu.du._waxing_home.customer.repository;

import edu.du._waxing_home.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByPhoneAndName(String phone, String name);

}
