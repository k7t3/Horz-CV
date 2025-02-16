package io.github.k7t3.horzcv.client.model;

import java.util.Objects;

/**
 * ユーザーが入力するストリーミングに関する情報モデルクラス。
 */
public class InputLiveStream {

    private StreamingService service = StreamingService.YOUTUBE;

    private String url = "";

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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InputLiveStream that)) return false;
        return service == that.service && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, url);
    }

}
