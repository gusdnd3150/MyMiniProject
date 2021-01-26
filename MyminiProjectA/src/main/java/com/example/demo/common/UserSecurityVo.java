package com.example.demo.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class UserSecurityVo implements UserDetails  {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UserVo user;
	
	public UserSecurityVo() {};
	
	public UserSecurityVo(UserVo user) {
		this.user=user;
	};
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(user.getAutho()));
		return authorities;
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getUserPassword();
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUserId();
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
