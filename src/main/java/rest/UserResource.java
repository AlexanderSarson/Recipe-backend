package rest;
/*
 * author mads
 * version 1.0
 */

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.favourites.FavouriteRecipeDTO;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String get() {
        throw new UnsupportedOperationException();
    }

    // TODO - Under construction
    @POST
    @Path("favourites/add")
    @Produces({MediaType.APPLICATION_JSON})
    public Response addToFavourite(String jsonString) {
        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
        // Gets the username from the body
        String username = json.get("username").getAsString();
        // Gets the nested recipe object from the body
        FavouriteRecipeDTO favouriteRecipeDTO = new Gson().fromJson(json.get("recipe"), FavouriteRecipeDTO.class);
        // ATM just returns the recipe in order to check the functionality
        return Response.ok(favouriteRecipeDTO).build();
    }

    // TODO - Endpoint to return favourites for a given username (
}
