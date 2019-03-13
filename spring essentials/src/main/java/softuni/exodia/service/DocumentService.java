package softuni.exodia.service;

import softuni.exodia.domain.models.binding.DocumentScheduleBindingModel;
import softuni.exodia.domain.models.service.DocumentServiceModel;

import java.util.List;

public interface DocumentService {

    DocumentServiceModel schedule(DocumentScheduleBindingModel documentScheduleBindingModel);

    public boolean print(String id);

    DocumentServiceModel findById(String id);

    List<DocumentServiceModel> getAllDocuments();
}
