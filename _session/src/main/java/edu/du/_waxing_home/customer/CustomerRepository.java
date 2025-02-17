package edu.du._waxing_home.customer;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 전화번호로 환자 조회
    Customer findByPhonenumber(String phonumber);

    // 전화번호로 환자 검색 (페이징 처리)
    Page<Customer> findByPhonenumberContaining(String phonenumber, Pageable pageable);



    // 이름으로 환자 검색 (페이징 처리)
    Page<Customer> findByNameContaining(String name, Pageable pageable);

    // 환자 번호로 검색 (페이징 처리)
    Page<Customer> findById(Long id, Pageable pageable);


}