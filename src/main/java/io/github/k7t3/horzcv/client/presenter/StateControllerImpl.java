package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.History;
import io.github.k7t3.horzcv.client.logic.StateController;
import io.github.k7t3.horzcv.client.model.LiveStream;
import io.github.k7t3.horzcv.client.model.StreamingService;
import io.github.k7t3.horzcv.client.presenter.twitch.TwitchChannelChatFrameBuilder;
import io.github.k7t3.horzcv.client.presenter.twitch.TwitchChannelDetector;
import io.github.k7t3.horzcv.client.presenter.youtube.YoutubeLiveChatFrameBuilder;
import io.github.k7t3.horzcv.client.presenter.youtube.YoutubeLiveDetector;

import java.util.List;
import java.util.logging.Logger;

public class StateControllerImpl extends StateController {

    private static final Logger logger = Logger.getLogger(StateControllerImpl.class.getName());

    private final LiveStreamEditorPresenter editorPresenter;

    private final EmbeddedChatPresenter chatListPresenter;

    private final StorageAPI storage = new StorageAPI();

    public StateControllerImpl() {
        super();
        editor.registerDetector(StreamingService.YOUTUBE, new YoutubeLiveDetector());
        editor.registerDetector(StreamingService.TWITCH, new TwitchChannelDetector());
        chatList.registerBuilder(StreamingService.YOUTUBE, new YoutubeLiveChatFrameBuilder("localhost"));
        chatList.registerBuilder(StreamingService.TWITCH, new TwitchChannelChatFrameBuilder("localhost"));

        editorPresenter = new LiveStreamEditorPresenter(editor);
        chatListPresenter = new EmbeddedChatPresenter(chatList);

        // 履歴トークンがユーザーによって変更されたときの処理を登録
        History.addValueChangeHandler(e -> {
            logger.info("History token changed: " + e.getValue());
            handleToken(e.getValue());
        });

        // ランディングページのイベントを登録
        editorPresenter.getDisplay().getSubmitButton().addClickHandler(e -> {
            submitStreams();
        });

        logger.info("StateController initialized");
    }

    public void launch() {
        History.fireCurrentHistoryState();
        logger.info("StateController launched");
    }

    @Override
    protected void showLandingPage() {
        // セッションに保存されるストリームの一覧を取得
        var streams = loadLiveStreams();

        editorPresenter.setInputItems(streams);

        // ページを表示
        editorPresenter.getDisplay().show();

        logger.info("Landing page shown");
    }

    @Override
    protected void showChatList() {

        // 有効なストリームの一覧を取得してチャットリストに反映
        var streams = editor.getLiveStreams();

        chatListPresenter.setLiveStreams(streams);

        // ページを表示
        chatListPresenter.getDisplay().show();

        logger.info("Chat list page shown");
    }

    @Override
    protected List<LiveStream> loadLiveStreams() {
        List<LiveStream> streams;
        var token = storage.loadToken();
        if (token != null) {
            streams = LiveStream.fromToken(token);
        } else {
            streams = List.of();
        }

        logger.info("Loaded live streams: " + streams);

        return streams;
    }

    @Override
    protected void submitAndStoreToken(String token) {
        // ストレージにトークンを保存
        storage.storeToken(token);

        // トークンを履歴に追加するがイベントは発行しない
        var encoded = History.encodeHistoryToken(token);
        History.newItem(encoded, false);

        logger.info("Token submitted and stored: " + token);
    }

    // セッションストレージを使うためのラッパー
    private static class StorageAPI {
        public static final String STORAGE_KEY = "io.github.k7t3.horzcv";

        private final Storage storage = Storage.getSessionStorageIfSupported();

        public StorageAPI() {
            if (storage == null) {
                logger.warning("Session storage is not supported");
            }
        }

        public void storeToken(String token) {
            if (storage != null) {
                storage.setItem(STORAGE_KEY, token);
            }
        }

        public String loadToken() {
            if (storage != null) {
                return storage.getItem(STORAGE_KEY);
            }
            return null;
        }
    }

}
