package io.github.k7t3.horzcv.client.model;

/**
 * 特定のストリーミングの埋め込みチャットフレームを持つレコード
 * <p>
 *     iframe要素を構築するときはXSS対策を施すこと。
 * </p>
 * @param stream ストリーミング
 * @param frame チャットフレーム
 */
public record EmbeddedChatFrame(LiveStream stream, String frame) {
}
