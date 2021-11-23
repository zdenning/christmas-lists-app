package com.denning.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.denning.demo.dynamodb.DynamoDBClientV2;
import com.denning.demo.exception.ResponseDiagnostic;
import com.denning.demo.mapper.UserMapper;
import com.denning.demo.model.User;

import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-12-05T14:50:01.382-05:00[America/New_York]")

@Controller
@RequestMapping("${openapi.christmasLists.base-path:/v1}")
public class UserApiController implements UserApi
{
	/** The JDBC template */
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DynamoDBClientV2 dynamoDBClientV2;
	
	@Autowired
	public UserApiController(final JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	
    /**
     * {@inheritDoc}
     */
	@Transactional
	@Override
	public ResponseEntity<List<User>> userAllGet()
	{
		HttpStatus status = HttpStatus.PROCESSING;
		
		final List<User> users = dynamoDBClientV2.getAllUsers();
		
		status = HttpStatus.OK;
		
        return new ResponseEntity<List<User>>(users, status);
    }
	
    /**
     * {@inheritDoc}
     */
	@Transactional
	@Override
	public ResponseEntity<List<User>> userFriendsGet(final String username)
	{
		HttpStatus status = HttpStatus.PROCESSING;
		
		final List<User> users = dynamoDBClientV2.getFriendsList(username);
		
		status = HttpStatus.OK;
		
        return new ResponseEntity<List<User>>(users, status);
    }
	
    /**
     * {@inheritDoc}
     */
	@Transactional
	@Override
	public ResponseEntity<String> userFriendsAdd(final String username, final String friendUsername)
	{
		HttpStatus status = HttpStatus.PROCESSING;
		
		dynamoDBClientV2.addFriend(username, friendUsername);
		
		status = HttpStatus.OK;
		
        return ResponseEntity.ok().build();
    }

	
    /**
     * {@inheritDoc}
     */
	@Transactional
	@Override
	public ResponseEntity<String> userLoginGet(@NotNull @ApiParam(value = "The user name for login", required = true)
			@Valid @RequestParam(value = "username", required = true) String username,
			@NotNull @ApiParam(value = "The password for login in clear text", required = true)
			@Valid @RequestParam(value = "password", required = true) String password)
	{
		
		final User user = dynamoDBClientV2.getUser(username);
		if (user.getUsername().equals(username) && user.getPassword().equals(password))
		{
			return ResponseEntity.ok().build();
		}
			
        return ResponseEntity.badRequest().body("Invalid username/password");

    }

	
    /**
     * {@inheritDoc}
     */
	@Transactional
	@Override
	public ResponseEntity<Void> userLogoutGet()
	{
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);

    }

	
    /**
     * {@inheritDoc}
     */
	@Transactional
	@Override
	public ResponseEntity<String> userPost(@ApiParam(value = "" ,required=true )  @Valid @RequestBody final User user)
	{
    	
    	final ResponseDiagnostic responseDiagnostic = dynamoDBClientV2.addUser(
    				user.getUsername(),
    				user.getFirstName(),
    				user.getPassword());
    	if (responseDiagnostic.getStatus().equals(HttpStatus.BAD_REQUEST))
    	{
    		return ResponseEntity.badRequest().body(responseDiagnostic.getMessage());
    	}
		
        return ResponseEntity.ok().build();
    }
	
	
    /**
     * {@inheritDoc}
     */
	@Transactional
	@Override
	public ResponseEntity<String> userDelete(
			@ApiParam(value = "") @Valid @RequestParam(value = "username", required = false) String username)
	{
        try
        {
        	jdbcTemplate.update("delete from USERS where USERNAME = '"+ username + "'");
        }
        catch (final Exception e)
        {
        	return ResponseEntity.badRequest().body("Failed to delete user " + username);
        }

        return ResponseEntity.ok().build();
    }
}
