package softuni.residentevil.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import softuni.residentevil.domain.models.binding.VirusBindingModel;
import softuni.residentevil.domain.models.service.VirusServiceModel;
import softuni.residentevil.domain.models.view.CapitalListViewModel;
import softuni.residentevil.domain.models.view.VirusListViewModel;
import softuni.residentevil.service.CapitalService;
import softuni.residentevil.service.VirusService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/viruses")
public class VirusController extends BaseController {
  private final CapitalService capitalService;
  private final VirusService virusService;
  private final ModelMapper modelMapper;


  @Autowired
  public VirusController(CapitalService capitalService, VirusService virusService, ModelMapper modelMapper) {
    this.capitalService = capitalService;
    this.virusService = virusService;
    this.modelMapper = modelMapper;
  }


  @GetMapping("/add")
  public ModelAndView add(ModelAndView modelAndView,
                          @ModelAttribute(name = "bindingModel") VirusBindingModel bindingModel) {
    modelAndView.addObject("bindingModel", bindingModel);

    modelAndView.addObject("capitals", getCapitalsList());

    return super.view("add-virus", modelAndView);
  }

  @PostMapping("/add")
  public ModelAndView addConfirm(@Valid @ModelAttribute(name = "bindingModel") VirusBindingModel bindingModel,
                                 BindingResult bindingResult, ModelAndView modelAndView) {
    if (bindingResult.hasErrors()) {
      modelAndView.addObject("bindingModel", bindingModel);
      return super.view("add-virus", modelAndView);
    }

    if (this.virusService.save(bindingModel) == null) {
      return super.view("add-virus", modelAndView);
    }

    return super.redirect("/viruses/show");
  }

  @GetMapping("/edit/{id}")
  public ModelAndView edit(@PathVariable("id") String id,
                           @ModelAttribute(name = "editModel") VirusBindingModel editModel,
                           ModelAndView modelAndView) {

    if (this.virusService.findById(id) == null){
      return super.redirect("/error");
    }

    editModel = this.modelMapper.map(this.virusService.findById(id), VirusBindingModel.class);
    modelAndView.addObject("editModel", editModel);

    modelAndView.addObject("capitals", getCapitalsList());

    return super.view("edit-virus", modelAndView);
  }

  @PostMapping("/edit/{id}")
  public ModelAndView editConfirm(@PathVariable("id") String id,@Valid @ModelAttribute(name = "editModel") VirusBindingModel editModel,
                                  BindingResult bindingResult, ModelAndView modelAndView){
    if (bindingResult.hasErrors()){
      modelAndView.addObject("editModel", editModel);
      modelAndView.addObject("capitals", getCapitalsList());

      return super.view("edit-virus", modelAndView);
    }

    VirusServiceModel virusServiceModel = this.virusService
        .update(editModel);
    if (virusServiceModel == null){
      return super.view("edit-virus", modelAndView);
    }
    return super.redirect("/viruses/show");
  }

  @GetMapping("/delete/{id}")
  public ModelAndView delete(@PathVariable String id){
    this.virusService.deleteById(id);
    return super.redirect("/viruses/show");
  }

  @GetMapping("/show")
  public ModelAndView show(ModelAndView modelAndView, @ModelAttribute(name = "viewModel") VirusListViewModel viewModel){

    modelAndView.addObject("viewModel", viewModel);

    modelAndView.addObject("viruses", this.virusService.findAllViruses()
    .stream()
    .map(v->this.modelMapper.map(v, VirusListViewModel.class))
    .collect(Collectors.toList()));

    return super.view("all-viruses", modelAndView);

  }

  private List<CapitalListViewModel> getCapitalsList(){
    return this.capitalService.findAllCapitals()
        .stream().map(c->
            this.modelMapper.map(c, CapitalListViewModel.class))
        .collect(Collectors.toList());
  }
}
