package com.abraaofaher.studyUUID.model.services.interfaces;

import com.abraaofaher.studyUUID.model.services.UserServiceImp.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserDefaultService {
    //---Default Methods to CRUD---
    Page<UserDTO> getAllUsers(Pageable pageable);

    UserDTO getUserByUniqueIdentify(String identify);

    UserDTO getUserByEmail(String email, String typeUser);

    UserDTO createNewUser(UserDTO user);

    UserDTO updateAnUser(UserDTO user);

    void deleteAnUser(String identify);
}