package com.jasonpyau.form;

import com.jasonpyau.entity.Blog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewBlogForm {

    @Size(min = 3, max = 250, message = Blog.BLOG_TITLE_ERROR)
    @NotBlank(message = Blog.BLOG_TITLE_ERROR)
    private String title;

    @Size(min = 3, max = 500, message = Blog.BLOG_DESCRIPTION_ERROR)
    @NotBlank(message = Blog.BLOG_DESCRIPTION_ERROR)
    private String description;

    @Size(max = 5000, message = Blog.BLOG_BODY_ERROR)
    @NotBlank(message = Blog.BLOG_BODY_ERROR)
    private String body;

}
