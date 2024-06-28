package org.acme.entities;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "cities")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class City extends PanacheEntity {

    private Long id;

    @NotBlank(message = "name may not be blank")
    private String name;

    @NotBlank(message = "region may not be blank")
    private String region;

    @OneToMany(mappedBy = "city")
    private List<User> users;

}