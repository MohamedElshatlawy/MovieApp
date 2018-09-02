package vogella.com.movieapp;

/**
 * Created by BinaryWorld on 25-Nov-16.
 */
public class ReviewModel {
    String author;
    String content;

    public ReviewModel(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
