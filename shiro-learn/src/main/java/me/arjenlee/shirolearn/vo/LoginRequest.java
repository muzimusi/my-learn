package me.arjenlee.shirolearn.vo;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {
    private String username;

    private String password;

    private String mobile;

    private String email;
}
