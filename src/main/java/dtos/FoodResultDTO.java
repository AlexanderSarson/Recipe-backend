package dtos;

import java.util.List;

public class FoodResultDTO {
    private long id;
    private String image;
    private List<String> imageUrls;
    private int readyInMinutes;
    private int servings;
    private String title;

    public FoodResultDTO(long id, String image, List<String> imageUrls, int readyInMinutes, int servings, String title) {
        this.id = id;
        this.image = image;
        this.imageUrls = imageUrls;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "FetchResultDTO{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", imageUrls=" + imageUrls +
                ", readyInMinutes=" + readyInMinutes +
                ", servings=" + servings +
                ", title='" + title + '\'' +
                '}';
    }
}
