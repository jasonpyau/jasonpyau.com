package com.jasonpyau.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationForm {

    public static final String PAGE_NUM_ERROR = "'pageNum' should be a positive integer.";
    public static final String PAGE_SIZE_ERROR = "'pageSize' should be between 1-50.";

    @Min(value = 0, message = PAGE_NUM_ERROR)
    private Integer pageNum = 0;

    @Min(value = 1, message = PAGE_SIZE_ERROR)
    @Max(value = 50, message = PAGE_SIZE_ERROR)
    private Integer pageSize = 5;
}
