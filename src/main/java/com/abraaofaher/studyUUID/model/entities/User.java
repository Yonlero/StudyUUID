package com.abraaofaher.studyUUID.model.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User implements Serializable {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid3")
    private UUID id;
    @NotNull(message = "The field 'name' shouldn't be null")
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @NotNull(message = "The field 'amount' shouldn't be null")
    private BigInteger amount;
    @Column(unique = true, nullable = false)
    @NotNull(message = "The field 'email' shouldn't be null")
    private String email;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime created_at;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updated_at;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}