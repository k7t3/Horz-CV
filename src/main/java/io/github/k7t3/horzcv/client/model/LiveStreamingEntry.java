package io.github.k7t3.horzcv.client.model;

import java.util.Objects;

/**
 * ユーザーが入力するストリーミングに関する情報モデルクラス。
 */
public class LiveStreamingEntry {

    private StreamingService service = StreamingService.YOUTUBE;

    private String url = "";

    // optional
    private String description = "";

    private String id = null;

    public StreamingService getService() {
        return service;
    }

    public void setService(StreamingService service) {
        this.service = service;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        id = null; // URLが変更されたらIDを無効化
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void invalidate() {
        id = null;
    }

    public boolean isValid() {
        return id != null;
    }

    public LiveStreamingIdentity asIdentity() {
        if (!isValid()) {
            throw new IllegalStateException("ID is not set");
        }
        return new LiveStreamingIdentity(service, id);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LiveStreamingEntry entry)) return false;
        return service == entry.service && Objects.equals(url, entry.url) && Objects.equals(description, entry.description) && Objects.equals(id, entry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, url, description, id);
    }

    @Override
    public String toString() {
        return "LiveStreamingEntry{" +
                "service=" + service +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
