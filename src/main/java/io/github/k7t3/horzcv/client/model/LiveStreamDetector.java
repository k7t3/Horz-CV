package io.github.k7t3.horzcv.client.model;

/**
 * 特定のストリーミングサービスのURLを検出するためのインターフェース。
 */
public interface LiveStreamDetector {

    /**
     * 指定されたURLがこのサービスのものであるかどうかを判定するメソッド。
     * @param url URL
     * @return このサービスのURLであればtrue
     */
    boolean isValidURL(String url);

    /**
     * 指定されたURLからIDを抽出するメソッド。
     * @param url URL
     * @return ID
     */
    String parseId(String url);

    /**
     * 指定されたIDからURLを構築するメソッド。
     * @param id ID
     * @return URL
     */
    String construct(String id);

    /**
     * プレースホルダーを取得するメソッド。
     * @return プレースホルダー
     */
    String placeholder();

}
