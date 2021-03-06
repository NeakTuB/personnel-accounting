package com.personnel_accounting.entity.converter.dto;

import com.personnel_accounting.domain.User;
import com.personnel_accounting.entity.dto.AuthorityDTO;
import com.personnel_accounting.entity.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;

public class UserDTOConverter implements Converter<User, UserDTO> {

    @Override
    public UserDTO convert(User source) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(source.getUsername());
        userDTO.setPassword(source.getPassword());
        userDTO.setActive(source.isActive());

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setUsername(source.getUsername());
        authorityDTO.setRole(source.getAuthority().getRole());
        userDTO.setAuthority(authorityDTO);
        return userDTO;
    }
}
