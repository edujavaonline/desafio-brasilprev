package com.brazilprev.brazilprev.api.openapi;

import com.brazilprev.brazilprev.api.request.ClientRegisterRequest;
import com.brazilprev.brazilprev.api.response.ClientRegisterResponse;
import com.brazilprev.brazilprev.exception.exceptionhandler.ApiError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@Api(tags = "Clients")
public interface ClientRegisterControllerOpenApi {

    @ApiOperation("Find all clients")
    public List<ClientRegisterResponse> findAll(boolean showInactivesToo);

    @ApiOperation("Find client by id")
    public ClientRegisterResponse findById(Long id);

    @ApiOperation("Save client")
    public ClientRegisterResponse save(ClientRegisterRequest clientRegisterRequest) ;

    @ApiOperation("Update client")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Request invalid (API consumer error)!", response = ApiError.class),
            @ApiResponse(code = 406, message = "Resource representation is not accepted!", response = ApiError.class),
            @ApiResponse(code = 404, message = "Client not found!", response = ApiError.class),
            @ApiResponse(code = 415, message = "Request declined because the body is in an unsupported format!", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal server error!", response = ApiError.class)
    })
    public ClientRegisterResponse update(Long id, ClientRegisterRequest clientRegisterRequest);

    @ApiOperation("Active client")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Client activated successfully!"),
            @ApiResponse(code = 404, message = "Client not found!", response = ApiError.class)
    })
    public void active(Long id);

    @ApiOperation("Inactive client")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Client inactivated successfully!"),
            @ApiResponse(code = 404, message = "Client not found!", response = ApiError.class)
    })
    public void inactive(Long id) ;
}
