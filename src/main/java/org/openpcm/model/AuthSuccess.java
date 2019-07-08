package org.openpcm.model;

import org.openpcm.utils.ObjectUtil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthSuccess {

    private User user;

    private UserJWTTokenState token;

    @Override
    public String toString() {
        return ObjectUtil.print(this);
    }
}
