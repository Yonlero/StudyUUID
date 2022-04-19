package com.abraaofaher.studyUUID.model.entities;

import com.abraaofaher.studyUUID.model.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transferInformation_tb")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferInformation {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid3")
    private UUID id;
    @NotNull
    private BigInteger amount;
    @ManyToOne
    @JoinColumn(name = "paying.id")
    @JsonIgnoreProperties({"id", "created_at", "updated_at"})
    private CommonObject paying;
    @ManyToOne
    @JsonIgnoreProperties({"id", "created_at", "updated_at"})
    @JoinColumn(name = "receiver.id")
    private User receiver;
    @Enumerated(EnumType.ORDINAL)
    private TransactionStatus status;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;
}