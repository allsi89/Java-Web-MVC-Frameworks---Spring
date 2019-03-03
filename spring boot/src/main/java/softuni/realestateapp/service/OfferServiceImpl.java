package softuni.realestateapp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.realestateapp.domain.entities.Offer;
import softuni.realestateapp.domain.models.binding.OfferFindBindingModel;
import softuni.realestateapp.domain.models.service.OfferServiceModel;
import softuni.realestateapp.repository.OfferRepository;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;


    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, Validator validator, ModelMapper modelMapper) {
        this.offerRepository = offerRepository;
        this.validator = validator;
        this.modelMapper = modelMapper;
    }

    @Override
    public void registerOffer(OfferServiceModel offerServiceModel) {
        if (this.validator.validate(offerServiceModel).size() != 0) {
            throw new IllegalArgumentException("Something went wrong!");
        }

        this.offerRepository.saveAndFlush(this.modelMapper.map(offerServiceModel, Offer.class));
    }

    @Override
    public List<OfferServiceModel> findAllOffers() {
        return this.offerRepository.findAll()
                .stream()
                .map(o ->
                        this.modelMapper.map(o, OfferServiceModel.class)
                )
                .collect(Collectors.toList());
    }

    @Override
    public void findOffer(OfferFindBindingModel offerFindBindingModel) {
        if (this.validator.validate(offerFindBindingModel).size() != 0) {
            throw new IllegalArgumentException("Something went wrong!");
        }

        Offer offer = this.offerRepository.findAll()
                .stream()
                .filter(o ->
                        o.getApartmentType().equalsIgnoreCase(offerFindBindingModel.getFamilyApartmentType())
                                &&
                                offerFindBindingModel.getFamilyBudget().compareTo(
                                        o.getApartmentRent().add(
                                                o.getAgencyCommission()
                                                        .divide(new BigDecimal("100"))
                                                        .multiply(o.getApartmentRent())
                                        )
                                ) >= 0)
                .findFirst()
                .orElse(null);

        if (offer == null) {
            throw new IllegalArgumentException("Something went wrong!");
        }

        this.offerRepository.delete(offer);

    }
}
