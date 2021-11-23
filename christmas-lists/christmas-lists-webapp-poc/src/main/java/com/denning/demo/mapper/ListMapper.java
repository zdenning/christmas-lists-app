package com.denning.demo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.denning.demo.model.Gift;

public class ListMapper implements RowMapper<Gift>
{
	
	public Gift mapRow(final ResultSet rs, int rowNum) throws SQLException
	{
		final Gift item = new Gift();
		item.setName(rs.getString("NAME"));
		item.setDescription(rs.getString("NOTES"));
		item.setBought(rs.getBoolean("BOUGHT"));
		
		return item;
		
	}
}
