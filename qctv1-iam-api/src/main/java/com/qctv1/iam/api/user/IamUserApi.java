package com.qctv1.iam.api.user;

import com.qctv1.iam.api.common.PageResult;
import com.qctv1.iam.api.header.IamUserHeaders;
import com.qctv1.iam.api.user.dto.UserListItemDto;
import com.qctv1.iam.api.user.dto.UserPageQuery;
import com.qctv1.iam.api.user.dto.UserProfileDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
public interface IamUserApi {

    @GetMapping("/users/current")
    UserProfileDto getCurrentProfile(
            @RequestHeader(value = IamUserHeaders.USER_ID, required = false) String userId,
            @RequestHeader(value = IamUserHeaders.USER_NAME, required = false) String userName,
            @RequestHeader(value = IamUserHeaders.USER_ROLE, required = false) String roleCode
    );

    @GetMapping("/users/{id}")
    UserProfileDto getUserById(
            @PathVariable("id") Long id,
            @RequestHeader(value = IamUserHeaders.USER_ID, required = false) String userId,
            @RequestHeader(value = IamUserHeaders.USER_NAME, required = false) String userName,
            @RequestHeader(value = IamUserHeaders.USER_ROLE, required = false) String roleCode
    );

    @PostMapping("/users/page")
    PageResult<UserListItemDto> pageUsers(
            @Valid @RequestBody UserPageQuery query,
            @RequestHeader(value = IamUserHeaders.USER_ID, required = false) String userId,
            @RequestHeader(value = IamUserHeaders.USER_NAME, required = false) String userName,
            @RequestHeader(value = IamUserHeaders.USER_ROLE, required = false) String roleCode
    );
}
