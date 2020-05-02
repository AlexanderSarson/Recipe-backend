package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import dtos.favourites.FavouriteRecipeDTO;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
@NamedQuery(name = "User.deleteAllRows", query = "DELETE FROM User")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "user_name", length = 25)
  private String userName;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "user_pass")
  private String userPass;
  @JoinTable(name = "user_roles", joinColumns = {
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
    @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
  @ManyToMany
  private List<Role> roleList = new ArrayList();

  @Column(name="user_salt")
  private String userSalt;

  public List<String> getRolesAsStrings() {
    if (roleList.isEmpty()) {
      return null;
    }
    List<String> rolesAsStrings = new ArrayList();
    for (Role role : roleList) {
      rolesAsStrings.add(role.getRoleName());
    }
    return rolesAsStrings;
  }

  @JoinTable(name = "user_favourites", joinColumns = {
          @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
          @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id")})
  @ManyToMany(cascade = CascadeType.PERSIST)
  private List<FavouriteRecipe> favourites = new ArrayList<>();

  public User() {}

   public boolean verifyPassword(String pw){
        return(BCrypt.checkpw(pw,userPass));
    }

  public User(String userName, String userPass) {
    this.userName = userName;
    this.userSalt = BCrypt.gensalt();
    this.userPass = BCrypt.hashpw(userPass, this.userSalt);
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPass() {
    return this.userPass;
  }

  public void setUserPass(String userPass) {
    this.userPass = userPass;
  }

  public List<Role> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<Role> roleList) {
    this.roleList = roleList;
  }

  public void addRole(Role userRole) {
    roleList.add(userRole);
  }

  public void addFavourite(FavouriteRecipe recipe) {
    favourites.add(recipe);
  }

  public void removeFavourite(FavouriteRecipe recipe) {
    favourites.remove(recipe);
  }

  public List<FavouriteRecipe> getFavourites() {
    return favourites;
  }

  public void setFavourites(List<FavouriteRecipe> favourites) {
    this.favourites = favourites;
  }
}
