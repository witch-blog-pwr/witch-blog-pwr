package com.witchs.blog.post;

import com.witchs.blog.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private Integer likes;
    private Integer disLikes;
    private String content;
    private Date createDate;
    private Integer userId;

    public Post(String title, Integer likes, Integer disLikes, String content, Date createDate, Integer userId) {
        this.title = title;
        this.likes = likes;
        this.disLikes = disLikes;
        this.content = content;
        this.createDate = createDate;
        this.userId = userId;
    }

}
