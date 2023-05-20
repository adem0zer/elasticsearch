package com.adem.elasticsearch.service;

import com.adem.elasticsearch.dto.PageVO;
import com.adem.elasticsearch.dto.UserEsQueryVO;
import com.adem.elasticsearch.dto.UserVO;

import java.io.IOException;
import java.util.List;

public interface UserService {

    String createUser(UserVO userVO) throws IOException;

    String deleteUser(String userId) throws IOException;

    String updateUser(UserVO userVO) throws IOException;

    UserVO getUser(String userId) throws IOException;
    List<UserVO> queryUser(UserEsQueryVO queryVO, PageVO pageVO) throws IOException;
}
