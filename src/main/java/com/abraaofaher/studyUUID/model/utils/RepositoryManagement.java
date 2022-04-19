package com.abraaofaher.studyUUID.model.utils;

import com.abraaofaher.studyUUID.model.entities.User;
import com.abraaofaher.studyUUID.model.exceptions.InvalidIdentify;
import com.abraaofaher.studyUUID.model.repositories.ICommonRepository;
import com.abraaofaher.studyUUID.model.repositories.IDefaultRepositoryInterface;
import com.abraaofaher.studyUUID.model.repositories.ISellerRepository;
import com.abraaofaher.studyUUID.model.repositories.ITransferRepository;
import org.springframework.stereotype.Component;

@Component
public record RepositoryManagement(ICommonRepository commonRepository, ISellerRepository sellerRepository,
                                   ITransferRepository transferRepository) {
    //region Get Repository based on Instance or Identify Length
    public <T extends IDefaultRepositoryInterface> T getRepositoryBasedOnInstance(User user) {
        return switch (user.getClass().getSimpleName()) {
            case "CommonObject" -> (T) commonRepository;
            case "SellerObject" -> (T) sellerRepository;
            default -> throw new InvalidIdentify("Invalid repository");
        };
    }

    public <T extends IDefaultRepositoryInterface> T getRepositoryBasedOnIdentifyNumber(String identify) {
        return switch (identify.length()) {
            case 11 -> (T) commonRepository;
            case 14 -> (T) sellerRepository;
            default -> throw new InvalidIdentify("Invalid repository");
        };
    }
    //endregion
}