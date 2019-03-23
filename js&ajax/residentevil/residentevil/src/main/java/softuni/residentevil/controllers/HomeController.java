package softuni.residentevil.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {
  @GetMapping("/")
  public ModelAndView home() {
    return super.view("index");
  }

  @GetMapping("/error")
  public ModelAndView error(){
    return super.view("error");
  }
}
