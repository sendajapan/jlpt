package com.scholarlyapps.pathlingo.data.remote.dto;

import java.util.List;

public class ListResponse<T> {
    public List<T> data;

    public List<T> getData() {
        return data != null ? data : java.util.Collections.emptyList();
    }
}
