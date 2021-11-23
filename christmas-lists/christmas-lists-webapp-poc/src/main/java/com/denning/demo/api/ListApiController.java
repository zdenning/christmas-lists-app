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
import com.denning.demo.model.Gift;
import com.denning.demo.validator.Validator;

import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.validation.Valid;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-12-05T14:50:01.382-05:00[America/New_York]")

@Controller
@RequestMapping("${openapi.christmasLists.base-path:/v1}")
public class ListApiController implements ListApi
{
	private JdbcTemplate jdbcTemplate;
	
	private Validator validator;
	
	@Autowired
	private DynamoDBClientV2 dynamoDBClientV2;
	
	@Autowired
	public ListApiController(final JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
		this.validator = new Validator(this.jdbcTemplate);
	}
    
    /**
     * {@inheritDoc}
     */
	@Transactional
    @Override
    public ResponseEntity<List<Gift>> listGet(@ApiParam(value = "Username for the list being retrieved.") @Valid
        	@RequestParam(value = "username", required = true) String username, 
        	@ApiParam(value = "Username for the logged in user.") @Valid
        	@RequestParam(value = "currentUser", required = true) String currentUser)
    {
    	HttpStatus status = HttpStatus.PROCESSING;
    	
    	final List<Gift> resultList = dynamoDBClientV2.getUserList(username);
    	
    	if (username.equals(currentUser))
    	{
    		resultList.stream().forEach(item -> {
    			item.setBought(null);
    		});
    	}
    	
    	status = HttpStatus.OK;
    	
        return new ResponseEntity<List<Gift>>(resultList, status);
    }
    
    /**
     * {@inheritDoc}
     */
	@Transactional
    @Override
    public ResponseEntity<String> listPost(@ApiParam(value = "Username for the list being retrieved.") @Valid
        	@RequestParam(value = "username", required = false) String username,  @Valid
        	@RequestBody final Gift gift)
    {

		if (!validator.validateUser(username)) return ResponseEntity.badRequest().body("Not a valid user");
    	
    	try
    	{
    		dynamoDBClientV2.addGift(username, gift);
    	}
    	catch (final Exception e)
    	{
    		return ResponseEntity.badRequest().body("That item already exists in that list!");
    	}
    	
        return ResponseEntity.ok().build();
    }

    /**
     * {@inheritDoc}
     */
	@Transactional
    @Override
	public ResponseEntity<String> listBuyPost(
			@ApiParam(value = "" ,required = true ) @Valid @RequestBody final Gift gift,
			@ApiParam(value = "Username for the list being retrieved.") @Valid @RequestParam(value = "username", required = true) final String username,
			@ApiParam(value = "The current user.") @Valid @RequestParam(value = "currentUser", required = true) final String currentUser)
	{
		if (!validator.validateUser(username)) return ResponseEntity.badRequest().body("Not a valid user");
		
		final ResponseDiagnostic response = dynamoDBClientV2.buyGift(username, gift);
		
		if (response.getStatus().equals(HttpStatus.BAD_REQUEST))
		{
			return ResponseEntity.badRequest().body(response.getMessage());
		}
		
        return ResponseEntity.ok().build();

    }

	@Override
	public ResponseEntity<String> listPatch(@Valid Gift item, @Valid String username, @Valid String currentUser)
	{
		
		final ResponseDiagnostic response = dynamoDBClientV2.updateGift(username, item);
		
		if (response.getStatus().equals(HttpStatus.BAD_REQUEST))
		{
			return ResponseEntity.badRequest().body(response.getMessage());
		}
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<String> listDelete(@Valid String item, @Valid String username, @Valid String currentUser)
	{
		final Gift gift = new Gift();
		gift.setName(item);
		final ResponseDiagnostic response = dynamoDBClientV2.deleteGift(username, gift);
		
		if (response.getStatus().equals(HttpStatus.BAD_REQUEST))
		{
			return ResponseEntity.badRequest().body(response.getMessage());
		}
		return ResponseEntity.ok().build();
	}
}
