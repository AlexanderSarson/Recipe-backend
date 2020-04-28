/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.FoodResultDTOList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Path("recipe")
public class RecipeResource {

    private FoodFacade foodFacade = new FoodFacade();
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RecipeResource
     */
    public RecipeResource() {
    }

    /**
     * Retrieves representation of an instance of rest.RecipeResource
     * @return an instance of java.lang.String
     */
    @Operation(summary = "Search for recipes, given a part of a full title of the recipe",
            tags = {"search"},
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodResultDTOList.class))),
                    @ApiResponse(responseCode = "200", description = "The found recipes")})
    @Path("/search")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchForRecipe(String json) {
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        String name = object.get("name").getAsString();
        int number = object.get("number").getAsInt();
        return Response
                .ok(foodFacade.searchByName(name,number))
                .build();
    }
    
    @Path("/random")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRandomRecipe() {
        return "{\n" +
"    \"offset\": 0,\n" +
"    \"number\": 2,\n" +
"    \"results\": [\n" +
"        {\n" +
"            \"id\": 633508,\n" +
"            \"image\": \"Baked-Cheese-Manicotti-633508.jpg\",\n" +
"            \"imageUrls\": [\n" +
"                \"Baked-Cheese-Manicotti-633508.jpg\"\n" +
"            ],\n" +
"            \"readyInMinutes\": 45,\n" +
"            \"servings\": 6,\n" +
"            \"title\": \"Baked Cheese Manicotti\"\n" +
"        },\n" +
"        {\n" +
"            \"id\": 634873,\n" +
"            \"image\": \"Best-Baked-Macaroni-and-Cheese-634873.jpg\",\n" +
"            \"imageUrls\": [\n" +
"                \"Best-Baked-Macaroni-and-Cheese-634873.jpg\"\n" +
"            ],\n" +
"            \"readyInMinutes\": 45,\n" +
"            \"servings\": 12,\n" +
"            \"title\": \"Best Baked Macaroni and Cheese\"\n" +
"        }\n" +
"    ],\n" +
"    \"totalResults\": 719\n" +
"}";
    }

}
