package edu.du._waxing_home.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String main() {
        // SecurityContext에서 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            System.out.println("Authenticated user: " + authentication.getName());
        } else {
            System.out.println("No authentication found.");
        }

        return "pages/main";
    }

//    @GetMapping("/")
//    public String main(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("Authenticated user: " + authentication.getName());
//        model.addAttribute("username", authentication.getName());
//        if (authentication != null && authentication.isAuthenticated()) {
//            model.addAttribute("username", authentication.getName());
//        } else {
//            model.addAttribute("username", "인증되지 않음");
//        }
//        return "pages/main";  // home.html 템플릿을 반환
//    }

    //People Menu
    @GetMapping("/peoplewaxing")
    public String peoplewaxing() { return "pages/people/peoplewaxing";}

    @GetMapping("/directions")
    public String directions() { return "pages/people/directions";}

    //Service Menu
    @GetMapping("/bodywaxing")
    public String bodywaxing() { return "pages/service/bodywaxing";}
    @GetMapping("/brazlianwaxing")
    public String brazlian() { return "pages/service/brazlianwaxing";}
    @GetMapping("/facewaxing")
    public String facewaxing() { return "pages/service/facewaxing";}
    @GetMapping("/pregnantwaxing")
    public String pregnant() { return "pages/service/pregnantwaxing";}

    //Community Menu
    @GetMapping("/event")
    public String event() { return "pages/community/event";}
    @GetMapping("/news")
    public String news() { return "pages/community/news";}
    @GetMapping("/review")
    public String review() { return "pages/community/review";}

    //Reservation Menu
    @GetMapping("/reservaion")
    public String reservation() { return "pages/reservation/reservation";}
    @GetMapping("/counseling")
    public String counseling() { return "pages/reservation/counseling";}

    //Cost Menu
    @GetMapping("/cost")
    public String cost() { return "pages/cost/cost";}

    //BestReview Menu
    @GetMapping("/bestreview")
    public String bestreview() { return "pages/bestreview/bestreview";}

    //Manager Menu
    @GetMapping("/customerlist")
    public String manger() { return "pages/admin/customer/customerlist";}
    @GetMapping("/reservationlist")
    public String reservationlist() { return "pages/admin/reservation/reservationlist";}

    //Write form
    @GetMapping("/writenews")
    public String writenews() { return "pages/write/writenews";}
    @GetMapping("/writereview")
    public String writereview() { return "pages/write/writereview";}
    @GetMapping("/writeevent")
    public String writeevent() { return "pages/write/writeevent";}





}
