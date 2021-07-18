package io.bytestreme.userservice.data;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRegisterDTO {

    @NotNull
    @Size(min = 3, max = 20, message = "{username.min.length}")
    private String username;

    @NotNull
    @Size(min = 5, max = 20, message = "{password.min.length}")
    private String password;

}
