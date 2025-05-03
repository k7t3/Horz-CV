/*
 * Copyright 2025 k7t3
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.k7t3.horzcv.server.services;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import io.github.k7t3.horzcv.shared.model.StreamerInfoResponse;
import io.github.k7t3.horzcv.server.services.twitch.TwitchStreamerIdentityFinder;
import io.github.k7t3.horzcv.server.services.youtube.YoutubeStreamerIdentityFinder;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * {@link StreamerFinder}のファサードクラス
 */
@ApplicationScoped
public class StreamerFinders implements StreamerFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamerFinders.class);

    // URLに対応するストリーマーの情報のキャッシュローダー
    // Twitch4Jのライブラリが使用している軽量のキャッシュ機構
    private final LoadingCache<String, StreamerInfoResponse> cache = Caffeine.newBuilder()
            .maximumSize(40) // 最大40件
            .softValues() // ソフトリファレンス
            .expireAfterAccess(Duration.ofMinutes(10)) // 最後のアクセスから10分経過すると期限切れ
            .evictionListener(this::onEvicted)
            .build(this::findFirst);

    // 定義済みの検索インスタンス
    private List<StreamerFinder> finders;

    @Inject
    private TwitchStreamerIdentityFinder twitchFinder;

    @Inject
    private YoutubeStreamerIdentityFinder youtubeFinder;

    /**
     * コンストラクタ
     */
    public StreamerFinders() {
    }

    @PostConstruct
    public void initialize() {
        LOGGER.info("StreamerFinders initializing");
        finders = List.of(twitchFinder, youtubeFinder);
    }

    @Override
    public StreamerInfoResponse find(String urlOrKeyword) {
        if (urlOrKeyword == null || urlOrKeyword.trim().isEmpty()) {
            LOGGER.warn("empty url or keyword");
            return StreamerInfoResponse.empty();
        }
        return cache.get(urlOrKeyword);
    }

    private StreamerInfoResponse findFirst(String urlOrKeyword) {
        return finders.stream()
                .map(f -> f.find(urlOrKeyword))
                .filter(r -> !r.isEmpty())
                .findFirst()
                .orElse(StreamerInfoResponse.empty());
    }

    private void onEvicted(String url, StreamerInfoResponse response, RemovalCause cause) {
        if (cause.wasEvicted()) {
            LOGGER.debug("evicted streamer response: {}", response);
        }
    }

}
