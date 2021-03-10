package com.brazilprev.brazilprev.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClientRegister {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cpf;

    private String gender;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Embedded
    private Address address;

    private Boolean active = Boolean.TRUE;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime createDate;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime updateDate;

    public void active() {
        setActive(true);
    }

    public void inactive() {
        setActive(false);
    }

    @PrePersist
    private void generateUUIDToClientId() {
        setClientId(UUID.randomUUID().toString());
    }

}
