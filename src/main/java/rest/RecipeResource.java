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
import facades.StatisticFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 * @author root
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Recipium API",
                version = "0.2",
                description = "This API is used for fetching recipes, ingredients and handling user creation and user login",
                contact = @Contact(name = "Gruppe 2", email = "gruppe2@cphbusiness.dk")
        ),
        tags = {
            @Tag(name = "Recipe", description = "API related to recipes"),
            @Tag(name = "Login", description = "API related to Login"),
            @Tag(name = "User", description = "API related to User"),
            @Tag(name = "Ingredient", description = "API Related to Ingredients"),
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
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);

    private FoodFacade foodFacade = new FoodFacade();
    private StatisticFacade statisticFacade = StatisticFacade.getStatisticFacade(EMF);
    private Gson gson = new Gson();

    @Context
    private UriInfo context;

    /**
     * Retrieves representation of an instance of rest.RecipeResource
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
    public Response searchForRecipe(
            @Parameter(
                    description = "Object with all parameters for performing a search",
                    required = true,
                    schema = @Schema(example = "{\"name\":\"falafel\",\"includeIngredients\":\"bacon,cheese,onion\",\"excludeIngredients\":\"lemon,flour\",\"offset\":0,\"number\":10,\"sessionId:\"2Ax1jf31\"}")
            )String json) {
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

    @Operation(summary = "Get a number of random recipes",
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

    @Operation(summary = "Get Recipes based on popularity",
            tags = {""}
    )
    @Path("/popular")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMostPopular() {
        return Response
                .ok(statisticFacade.getMostPopular())
                .build();
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
}
