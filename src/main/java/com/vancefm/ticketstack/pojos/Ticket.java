package com.vancefm.ticketstack.pojos;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class Ticket {

    private Integer       id;

    @NotEmpty(message = "A ticket subject is required.")
    private String        subject;
    private Integer       contactId;
    private Integer       requestCategoryId;
    private String        description;
    private Integer       statusId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private LocalDateTime closedTime;

}
