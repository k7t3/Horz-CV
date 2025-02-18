package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.storage.client.Storage;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * HTML5の仕様によるストレージアクセスAPIのラッパークラス。
 * <p>
 *     内部的に実験的な実装を使用しているため、提供方法が変更される可能性がある。
 * </p>
 */
public class StorageAPI {

    private static final Logger logger = Logger.getLogger(StorageAPI.class.getName());

    /**
     * ウインドウ及びタブのライフスタイルに依存するインメモリストレージ
     */
    private static final Storage session = Storage.getSessionStorageIfSupported();

    /**
     * ディスクに保存するストレージ
     */
    private static final Storage local = Storage.getLocalStorageIfSupported();

    static {
        if (session == null) {
            logger.warning("Session storage is not supported");
        }
        if (local == null) {
            logger.warning("Local storage is not supported");
        }
    }

    private StorageAPI() {
    }

    /**
     * セッションストレージに値を保存するメソッド。
     * <p>
     *     プラットフォームでセッションストレージが有効でないときは何も実行されない。
     * </p>
     * @param key 保存する値のキー
     * @param value 保存する値
     * @throws NullPointerException キー、値がnullのとき
     */
    public static void storeSessionValue(String key, String value) {
        if (session != null) {
            session.setItem(Objects.requireNonNull(key), Objects.requireNonNull(value));
        }
    }

    /**
     * セッションストレージの値をロード返すメソッド。
     * <p>
     *     キーが<code>null</code>或いは空であるときは<code>null</code>を返す。
     * </p>
     * <p>
     *     セッションストレージが有効でないときか、値が保存されていないときは<code>null</code>を返す。
     * </p>
     * @param key 値をロードするキー
     * @return セッションストレージの値。保存されていない、サポートされていないときは<code>null</code>。
     */
    public static String loadSessionValue(String key) {
        if (session != null && key != null && !key.isEmpty()) {
            return session.getItem(key);
        }
        return null;
    }

    /**
     * ローカルストレージに値を保存するメソッド。
     * <p>
     *     プラットフォームでローカルストレージが有効でないときは何も実行されない。
     * </p>
     * @param key 保存する値のキー
     * @param value 保存する値
     * @throws NullPointerException キー、値がnullのとき
     */
    public static void storeLocalValue(String key, String value) {
        if (local != null) {
            local.setItem(Objects.requireNonNull(key), Objects.requireNonNull(value));
        }
    }

    /**
     * ローカルストレージの値をロード返すメソッド。
     * <p>
     *     キーが<code>null</code>或いは空であるときは<code>null</code>を返す。
     * </p>
     * <p>
     *     ローカルストレージが有効でないときか、値が保存されていないときは<code>null</code>を返す。
     * </p>
     * @param key 値をロードするキー
     * @return ローカルストレージの値。保存されていない、サポートされていないときは<code>null</code>。
     */
    public static String loadLocalValue(String key) {
        if (local != null && key != null && !key.isEmpty()) {
            return local.getItem(key);
        }
        return null;
    }

}
