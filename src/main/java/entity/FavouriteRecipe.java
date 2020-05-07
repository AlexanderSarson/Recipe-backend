package entity;
/*
 * author mads
 * version 1.0
 */

import dtos.favourites.FavouriteRecipeDTO;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "favourite_recipe")
@NamedQuery(name = "favourite_recipe.deleteAllRows", query = "DELETE FROM FavouriteRecipe ")
public class FavouriteRecipe implements Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "recipe_id")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "title")
    private String title;

    @Basic()
    @Column(name  = "imgUrl")
    private String imgUrl;

    @Basic()
    @Column(name = "readyInMinutes")
    private Integer readyInMinutes;

    @Basic()
    @Column(name = "servings")
    private Integer servings;

    @ManyToMany(mappedBy = "favourites")
    private List<User> userList;


    public FavouriteRecipe() {
    }

    public FavouriteRecipe (FavouriteRecipeDTO dto) {
        this.id = dto.getId();
        this.title = dto.getTitle();
        this.imgUrl = dto.getImage();
        this.readyInMinutes = dto.getReadyInMinutes();
        this.servings = dto.getServings();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
