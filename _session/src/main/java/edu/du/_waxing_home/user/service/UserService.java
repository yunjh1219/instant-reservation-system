package edu.du._waxing_home.user.service;

import edu.du._waxing_home.user.domain.Role;
import edu.du._waxing_home.user.domain.User;
import edu.du._waxing_home.user.dto.UserRequestDto;
import edu.du._waxing_home.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;


}