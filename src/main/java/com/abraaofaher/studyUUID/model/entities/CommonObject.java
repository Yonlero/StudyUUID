package com.abraaofaher.studyUUID.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "common_tb")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public final class CommonObject extends User {
    @NotNull(message = "The field 'cpf' shouldn't be null")
    @Column(unique = true, nullable = false)
    private String cpf;
    @JsonIgnore
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "paying")
    private List<TransferInformation> transferHistoric;
    @JsonIgnore
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "receiver")
    private List<TransferInformation> receiverHistoric;
}