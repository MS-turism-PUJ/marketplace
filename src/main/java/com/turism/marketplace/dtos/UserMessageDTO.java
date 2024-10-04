package com.turism.marketplace.dtos;

import com.turism.marketplace.models.User;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserMessageDTO implements Serializable{
    private String userId;
    private String username;
    private String name;
    private String email;

    public User toUser() {
        return new User(userId, username, name, email);
    }
}

