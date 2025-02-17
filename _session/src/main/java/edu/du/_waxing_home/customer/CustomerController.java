package edu.du._waxing_home.customer;

import edu.du._waxing_home.reservation.Reservation;
import edu.du._waxing_home.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ReservationService reservationService;  // 예약 서비스를 가져오기 위한 서비스

    @Autowired
    private CustomerService customerService;

    // 환자 목록 조회
    @GetMapping("/list")
    public String getAllCustomers(@RequestParam(value = "search", required = false) String search,
                                 @RequestParam(value = "searchType", required = false) String searchType,
                                 @RequestParam(value = "reset", required = false) String reset,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                 Model model) {

        Pageable pageable = PageRequest.of(page, size);  // 페이지 번호와 크기 설정
        Page<Customer> customerPage;

        // 전체 목록 보기 버튼 클릭 시
        if ("true".equals(reset)) {
            customerPage = customerService.getCustomers(pageable);  // 전체 환자 목록 반환
        } else if (search != null && !search.isEmpty() && searchType != null) {
            // 검색 조건이 있을 때, CustomerService에서 검색 처리
            customerPage = customerService.searchCustomers(search, searchType, pageable);
        } else {
            // 검색 조건이 없으면 모든 환자 목록 반환
            customerPage = customerService.getCustomers(pageable);
        }

        model.addAttribute("customers", customerPage.getContent());  // 현재 페이지의 환자 목록
        model.addAttribute("currentPage", page);  // 현재 페이지
        model.addAttribute("totalPages", customerPage.getTotalPages());  // 전체 페이지 수
        model.addAttribute("totalItems", customerPage.getTotalElements());  // 전체 환자 개수
        model.addAttribute("search", search);  // 검색어 유지
        model.addAttribute("searchType", searchType);  // 검색 조건 유지

        return "pages/admin/customer/customerlist";  // 환자 목록을 표시할 페이지
    }

    // 환자 추가 요청 처리
    @PostMapping("/add")
    public ResponseEntity<?> addCustomer(@RequestParam String name,
                                        @RequestParam String phonenumber,
                                        @RequestParam String email,
                                        @RequestParam String gender) {
        Customer customer = Customer.builder()
                .name(name)
                .phonenumber(phonenumber)
                .email(email)
                .gender(gender)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();

        customerService.saveCustomer(customer);  // 환자 정보 저장
        return ResponseEntity.ok().body("{\"success\": true}");  // 성공 응답 반환
    }

    // 환자 정보 수정 요청 처리
    @PostMapping("/update")
    public String updateCustomer(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam String phonenumber,
                                @RequestParam String email,
                                @RequestParam String gender) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            customer.setName(name);
            customer.setPhonenumber(phonenumber);
            customer.setEmail(email);
            customer.setGender(gender);
            customer.setUpdated_at(LocalDateTime.now());  // 수정일자 갱신

            customerService.saveCustomer(customer);  // 수정된 환자 정보 저장
        }
        return "redirect:/customer/list";  // 환자 목록 페이지로 리다이렉트
    }

    // 환자 정보 수정 화면 요청 처리
    @GetMapping("/{id}/edit")
    public String editCustomer(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            List<Reservation> reservations = reservationService.getReservationsByCustomerId(id);  // 예약 정보 가져오기
            model.addAttribute("customer", customer);  // 환자 정보 모델에 추가
            model.addAttribute("reservations", reservations);  // 예약 정보 모델에 추가
        }
        return "pages/customeredit";  // 환자 수정 페이지로 이동
    }

}