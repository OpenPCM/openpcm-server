package org.openpcm.model;

import org.openpcm.utils.ObjectUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
