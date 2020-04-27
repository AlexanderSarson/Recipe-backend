/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import spoon.FoodFacade;

import javax.print.attribute.standard.Media;
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
    @Path("/search")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchForRecipe(String json) {
        String name = new JsonParser().parse(json).getAsJsonObject().get("name").getAsString();
        return Response
                .ok(foodFacade.searchByName(name,1))
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
