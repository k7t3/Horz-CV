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

package io.github.k7t3.horzcv.client.view;

import io.github.k7t3.horzcv.client.model.EmbeddedChatFrame;
import io.github.k7t3.horzcv.client.view.ui.ChatView;
import org.dominokit.domino.api.client.mvp.view.ContentView;
import org.dominokit.domino.api.client.mvp.view.HasUiHandlers;
import org.dominokit.domino.api.client.mvp.view.UiHandlers;

import java.util.List;

public interface ChatListView extends ContentView,
        HasUiHandlers<ChatListView.ChatListUiHandler>, ChatView.ChatUiHandler {

    /**
     * チャットリストに表示するストリームのリストを設定する。
     * <p>
     * 値に割り当てられるのは埋め込みのチャットビューを表示するためのHTML。
     * </p>
     * @param chatFrames 表示するチャットリスト
     */
    void setChatList(List<EmbeddedChatFrame> chatFrames);

    interface ChatListUiHandler extends UiHandlers {

        /**
         * 表示されているチャットリストが削除されるか順番を変更されるときに呼び出される。
         * @param chatFrames 表示されるチャットリスト
         */
        void onListUpdated(List<EmbeddedChatFrame> chatFrames);

        /**
         * ストリームのリンクがクリックされたときに呼び出される。
         * @param chatFrame ストリーム
         */
        void onLinkOpening(EmbeddedChatFrame chatFrame);
    }
}
