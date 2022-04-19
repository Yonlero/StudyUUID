package com.abraaofaher.studyUUID.controllers;

import com.abraaofaher.studyUUID.model.services.UserServiceImp;
import com.abraaofaher.studyUUID.model.services.UserServiceImp.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/study/v1")
public class UserController {
    private final UserServiceImp userService;

    public UserController(UserServiceImp userService) {
        this.userService = userService;
    }

    //region Get Methods
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "5") Integer size) {
        return ResponseEntity.ok(userService.getAllUsers(PageRequest.of(page, size)));
    }

    @RequestMapping(value = "/searchBy", params = "identify", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUserByUniqueIdentify(@Valid @RequestParam String identify) {
        return ResponseEntity.ok(userService.getUserByUniqueIdentify(identify));
    }

    @RequestMapping(value = "/searchBy", params = {"email", "typeUser"}, method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email, @RequestParam(defaultValue = "common", required = false) String typeUser) {
        return ResponseEntity.ok(userService.getUserByEmail(email, typeUser));
    }
    //endregion

    //region Post Methods
    @PostMapping
    public ResponseEntity<UserDTO> createNewUser(@Valid @RequestBody UserDTO body) {
        if (Objects.nonNull(body.getCpf()) && Objects.nonNull(body.getCnpj()))
            throw new RuntimeException("Invalid Body");
        return ResponseEntity.ok(userService.createNewUser(body));
    }
    //endregion

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO body) {
        if (Objects.nonNull(body.getCpf()) && Objects.nonNull(body.getCnpj()))
            throw new RuntimeException("Invalid Body");
        return ResponseEntity.ok(userService.createNewUser(body));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Valid @RequestParam String identify) {
        userService.deleteAnUser(identify);
        return ResponseEntity.noContent().build();
    }

}