package io.github.k7t3.horzcv.client.presenter;

import gwt.material.design.client.base.validator.RegExValidator;
import io.github.k7t3.horzcv.client.model.LiveStreamDetector;

public abstract class LiveStreamValidator extends RegExValidator implements LiveStreamDetector {

    public LiveStreamValidator(String pattern, String invalidMessageOverride) {
        super(pattern, invalidMessageOverride);
    }

    @Override
    public boolean isValidURL(String url) {
        return isValid(url);
    }

}
