/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.RecipeDTOList;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import session.SessionOffsetManager;
import spoonacular.FoodFacade;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author root
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Recipe API",
                version = "0.1",
                description = "This API is use as a base for building a backend for a separate Frontend",
                contact = @Contact(name = "Gruppe 2", email = "gruppe2@cphbusiness.dk")
        ),
        tags = {
            @Tag(name = "recipe", description = "API related to recipes"),
            @Tag(name = "login", description = "API related to Login"),},
        servers = {
            @Server(
                    description = "For Local host testing",
                    url = "http://localhost:8080/recipe-backend"
            ),
            @Server(
                    description = "Server API",
                    url = "https://www.sarson.codes/recipe-backend"
            )
        }
)
@Path("recipe")
public class RecipeResource {

    private FoodFacade foodFacade = new FoodFacade();
    private Gson gson = new Gson();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RecipeResource
     */
    public RecipeResource() {
    }

    /**
     * Retrieves representation of an instance of rest.RecipeResource
     *
     * @return an instance of java.lang.String
     */
    @Operation(summary = "Search for recipes, given a part of a full title of the recipe",
            tags = {"search"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTOList.class))),
                @ApiResponse(responseCode = "200", description = "The found recipes")})
    @Path("/search")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchForRecipe(@Context HttpServletRequest request, String json) {
        // Get the session id from the header.
        String sessionId = request.getHeader("sessionId") ;
        // Get data from the post request.
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        String search = object.get("name").getAsString();
        int number = object.get("number").getAsInt();

        int sessionOffset = SessionOffsetManager.getSessionOffset(sessionId,search);

        // Correct the sessionOffset.
        if(isOffsetMovingForward(object)) {
            SessionOffsetManager.setSessionOffset(sessionId,search,sessionOffset+number);
        } else if(isOffsetMovingBackward(object)) {
            sessionOffset = Math.min(0, sessionOffset-(number*2));
            SessionOffsetManager.setSessionOffset(sessionId,search,sessionOffset);
        } else {
            sessionOffset = 0;
            SessionOffsetManager.setSessionOffset(sessionId,search,sessionOffset+number);
        }

        // Return data, fetched using both the limiting number and the offset.
        return Response
                .ok(foodFacade.searchByName(search, number, sessionOffset))
                .build();
    }

    /**
     * Check if the request is moving the offset forward.
     * @param object the object holding the String offsetMove value
     * @return FALSE if 'forward' is specified, FALSE in any other case.
     */
    private boolean isOffsetMovingForward(JsonObject object) {
        JsonElement offsetMove = object.get("moveOffset");
        if(offsetMove != null) {
            String move = offsetMove.getAsString();
            return move.equals("forward");
        }
        return false;
    }
    /**
     * Check if the request is moving the offset forward.
     * @param object the object holding the String offsetMove value
     * @return TRUE if 'backwards' is specified, FALSE in any other case.
     */
    private boolean isOffsetMovingBackward(JsonObject object) {
        JsonElement offsetMove = object.get("moveOffset");
        if(offsetMove != null) {
            String move = offsetMove.getAsString();
            return move.equals("backward");
        }
        return false;
    }

    @Operation(summary = "Get x random number of recipes",
            tags = {"random"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTOList.class))),
                @ApiResponse(responseCode = "200", description = "The found random recipes")})
    @Path("/random/{number}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRandomRecipe(@PathParam("number") int number) {
        return Response
                .ok(foodFacade.getRandomRecipes(number))
                .build();
    }
    
    @Operation(summary = "Get recipe by id",
            tags = {"random"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTOList.class))),
                @ApiResponse(responseCode = "200", description = "The found recipe")})
    @Path("/id/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecipeById(@PathParam("id") long id) {
        return gson.toJson(foodFacade.getRecipeById(id));
    }

}
