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

package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import io.github.k7t3.horzcv.client.logic.EmbeddedChatList;
import io.github.k7t3.horzcv.client.logic.LiveStreamingManager;
import io.github.k7t3.horzcv.client.model.LiveStreamingIdentity;
import io.github.k7t3.horzcv.client.model.StreamingService;
import io.github.k7t3.horzcv.client.presenter.twitch.TwitchChannelChatFrameBuilder;
import io.github.k7t3.horzcv.client.presenter.twitch.TwitchChannelDetector;
import io.github.k7t3.horzcv.client.presenter.youtube.YoutubeLiveChatFrameBuilder;
import io.github.k7t3.horzcv.client.presenter.youtube.YoutubeLiveDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 画面の状態コントローラー
 * <p>
 *     下記の状態を管理する。
 *     <nl>
 *         <li>ランディングページ</li>
 *         アクセス時に初期表示されるページ。
 *         <li>チャットリストページ</li>
 *         入力されたストリーミング情報に基づいて表示されるチャット一覧が表示されるページ。
 *     </nl>
 * </p>
 * <p>
 *     このクラスはprivateな変数として
 *     {@link LiveStreamingManager}・{@link EmbeddedChatList}を持っており、
 *     それぞれ{@link #showLandingPage()}、{@link #showChatList()}の実装時に使用できる。
 * </p>
 */
public class StateController {

    public static final String STORAGE_KEY = "value";

    private static final Logger logger = Logger.getLogger(StateController.class.getName());
    private final LiveStreamingManager editor = new LiveStreamingManager();

    private final EmbeddedChatList chatList = new EmbeddedChatList();
    private final LiveStreamEditorPresenter editorPresenter;
    private final EmbeddedChatPresenter chatListPresenter;

    private final WebStorageDataStore displayNameStore = new WebStorageDataStore(
            "horzcv.displayname",
            new GWTWebStorage(Storage.getLocalStorageIfSupported())
    );
    private final WebStorageDataStore tokenStore = new WebStorageDataStore(
            "horzcv.token",
            new GWTWebStorage(Storage.getSessionStorageIfSupported())
    );

    public StateController() {
        editor.registerDetector(StreamingService.YOUTUBE, new YoutubeLiveDetector());
        editor.registerDetector(StreamingService.TWITCH, new TwitchChannelDetector());
        chatList.registerBuilder(StreamingService.YOUTUBE, new YoutubeLiveChatFrameBuilder(Window.Location.getHost()));
        chatList.registerBuilder(StreamingService.TWITCH, new TwitchChannelChatFrameBuilder(Window.Location.getHost()));

        editorPresenter = new LiveStreamEditorPresenter(editor, displayNameStore);
        chatListPresenter = new EmbeddedChatPresenter(chatList, displayNameStore);

        // 履歴トークンがユーザーによって変更されたときの処理を登録
        History.addValueChangeHandler(e -> {
            logger.info("History token changed: " + e.getValue());
            handleToken(e.getValue());
        });

        // ランディングページのイベントを登録
        editorPresenter.getDisplay().getSubmitButton().addClickHandler(e -> {
            submit();
        });

        // データストアを初期化
        displayNameStore.initialize();
        // tokenStore.initialize(); // セッションは初期化不要

        logger.info("StateController initialized");
    }

    /**
     * トークンがユーザー操作で変更されたときに呼び出すメソッド。
     * <p>
     *     トークンの内容に基づいてストリーミング情報を生成し、
     *     チャットリストかランディングページに遷移する。
     * </p>
     * @param token 変更されたトークン
     */
    private void handleToken(String token) {
        // トークンが空のときはランディングページを表示
        if (token == null || token.trim().isEmpty()) {
            showLandingPage();
            return;
        }

        var identities = LiveStreamingIdentity.fromToken(token);
        if (identities.isEmpty()) {
            // トークンが不正なときはランディングページを表示
            showLandingPage();
        } else {
            // チャットリストで表示するストリーミング情報を復元
            editor.restoreFromIdentities(identities);

            for (var entry : editor.getEntries()) {
                // データストアに表示名が保存されていた場合はそれを反映
                if (entry.isValid()) {
                    var identity = entry.asIdentity();
                    var displayName = displayNameStore.load(identity.toToken());
                    if (displayName != null) {
                        entry.setDisplayName(displayName);
                    }
                }
            }

            // チャットリストを表示
            showChatList();
        }
    }

    public void launch() {
        History.fireCurrentHistoryState();
        logger.info("StateController launched");
    }

    private void showLandingPage() {
        // セッションに保存されるストリームの一覧を取得
        var identities = loadLiveIdentities();

        editorPresenter.setInputItems(identities);

        // ページを表示
        editorPresenter.getDisplay().show();

        StateController.logger.info("Landing page shown");
    }

    private void showChatList() {
        // 有効なストリームの一覧を取得してチャットリストに反映
        var entries = editor.getActiveEntries();

        chatListPresenter.setLiveStreams(entries);

        // ページを表示
        chatListPresenter.getDisplay().show();

        StateController.logger.info("Chat list page shown");
    }

    private List<LiveStreamingIdentity> loadLiveIdentities() {
        List<LiveStreamingIdentity> streams;
        var token = tokenStore.load(STORAGE_KEY);
        if (token != null) {
            streams = LiveStreamingIdentity.fromToken(token);
        } else {
            streams = List.of();
        }

        logger.info("Loaded live streams: " + streams);

        return streams;
    }

    private void submit() {
        // 入力内容をモデルに反映
        editorPresenter.updateModels();

        var entries = editor.getActiveEntries();
        if (entries.isEmpty()) {
            return;
        }

        // 入力された情報をサービスごとのエントリに変換
        var identities = new ArrayList<LiveStreamingIdentity>(entries.size());
        for (var entry : entries) {
            var identity = entry.asIdentity();

            // 表示名が設定されている場合は保存
            var displayName = entry.getDisplayName();
            if (displayName != null) {
                displayNameStore.store(identity.toToken(), displayName);
            } else {
                displayNameStore.remove(identity.toToken());
            }

            identities.add(identity);
        }

        // ストレージにトークンを保存
        var token = LiveStreamingIdentity.toToken(identities);
        tokenStore.store(STORAGE_KEY, token);

        // トークンを履歴に追加するがイベントは発行しない
        var encoded = History.encodeHistoryToken(token);
        History.newItem(encoded, false);

        logger.info("Token submitted and stored: " + token);

        // チャットリストを表示
        showChatList();

    }

}
