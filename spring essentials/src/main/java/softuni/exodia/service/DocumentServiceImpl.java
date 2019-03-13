package softuni.exodia.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exodia.domain.entities.Document;
import softuni.exodia.domain.models.binding.DocumentScheduleBindingModel;
import softuni.exodia.domain.models.service.DocumentServiceModel;
import softuni.exodia.repository.DocumentRepository;

import javax.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.exodia.constants.Constants.INVALID_INPUT_DATA;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final Validator validator;
    private final ModelMapper modelMapper;
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(Validator validator, ModelMapper modelMapper, DocumentRepository documentRepository) {
        this.validator = validator;
        this.modelMapper = modelMapper;
        this.documentRepository = documentRepository;
    }

    @Override
    public DocumentServiceModel schedule(DocumentScheduleBindingModel documentScheduleBindingModel) {
        if (this.validator.validate(documentScheduleBindingModel).size() != 0)
            throw new IllegalArgumentException(INVALID_INPUT_DATA);

        Document documentEntity = this.modelMapper.map(
                this.modelMapper.map(
                        documentScheduleBindingModel, DocumentServiceModel.class),
                Document.class);

        try {
            this.documentRepository.saveAndFlush(documentEntity);

            return this.modelMapper.map(documentEntity, DocumentServiceModel.class);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public boolean print(String id) {
        System.out.println();
        try {
            this.documentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public DocumentServiceModel findById(String id) {
        Document document = this.documentRepository.findById(id).orElse(null);
        if (document != null) {
            return this.modelMapper.map(document, DocumentServiceModel.class);
        }
        return null;
    }

    @Override
    public List<DocumentServiceModel> getAllDocuments() {
        return this.documentRepository.findAll()
                .stream()
                .map(d->
                        this.modelMapper.map(d, DocumentServiceModel.class))
                .collect(Collectors.toList());
    }
}
