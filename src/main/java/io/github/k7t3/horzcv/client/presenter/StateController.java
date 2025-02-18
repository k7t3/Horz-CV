package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.user.client.History;
import io.github.k7t3.horzcv.client.logic.EmbeddedChatList;
import io.github.k7t3.horzcv.client.logic.LiveStreamingManager;
import io.github.k7t3.horzcv.client.model.LiveStreamingIdentity;
import io.github.k7t3.horzcv.client.model.LiveStreamingEntry;
import io.github.k7t3.horzcv.client.model.StreamingService;
import io.github.k7t3.horzcv.client.presenter.twitch.TwitchChannelChatFrameBuilder;
import io.github.k7t3.horzcv.client.presenter.twitch.TwitchChannelDetector;
import io.github.k7t3.horzcv.client.presenter.youtube.YoutubeLiveChatFrameBuilder;
import io.github.k7t3.horzcv.client.presenter.youtube.YoutubeLiveDetector;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    public static final String STORAGE_KEY = "io.github.k7t3.horzcv";

    private static final Logger logger = Logger.getLogger(StateController.class.getName());
    private final LiveStreamingManager editor = new LiveStreamingManager();

    private final EmbeddedChatList chatList = new EmbeddedChatList();
    private final LiveStreamEditorPresenter editorPresenter;
    private final EmbeddedChatPresenter chatListPresenter;

    public StateController() {
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
            submit();
        });

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
        var streams = loadLiveIdentities();

        editorPresenter.setInputItems(streams);

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
        var token = StorageAPI.loadSessionValue(STORAGE_KEY);
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
        
        var streams = entries.stream()
                .map(LiveStreamingEntry::asIdentity)
                .collect(Collectors.toList());

        var token = LiveStreamingIdentity.toToken(streams);

        // ストレージにトークンを保存
        StorageAPI.storeSessionValue(STORAGE_KEY, token);

        // トークンを履歴に追加するがイベントは発行しない
        var encoded = History.encodeHistoryToken(token);
        History.newItem(encoded, false);

        logger.info("Token submitted and stored: " + token);

        // チャットリストを表示
        showChatList();

    }

}
