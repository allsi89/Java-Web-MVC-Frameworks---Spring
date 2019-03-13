package softuni.exodia.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exodia.domain.entities.User;
import softuni.exodia.domain.models.binding.UserLoginBindingModel;
import softuni.exodia.domain.models.binding.UserRegisterBindingModel;
import softuni.exodia.domain.models.service.UserServiceModel;
import softuni.exodia.repository.UserRepository;

import javax.validation.Validator;

import static softuni.exodia.constants.Constants.INVALID_INPUT_DATA;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, Validator validator) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @Override
    public boolean register(UserRegisterBindingModel userRegisterBindingModel) {

        if (this.validator.validate(userRegisterBindingModel).size() != 0)
            throw new IllegalArgumentException(INVALID_INPUT_DATA);

        UserServiceModel userServiceModel = this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class);
        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));

        try {
            this.userRepository.saveAndFlush(user);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public UserServiceModel login(UserLoginBindingModel userLoginBindingModel) {
        return this.userRepository.findByUsername(userLoginBindingModel.getUsername())
                .filter(u->u.getPassword().equals(DigestUtils.sha256Hex(userLoginBindingModel.getPassword())))
                .map(u->this.modelMapper.map(u, UserServiceModel.class))
                .orElse(null);

//        User user = this.userRepository.findByUsername(userLoginBindingModel.getUsername()).orElse(null);
//        if (user == null || !user.getPassword().equals(DigestUtils.sha256Hex(userLoginBindingModel.getPassword())))
//            return null;
//
//        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel findById(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user != null){
            return this.modelMapper.map(user, UserServiceModel.class);
        }
        return null;
    }

    @Override
    public UserServiceModel findByUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElse(null);
        if (user != null){
            return this.modelMapper.map(user, UserServiceModel.class);
        }
        return null;
    }


}
