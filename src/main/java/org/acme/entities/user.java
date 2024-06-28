package org.acme.entities;

import org.acme.enums.roles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends PanacheEntity {

    @NotBlank(message = "Name is required")
    @JsonProperty("name")
    private String name;

    // @NotBlank(message = "Role is required")
    @JsonProperty("role")
    private roles Role;

    @NotBlank(message = "Username is required")
    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @ManyToOne
    @JsonProperty("city")
    private City city;

}