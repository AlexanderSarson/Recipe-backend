package rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import spoonacular.FoodFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("ingredient")
public class IngredientResource {
        private final FoodFacade foodFacade = new FoodFacade();

        private static final int NUMBER_OF_RESULTS = 30;
        @Context
        private UriInfo context;
        public IngredientResource() {
        }

        @Operation(summary = "Tries to autocomplete a given string to an ingredient",
                tags = {"Ingredient"},
                responses = {
                        @ApiResponse(
                                content = @Content(mediaType = "application/json", schema = @Schema(example = "[{\"id\":0,\"name\":\"apple\"},{\"id\":0,\"name\":\"applesauce\"}, ...]"))),
                        @ApiResponse(responseCode = "200", description = "The found recipes")})
        @Path("/autocomplete/{query}")
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Response autoCompleteIngredient(@PathParam("query") String json) {
            return Response.ok(foodFacade.autoCompleteIngredient(json, NUMBER_OF_RESULTS)).build();
        }
}
