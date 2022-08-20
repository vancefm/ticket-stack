package com.vancefm.ticketstack.pojos;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Contact {

    private Integer id;

    @NotEmpty(message = "Email Address is required")
    private String  emailAddress;
    private String  firstName;
    private String  lastName;

}
