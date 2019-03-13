package softuni.exodia.domain.models.view;

public class DocumentHomeViewModel {
    private static final String ELLIPSIS = "...";
    private static final int TWELVE = 12;
    private static final int ZERO = 0;

    private String id;
    private String title;

    public DocumentHomeViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        if (title.length() < TWELVE) {
            return title + ELLIPSIS;
        }
        return title.substring(ZERO, TWELVE) + ELLIPSIS;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
