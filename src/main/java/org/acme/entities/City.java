package org.acme.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class City extends PanacheEntity {

    @NotBlank(message="name may not be blank")
    private String name;
    @NotBlank(message="region may not be blank")
    private String region;


}