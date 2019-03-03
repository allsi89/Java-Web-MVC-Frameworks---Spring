package softuni.realestateapp.service;

import softuni.realestateapp.domain.models.binding.OfferFindBindingModel;
import softuni.realestateapp.domain.models.service.OfferServiceModel;

import java.util.List;

public interface OfferService {

    void registerOffer(OfferServiceModel offerServiceModel);

    List<OfferServiceModel> findAllOffers();

    void findOffer(OfferFindBindingModel offerFindBindingModel);
}
