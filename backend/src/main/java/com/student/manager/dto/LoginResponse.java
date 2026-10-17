package com.student.manager.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String username;
    private String nickname;
    private String avatar;
    private Set<String> permissions;
}
