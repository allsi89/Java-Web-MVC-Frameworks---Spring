package softuni.exodia.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import softuni.exodia.domain.models.view.DocumentHomeViewModel;
import softuni.exodia.service.DocumentService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final DocumentService documentService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(DocumentService documentService, ModelMapper modelMapper) {
        this.documentService = documentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView, HttpSession session) {
        if(session.getAttribute("userName") != null) {
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("index");
        }

        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView home(ModelAndView modelAndView, HttpSession session) {
        if(session.getAttribute("userName") == null) {
            modelAndView.setViewName("redirect:/login");
        } else {
            List<DocumentHomeViewModel> documents = this.documentService.getAllDocuments()
                    .stream()
                    .map(d->this.modelMapper.map(d, DocumentHomeViewModel.class))
                    .collect(Collectors.toList());

            modelAndView.setViewName("home");
            modelAndView.addObject("documents", documents);
        }

        return modelAndView;
    }

}
