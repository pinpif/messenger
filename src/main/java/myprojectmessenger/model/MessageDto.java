package myprojectmessenger.model;

import java.util.Date;

public class MessageDto {
    private Date date;
    private String author;
    private Long authorId;
    private String message;
    private ContentDto contentDto;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ContentDto getContentDto() {
        return contentDto;
    }

    public void setContentDto(ContentDto contentDto) {
        this.contentDto = contentDto;
    }
}
