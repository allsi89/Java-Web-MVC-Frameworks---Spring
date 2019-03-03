package softuni.realestateapp.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import softuni.realestateapp.domain.models.binding.OfferFindBindingModel;
import softuni.realestateapp.domain.models.binding.OfferRegisterBindingModel;
import softuni.realestateapp.domain.models.service.OfferServiceModel;
import softuni.realestateapp.service.OfferService;

@Controller
public class OfferController {
    private final OfferService offerService;
    private final ModelMapper modelMapper;

    public OfferController(OfferService offerService, ModelMapper modelMapper) {
        this.offerService = offerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/reg")
    public String register(){
        return "register.html";
    }

    @PostMapping("/reg")
    public String registerConfirm(@ModelAttribute(name = "model") OfferRegisterBindingModel model) {

        try {
            this.offerService.registerOffer(this.modelMapper.map(model, OfferServiceModel.class));
        } catch (IllegalArgumentException e){
            e.printStackTrace();

            return "redirect:/reg";
        }

        return "redirect:/";
    }

    @GetMapping("/find")
    public String find(){
        return "find.html";
    }

    @PostMapping("/find")
    public String findConfirm(@ModelAttribute(name = "model") OfferFindBindingModel model) {

        try {
            this.offerService.findOffer(model);
        } catch (IllegalArgumentException e){
            e.printStackTrace();

            return "redirect:/find";
        }

        return "redirect:/";
    }


}
