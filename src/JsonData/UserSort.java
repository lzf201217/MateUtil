package JsonData;


/**
 * 带匹配度的Entity
 */
public class UserSort{

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserSort(String username) {
        this.username = username;
    }

    private String username;

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    private double similarity = 0;
}
