package com.jasonpyau.form;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogSearchForm {

    public static final String ORDER_BY_ERROR = "Invalid 'orderBy', should be 'title', 'like_count'. 'view_count', or 'unix_time'.";
    public static final String SEARCH_ERROR = "'search' should have a max of 250 characters.";
    
    @Pattern(regexp = "^(title|like_count|view_count|unix_time)", message = ORDER_BY_ERROR)
    private String orderBy = "unix_time";

    private Boolean ascending = false;

    @Size(max = 250, message = SEARCH_ERROR)
    private String search = "";

    private Boolean liked = false;

}
