package softuni.exodia.service;

import softuni.exodia.domain.models.binding.UserLoginBindingModel;
import softuni.exodia.domain.models.binding.UserRegisterBindingModel;
import softuni.exodia.domain.models.service.UserServiceModel;

public interface UserService {

    boolean register(UserRegisterBindingModel userRegisterBindingModel);

    UserServiceModel login(UserLoginBindingModel userLoginBindingModel);

    UserServiceModel findById(String id);

    UserServiceModel findByUsername(String username);
}
