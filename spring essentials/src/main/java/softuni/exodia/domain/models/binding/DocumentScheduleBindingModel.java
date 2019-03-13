package softuni.exodia.domain.models.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DocumentScheduleBindingModel {
    private String title;
    private String content;

    @NotNull
    @NotEmpty
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    @NotEmpty
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
