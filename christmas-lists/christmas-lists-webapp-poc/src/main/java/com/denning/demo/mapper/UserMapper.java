package com.denning.demo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.denning.demo.model.User;

public class UserMapper implements RowMapper<User>
{
	
	public User mapRow(final ResultSet rs, int rowNum) throws SQLException
	{
		final User user = new User();
		user.setUsername(rs.getString("USERNAME"));
		user.setPassword(rs.getString("PASSWORD"));
		user.setFirstName(rs.getString("FIRST_NAME"));
		
		return user;
		
	}
}
