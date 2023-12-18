package com.vancefm.ticketstack.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "A subject is required.")
    private String subject;

    @Min(value = 1, message = "Invalid contact id.")
    @NotNull(message = "A contactId is required.")
    private Integer contactId;

    @NotNull(message = "A requestCategoryId is required.")
    private Integer  requestCategoryId;

    private String description;

    @NotNull(message = "A statusId is required.")
    private Integer statusId = 1;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private LocalDateTime closedTime;

}
