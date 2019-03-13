package softuni.exodia.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import softuni.exodia.domain.models.binding.UserLoginBindingModel;
import softuni.exodia.domain.models.binding.UserRegisterBindingModel;
import softuni.exodia.domain.models.service.UserServiceModel;
import softuni.exodia.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Validator;

import static softuni.exodia.constants.Constants.INVALID_INPUT_DATA;

@Controller
public class UserController {
    private static final String PWD_NOT_MATCH = "Passwords don't match!";
    private static final String FAILED_REG = "User registration failed!";
    private static final String FAILED_LOGIN = "User login failed!";

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public ModelAndView register(ModelAndView modelAndView, HttpSession session) {
        if (session.getAttribute("userName") != null){
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("register");
        }
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerConfirm(@ModelAttribute UserRegisterBindingModel model, ModelAndView modelAndView){
        if (!model.getPassword().equals(model.getConfirmPassword()))
            throw new IllegalArgumentException(PWD_NOT_MATCH);

        if (!this.userService.register(model))
            throw new IllegalArgumentException(FAILED_REG);

        modelAndView.setViewName("redirect:/login");

        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView, HttpSession session) {
        if (session.getAttribute("userName") != null){
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("login");
        }

        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView loginConfirm(@ModelAttribute UserLoginBindingModel userLoginBindingModel, ModelAndView modelAndView, HttpSession session) {
        UserServiceModel userServiceModel = this.userService.login(userLoginBindingModel);

        if (userServiceModel == null)
            throw new IllegalArgumentException(FAILED_LOGIN);

        session.setAttribute("userId", userServiceModel.getId());
        session.setAttribute("userName", userServiceModel.getUsername());

        modelAndView.setViewName("redirect:/home");

        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logout(ModelAndView modelAndView, HttpSession session) {
        if (session.getAttribute("userName") == null){
            modelAndView.setViewName("redirect:/login");
        } else {
            session.invalidate();
            modelAndView.setViewName("redirect:/");
        }

        return modelAndView;
    }
}
