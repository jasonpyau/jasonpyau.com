package com.jasonpyau.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blogs", indexes = {@Index(name = "unix_time_ind", columnList = "unix_time"), 
                                @Index(name = "title_ind", columnList = "title"),
                                @Index(name = "like_count_ind", columnList = "like_count")})
public class Blog {

    public static final String BLOG_ID_ERROR = "Invalid 'id', blog not found.";
    public static final String BLOG_TITLE_ERROR = "'title' should be between 3-250 characters.";
    public static final String BLOG_DESCRIPTION_ERROR = "'title' should be between 3-500 characters.";
    public static final String BLOG_BODY_ERROR = "'body' should be between 1-5000 characters.";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    @Size(min = 3, max = 250, message = BLOG_TITLE_ERROR)
    @NotBlank(message = BLOG_TITLE_ERROR)
    private String title;

    @Column(name = "description")
    @Size(min = 3, max = 500, message = BLOG_DESCRIPTION_ERROR)
    @NotBlank(message = BLOG_DESCRIPTION_ERROR)
    private String description;

    @Column(name = "body", columnDefinition = "varchar(5000)")
    @Size(max = 5000, message = BLOG_BODY_ERROR)
    @NotBlank(message = BLOG_BODY_ERROR)
    private String body;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "date")
    private String date;

    @Column(name = "unix_time")
    private Long unixTime;

    @Getter(AccessLevel.NONE)
    @Column (name = "liked_users")
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "blog_user", 
                joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private final Set<User> likedUsers = new HashSet<>();

    @Transient
    private Boolean isLikedByUser;

    public void view() {
        this.viewCount++;
    }

    public void like(User user) {
        user.getLikedBlogs().add(this);
        this.likedUsers.add(user);
        this.likeCount = likedUsers.size();
    }

    public void unlike(User user) {
        user.getLikedBlogs().remove(this);
        this.likedUsers.remove(user);
        this.likeCount = likedUsers.size();
    }

    public void checkIsLikedByUser(User user) {
        this.isLikedByUser = likedUsers.contains(user);
    }
}
