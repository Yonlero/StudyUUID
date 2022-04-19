package com.abraaofaher.studyUUID.model.services;

import com.abraaofaher.studyUUID.model.entities.CommonObject;
import com.abraaofaher.studyUUID.model.entities.SellerObject;
import com.abraaofaher.studyUUID.model.entities.TransferInformation;
import com.abraaofaher.studyUUID.model.entities.User;
import com.abraaofaher.studyUUID.model.exceptions.InvalidIdentify;
import com.abraaofaher.studyUUID.model.exceptions.NotFoundException;
import com.abraaofaher.studyUUID.model.repositories.ICommonRepository;
import com.abraaofaher.studyUUID.model.repositories.ISellerRepository;
import com.abraaofaher.studyUUID.model.services.interfaces.IUserDefaultService;
import com.abraaofaher.studyUUID.model.utils.RepositoryManagement;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public record UserServiceImp(ICommonRepository commonRepository,
                             ISellerRepository sellerRepository,
                             RepositoryManagement repositoryManagement) implements IUserDefaultService {
    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        List<UserDTO> allUsersResponseList = new ArrayList<>();
        //Convert Common and Seller to DTO and Add to List
        allUsersResponseList.addAll(commonRepository.findAll(pageable).getContent().stream().map(UserServiceImp::convertCommonToUserDTO).toList());
        allUsersResponseList.addAll(sellerRepository.findAll(pageable).getContent().stream().map(UserServiceImp::convertSellerToUserDTO).toList());
        return new PageImpl<>(allUsersResponseList);
    }

    @Override
    public UserDTO getUserByUniqueIdentify(String identify) {
        return switch (identify.length()) {
            case 11 -> convertCommonToUserDTO(commonRepository.findByCpf(identify).orElseThrow(() -> new NotFoundException("Identify not found")));
            case 14 -> convertSellerToUserDTO(sellerRepository.findByCnpj(identify).orElseThrow(() -> new NotFoundException("Identify not found")));
            default -> throw new InvalidIdentify("Illegal identify");
        };
    }

    @Override
    public UserDTO getUserByEmail(String email, String typeUser) {
        return switch (typeUser) {
            case "common", "Common" -> convertCommonToUserDTO((CommonObject) commonRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email not Found")));
            case "seller", "Seller" -> convertSellerToUserDTO((SellerObject) sellerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email not Found")));
            default -> throw new NotFoundException("User type can't be accepted");
        };
    }

    @Override
    public UserDTO createNewUser(UserDTO user) {
        return convertUserToUserDTO((User) repositoryManagement.getRepositoryBasedOnInstance(convertUserDtoToUser(user)).save(user));
    }

    @Override
    public UserDTO updateAnUser(UserDTO user) {
        User defaultUser = convertUserDtoToUser(this.getUserByEmail(user.getEmail(), user.getClass().getSimpleName()));

        defaultUser.setAmount(user.getAmount());
        defaultUser.setEmail(user.getEmail());
        defaultUser.setName(user.getName());

        if (defaultUser instanceof CommonObject) {
            ((CommonObject) defaultUser).setCpf(user.getCpf());
        } else {
            ((SellerObject) defaultUser).setCnpj(user.getCnpj());
        }

        return convertUserToUserDTO((User) repositoryManagement.getRepositoryBasedOnInstance(defaultUser).save(defaultUser));
    }

    @Override
    public void deleteAnUser(String identify) {
        repositoryManagement.getRepositoryBasedOnIdentifyNumber(identify).deleteById(getUserByUniqueIdentify(identify).getId());
    }

    //region Converters DTO to Child Class
    private static CommonObject convertUserDTOToCommonObject(UserDTO userDTO) {
        return CommonObject.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .cpf(userDTO.getCpf())
                .amount(userDTO.getAmount())
                .email(userDTO.getEmail())
                .build();
    }

    private static SellerObject convertUserDTOToSeller(UserDTO userDTO) {
        return SellerObject.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .cnpj(userDTO.getCnpj())
                .amount(userDTO.getAmount())
                .email(userDTO.getEmail())
                .build();
    }
    //endregion

    //region Converter User to CommonObject or Seller
    private static CommonObject convertUserToCommonObject(User user) {
        return CommonObject.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .cpf(((CommonObject) user).getCpf())
                .amount(user.getAmount())
                .build();
    }

    private static SellerObject convertUserToSeller(User user) {
        return SellerObject.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .cnpj(((SellerObject) user).getCnpj())
                .amount(user.getAmount())
                .build();
    }

    private static UserDTO convertCommonToUserDTO(CommonObject commonObject) {
        return UserDTO.builder()
                .id(commonObject.getId())
                .name(commonObject.getName())
                .email(commonObject.getEmail())
                .cpf(commonObject.getCpf())
                .amount(commonObject.getAmount())
                .build();
    }

    private static UserDTO convertSellerToUserDTO(SellerObject sellerObject) {
        return UserDTO.builder()
                .id(sellerObject.getId())
                .name(sellerObject.getName())
                .email(sellerObject.getEmail())
                .cnpj(sellerObject.getCnpj())
                .amount(sellerObject.getAmount())
                .build();
    }
    //endregion

    //region Convert User To DTO or Dto to User
    private static UserDTO convertUserToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .amount(user.getAmount())
                .cpf(((CommonObject) user).getCpf())
                .cnpj(((SellerObject) user).getCnpj())
                .created_at(user.getCreated_at())
                .updated_at(user.getUpdated_at())
                .build();
    }

    private static User convertUserDtoToUser(UserDTO userDto) {
        if (Objects.nonNull(userDto.getCpf())) {
            return convertUserDTOToCommonObject(userDto);
        } else {
            return convertUserDTOToSeller(userDto);
        }
    }
    //endregion

    //region Nested Class
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class AuthorizationResponse {
        private static RestTemplate restTemplate = new RestTemplate();
        private String message;

        public static AuthorizationResponse getExternalAuthentication() {
            return restTemplate.getForObject("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", AuthorizationResponse.class);
        }
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class UserDTO {
        @EqualsAndHashCode.Include
        private UUID id;
        private String name;
        private BigInteger amount;
        private String cpf;
        private String cnpj;
        private String email;
        private List<TransferInformation> transferHistoric;
        private List<TransferInformation> receiverHistoric;
        @JsonFormat(pattern = "yyyy/mm/dd", shape = JsonFormat.Shape.STRING)
        private LocalDateTime created_at;
        @JsonFormat(pattern = "yyyy/mm/dd", shape = JsonFormat.Shape.STRING)
        private LocalDateTime updated_at;
    }
    //endregion
}