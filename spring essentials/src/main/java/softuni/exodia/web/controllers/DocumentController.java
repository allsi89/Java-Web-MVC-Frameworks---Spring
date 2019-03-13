package softuni.exodia.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import softuni.exodia.domain.models.binding.DocumentScheduleBindingModel;
import softuni.exodia.domain.models.service.DocumentServiceModel;
import softuni.exodia.domain.models.view.DocumentDetailsViewModel;
import softuni.exodia.service.DocumentService;

import javax.servlet.http.HttpSession;

@Controller
public class DocumentController {

    private static final String FAILED_CREATION = "Document creation failed.";
    private static final String NOT_FOUND = "Document not found!";
    private static final String SMTH_WRG = "Something went wrong";

    private final DocumentService documentService;
    private final ModelMapper modelMapper;

    @Autowired
    public DocumentController(DocumentService documentService, ModelMapper modelMapper) {
        this.documentService = documentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/schedule")
    public ModelAndView schedule(ModelAndView modelAndView, HttpSession session) {

        if (session.getAttribute("userName") == null) {
            modelAndView.setViewName("redirect:/login");
        } else {
            modelAndView.setViewName("schedule");
        }

        return modelAndView;
    }

    @PostMapping("/schedule")
    public ModelAndView scheduleConfirm(@ModelAttribute DocumentScheduleBindingModel model,
                                        ModelAndView modelAndView,
                                        HttpSession session) {

        DocumentServiceModel documentServiceModel = this.documentService.schedule(model);

        if (documentServiceModel == null)
            throw new IllegalArgumentException(FAILED_CREATION);

        modelAndView.setViewName("redirect:/details/" + documentServiceModel.getId());

        return modelAndView;
    }

    @GetMapping("/details/{documentId}")
    public ModelAndView details(@PathVariable("documentId") String documentId, ModelAndView modelAndView, HttpSession session) {

        if (session.getAttribute("userName") == null) {
            modelAndView.setViewName("redirect:/login");
        } else {
            DocumentServiceModel documentServiceModel = this.documentService.findById(documentId);

            if (documentServiceModel == null) {
                throw new IllegalArgumentException(NOT_FOUND);
            }

            modelAndView.setViewName("details");
            modelAndView.addObject("document", this.modelMapper
                    .map(documentServiceModel, DocumentDetailsViewModel.class));
        }

        return modelAndView;
    }

    @GetMapping("/print/{documentId}")
    public ModelAndView print(@PathVariable("documentId") String documentId, ModelAndView modelAndView, HttpSession session) {
        if (session.getAttribute("userName") == null) {
            modelAndView.setViewName("redirect:/login");
        } else {
            DocumentServiceModel documentServiceModel = this.documentService.findById(documentId);

            if (documentServiceModel == null) {
                throw new IllegalArgumentException(NOT_FOUND);
            }

            modelAndView.setViewName("print");
            modelAndView.addObject("document", this.modelMapper
                    .map(documentServiceModel, DocumentDetailsViewModel.class));
        }

        return modelAndView;
    }

    @PostMapping("/print/{documentId}")
    public ModelAndView printConfirm(@PathVariable("documentId") String documentId, ModelAndView modelAndView) {

        if (!this.documentService.print(documentId)){
            throw new IllegalArgumentException(SMTH_WRG);
        }

        modelAndView.setViewName("redirect:/home");

        return modelAndView;
    }


}
