package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.regexp.shared.RegExp;
import io.github.k7t3.horzcv.client.model.LiveStreamingDetector;

public abstract class LiveStreamingValidator implements LiveStreamingDetector {

    private final RegExp pattern;

    public LiveStreamingValidator(String pattern) {
        this.pattern = RegExp.compile(pattern);
    }

    @Override
    public boolean isValidURL(String url) {
        return url == null || this.pattern.test(url);
    }

}
