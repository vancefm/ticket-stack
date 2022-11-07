package com.vancefm.ticketstack.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    private Integer id;

    @NotEmpty(message = "Email Address is required")
    private String  emailAddress;
    private String  firstName;
    private String  lastName;

}
