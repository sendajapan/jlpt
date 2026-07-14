package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class PagedResponse<T> {
    public List<T> data;

    public Meta meta;

    public List<T> getData() {
        return data != null ? data : Collections.emptyList();
    }

    public static class Meta {
        @SerializedName("current_page")
        public int currentPage;

        @SerializedName("last_page")
        public int lastPage;

        @SerializedName("per_page")
        public int perPage;

        public int total;
    }
}
