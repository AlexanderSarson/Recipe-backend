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
import session.Search;
import session.SessionOffsetManager;
import spoonacular.FoodFacade;

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
            @Tag(name = "Recipe", description = "API related to recipes"),
            @Tag(name = "Login", description = "API related to Login"),
            @Tag(name = "User", description = "API related to User")
        },
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
            tags = {"Recipe"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTOList.class))),
                @ApiResponse(responseCode = "200", description = "The found recipes")})
    @Path("/search")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchForRecipe(String json) {
        // Create a Search from the object.
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        Search search = Search.searchFromJsonObject(object);
        int number = object.get("number").getAsInt();

        JsonElement sessionIdElement = object.get("sessionId");
        String sessionId = "";
        if(sessionIdElement != null)
            sessionId = sessionIdElement.getAsString();

        int sessionOffset = getSessionOffset(sessionId, object, search.toString(), number);
        search.addParameter("offset",""+sessionOffset);

        return Response.ok(foodFacade.complexSearch(search)).build();
    }

    /**
     * This will handle all of the offset correction needed.
     * @param sessionId the sessionId of the current request.
     * @param object The POSTed object.
     * @param completeSearchString the searched recipe
     * @param number the number of elements to fetch.
     * @return A Integer offset.
     */
    private int getSessionOffset(String sessionId, JsonObject object, String completeSearchString, int number) {
        int sessionOffset = SessionOffsetManager.getSessionOffset(sessionId,completeSearchString);
        JsonElement offsetMove = object.get("moveOffset");
        if(offsetMove != null && offsetMove.getAsString().equals("forward")) {
            SessionOffsetManager.setSessionOffset(sessionId,completeSearchString,sessionOffset+number);
        } else if(offsetMove != null && offsetMove.getAsString().equals("backward")) {
            sessionOffset = Math.min(0, sessionOffset-(number*2));
            SessionOffsetManager.setSessionOffset(sessionId,completeSearchString,sessionOffset);
        } else {
            sessionOffset = 0;
            SessionOffsetManager.setSessionOffset(sessionId,completeSearchString,sessionOffset+number);
        }
        return sessionOffset;
    }

    @Operation(summary = "Get x random number of recipes",
            tags = {"Recipe"},
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
            tags = {"Recipe"},
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
