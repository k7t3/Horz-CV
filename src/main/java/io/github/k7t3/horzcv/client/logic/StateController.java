package io.github.k7t3.horzcv.client.logic;

import io.github.k7t3.horzcv.client.model.LiveStream;

import java.util.List;

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
 *     このクラスはprotectedな変数として
 *     {@link LiveStreamEditor}・{@link EmbeddedChatList}を持っており、
 *     それぞれ{@link #showLandingPage()}、{@link #showChatList()}の実装時に使用できる。
 * </p>
 */
public abstract class StateController {

    protected final LiveStreamEditor editor = new LiveStreamEditor();

    protected final EmbeddedChatList chatList = new EmbeddedChatList();

    /**
     * トークンがユーザー操作で変更されたときに呼び出すメソッド。
     * <p>
     *     トークンの内容に基づいてストリーミング情報を生成し、
     *     チャットリストかランディングページに遷移する。
     * </p>
     * @param token 変更されたトークン
     */
    protected void handleToken(String token) {
        // トークンが空のときはランディングページを表示
        if (token == null || token.trim().isEmpty()) {
            showLandingPage();
            return;
        }

        var streams = LiveStream.fromToken(token);
        if (streams.isEmpty()) {
            showLandingPage();
        } else {
            editor.setAll(streams);
            showChatList();
        }
    }

    /**
     * ランディングページを表示する。
     * <p>
     *     ストレージにストリーミング情報が保存されていればそれをロードすること。
     * </p>
     * @see #loadLiveStreams() 保存される情報をロードするメソッド
     * @see LiveStreamEditor ストリーミング情報の入力関連
     */
    protected abstract void showLandingPage();

    /**
     * チャットリストページを表示する。
     * <p>
     *     必要なフレーム情報は{@link #chatList}から取得できる。
     * </p>
     * @see EmbeddedChatList 埋め込みチャットフレーム関連
     */
    protected abstract void showChatList();

    /**
     * 保存されたトークンからストリーミング情報をロードするメソッド。
     * <p>
     *     トークンが保存されていないときは空のリストを返す。
     * </p>
     * @return ストリーミング情報。空のこともある。
     */
    protected abstract List<LiveStream> loadLiveStreams();

    /**
     * 状態トークンを提出・保存するメソッド。
     * <p>
     *     保存したトークンはランディングページの表示時に使用されることがある。
     * </p>
     * @param token トークン
     */
    protected abstract void submitAndStoreToken(String token);

    /**
     * 入力されたストリーミングの情報を提出するメソッド。
     * <p>
     *     適切な入力が存在し、適切に処理されたときはtrue
     * </p>
     * @return 入力が正常で適切に処理されたときはtrue
     */
    public boolean submitStreams() {
        var streams = editor.getLiveStreams();
        if (streams.isEmpty()) {
            return false;
        }

        var token = LiveStream.toToken(streams);
        submitAndStoreToken(token);

        // チャットリストを表示
        chatList.setAll(streams);
        showChatList();

        return true;
    }

}
