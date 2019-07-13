package com.example.medicapp.networking.data;

public class MoreMessagesBody {
    private Long count;

    public MoreMessagesBody(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
