package softuni.exodia.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Document extends BaseEntity {
    private String title;
    private String content;

    public Document() {
    }

    @Column(nullable =  false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(nullable = false, columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
