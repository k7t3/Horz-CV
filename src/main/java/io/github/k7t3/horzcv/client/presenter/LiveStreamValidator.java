package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.regexp.shared.RegExp;
import io.github.k7t3.horzcv.client.model.LiveStreamDetector;

public abstract class LiveStreamValidator implements LiveStreamDetector {

    private final RegExp pattern;

    public LiveStreamValidator(String pattern) {
        this.pattern = RegExp.compile(pattern);
    }

    @Override
    public boolean isValidURL(String url) {
        return url == null || this.pattern.test(url);
    }

}
