package com.dr.bean;

/**
 * @description: page return

 */
public class PageResponse<T> extends Response<T> {


    private Long count;

    public PageResponse(String code, String msg, T data, Long count) {
        super(code, msg, data);
        this.count = count;
    }

    public static <T> PageResponse<T> ok(T data, Long count) {
        return new PageResponse<>("200", "", data, count);
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
