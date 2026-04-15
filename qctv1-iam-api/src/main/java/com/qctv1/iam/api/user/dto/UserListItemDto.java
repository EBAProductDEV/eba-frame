package com.qctv1.iam.api.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserListItemDto {

    private Long id;

    private String userName;

    private String trueName;

    private String mobile;

    private String email;

    private String roleCode;

    private String status;

    private LocalDateTime loginTime;

    private LocalDateTime createTime;
}
