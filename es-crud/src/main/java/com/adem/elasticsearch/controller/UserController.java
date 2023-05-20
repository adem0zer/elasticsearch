package com.adem.elasticsearch.controller;

import com.adem.elasticsearch.dto.PageVO;
import com.adem.elasticsearch.dto.UserEsQueryVO;
import com.adem.elasticsearch.dto.UserVO;
import com.adem.elasticsearch.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    String createUser(@RequestBody UserVO userVO) throws IOException {
        return userService.createUser(userVO);
    }

    @PutMapping
    String updateUser(@RequestBody UserVO userVO) throws IOException {
        return userService.updateUser(userVO);
    }


    @GetMapping(value = "{userId}")
    UserVO getUser(@PathVariable String userId) throws IOException {
        return userService.getUser(userId);
    }


    @DeleteMapping(value = "{userId}")
    String deleteUser(@PathVariable String userId) throws IOException {
        return userService.deleteUser(userId);
    }

    @PostMapping(value = "{page}/{pageSize}")
    List<UserVO> queryUser(@RequestBody UserEsQueryVO queryVO, PageVO pageVO) throws IOException {
        return userService.queryUser(queryVO, pageVO);
    }
}
