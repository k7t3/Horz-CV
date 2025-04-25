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

package io.github.k7t3.horzcv.client.view.ui;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrame;
import io.github.k7t3.horzcv.client.presenter.ChatListPresenter;
import io.github.k7t3.horzcv.client.view.ChatListView;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.ui.animations.Animation;
import org.dominokit.domino.ui.animations.Transition;
import org.dominokit.domino.ui.grid.flex.FlexAlign;
import org.dominokit.domino.ui.grid.flex.FlexDirection;
import org.dominokit.domino.ui.grid.flex.FlexJustifyContent;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.dominokit.domino.view.BaseElementView;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@UiView(presentable = ChatListPresenter.class)
public class ChatListViewImpl extends BaseElementView<HTMLElement> implements ChatListView {

    private static final Logger LOGGER = Logger.getLogger(ChatListViewImpl.class.getName());

    private Chat first = null;

    private final FlexLayout flex;

    private ChatListUiHandler handler;

    public ChatListViewImpl() {
        flex = FlexLayout.create()
                .setDirection(FlexDirection.LEFT_TO_RIGHT)
                .setJustifyContent(FlexJustifyContent.CENTER)
                .setAlignItems(FlexAlign.STRETCH)
                .setGap("2mm")
                .setHeight("100%");
    }

    private void notifyListUpdated() {
        if (handler != null) {
            var list = new LinkedList<EmbeddedChatFrame>();
            if (first != null) {
                for (var chat : first) {
                    list.add(chat.chatFrame);
                }
            }
            handler.onListUpdated(Collections.unmodifiableList(list));
        }
    }

    private void clearChatList() {
        flex.clearElement();
        first = null;
    }

    @Override
    public void setChatList(List<EmbeddedChatFrame> chatFrames) {
        clearChatList();

        var size = chatFrames.size();
        var index = 0;
        Chat current = null;

        for (var chatFrame : chatFrames) {
            var html = chatFrame.getIframe();

            var chatView = new ChatView();
            flex.appendChild(chatView);

            if (first == null) {
                first = new Chat(chatFrame, chatView);
                current = first;
            } else {
                assert current != null;

                var next = new Chat(chatFrame, chatView);
                current.right = next;
                next.left = current;
                current = next;
            }

            chatView.setUiHandlers(this);
            chatView.flexItem.setFlexGrow(1);
            chatView.iframe.setInnerHtml(html);
            chatView.name.setValue(chatFrame.getStream().getName());
            chatView.setDisableLeft(index++ == 0);
            chatView.setDisableRight(index == size);
            chatView.startAnimation();
        }
    }

    private Chat find(ChatView view) {
        if (first == null) {
            throw new IllegalStateException("Chat list is empty");
        }
        if (view == null) {
            throw new IllegalArgumentException("ChatView cannot be null");
        }
        for (var chat : first) {
            if (chat.isSameView(view)) {
                return chat;
            }
        }
        throw new IllegalArgumentException("ChatView not found in the list");
    }


    private void remove(ChatView view) {
        if (first == null) {
            return; // リストが空の場合は何もしない
        }

        var iterator = first.iterator();
        Chat removed = null;
        Chat last = null;

        // 削除する要素を見つける＆最後の要素を追跡する
        while (iterator.hasNext()) {
            var chat = iterator.next();
            if (chat.right == null) {
                last = chat; // 最後の要素を記録
            }
            if (chat.isSameView(view)) {
                removed = chat;
                // 先頭の要素が削除されたときは参照を更新する
                if (chat == first) {
                    first = chat.right;
                }
                iterator.remove();
                break;
            }
        }

        if (removed != null) {
            // 削除されたノードの左右のノードを接続した後でボタン状態を更新
            var left = removed.left;
            var right = removed.right;

            // 左隣の要素が存在する場合
            if (left != null) {
                // 右隣が存在しないなら末尾になるのでrightボタンを無効化
                left.view.setDisableRight(left.right == null);
            }

            // 右隣の要素が存在する場合
            if (right != null) {
                // 左隣が存在しないなら先頭になるのでleftボタンを無効化
                right.view.setDisableLeft(right.left == null);
            }

            // 先頭要素の更新（firstが更新された場合）
            if (first != null) {
                first.view.setDisableLeft(true); // 先頭要素のleftボタンは常に無効
            }

            // 要素が1つだけになった場合、両方のボタンを無効化
            if (first != null && first.right == null) {
                first.view.setDisableLeft(true);
                first.view.setDisableRight(true);
            }

            // 末尾要素の更新（lastが削除された場合）
            if (last == removed && first != null) {
                // 新しい末尾要素を見つける
                Chat newLast = first;
                while (newLast.right != null) {
                    newLast = newLast.right;
                }
                newLast.view.setDisableRight(true); // 末尾要素のrightボタンは常に無効
            }
        }
    }


    @Override
    public void onLeftButtonClicked(ChatView view) {
        var chat = find(view);
        var left = chat.left;

        if (left != null) {
            // 左隣の要素があれば交換操作を行う
            var leftLeft = left.left;
            var chatRight = chat.right;
            
            // 左ノードの左側との接続を更新
            if (leftLeft != null) {
                leftLeft.right = chat;
            } else {
                // leftが先頭だった場合は、新しい先頭をchatに更新
                first = chat;
            }
            
            // 自ノードの右側との接続を更新
            if (chatRight != null) {
                chatRight.left = left;
            }
            
            // 両ノード間の接続を更新
            chat.left = leftLeft;
            chat.right = left;
            left.left = chat;
            left.right = chatRight;

            // UIの更新
            var leftView = left.view;
            view.setDisableLeft(chat.left == null);
            view.setDisableRight(false);
            leftView.setDisableLeft(false);
            leftView.setDisableRight(left.right == null);

            // DOM要素の順序を更新
            flex.insertBefore(view.element(), leftView.element());
            
            // アニメーション開始
            view.startAnimation();
            leftView.startAnimation();
            
            // リスト更新を通知
            notifyListUpdated();
        }
    }

    @Override
    public void onRightButtonClicked(ChatView view) {
        var chat = find(view);
        var right = chat.right;

        if (right != null) {
            // 右隣の要素があれば交換操作を行う
            var chatLeft = chat.left;
            var rightRight = right.right;
            
            // 自ノードの左側との接続���更新
            if (chatLeft != null) {
                chatLeft.right = right;
            } else {
                // chatが先頭だった場合は、新しい先頭をrightに更新
                first = right;
            }
            
            // 右ノードの右側との接続を更新
            if (rightRight != null) {
                rightRight.left = chat;
            }
            
            // 両ノード間の接続を更新
            chat.left = right;
            chat.right = rightRight;
            right.left = chatLeft;
            right.right = chat;

            // UIの更新
            var rightView = right.view;
            view.setDisableLeft(false);
            view.setDisableRight(chat.right == null);
            rightView.setDisableLeft(right.left == null);
            rightView.setDisableRight(false);

            // DOM要素の順序を更新
            flex.insertAfter(view.element(), rightView.element());
            
            // アニメーション開始
            view.startAnimation();
            rightView.startAnimation();
            
            // リスト更新を通知
            notifyListUpdated();
        }
    }

    @Override
    public void onOpenLinkClicked(ChatView view) {
        if (handler != null) {
            var chat = find(view);
            handler.onLinkOpening(chat.chatFrame);
        }
    }

    @Override
    public void onRemoveClicked(ChatView view) {
        remove(view);

        var animation = Animation.create(view.element())
                .transition(Transition.FADE_OUT_RIGHT)
                .duration(500)
                .callback(Element::remove);
        animation.animate();

        notifyListUpdated();
    }

    @Override
    public void onNameChanged(ChatView view, String name) {
        var chat = find(view);
        chat.chatFrame.getStream().setName(name);
        notifyListUpdated();
    }

    @Override
    protected HTMLElement init() {
        return flex.element();
    }

    @Override
    public void setUiHandlers(ChatListUiHandler handler) {
        this.handler = handler;
    }

    static class Chat implements Iterable<Chat> {
        final EmbeddedChatFrame chatFrame;
        final ChatView view;

        Chat left = null;
        Chat right = null;

        public Chat(EmbeddedChatFrame chatFrame, ChatView view) {
            this.chatFrame = chatFrame;
            this.view = view;
        }

        public boolean isSameView(ChatView view) {
            return Objects.equals(this.view, view);
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public Iterator<Chat> iterator() {
            return new ChatIterator(this);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Chat chat)) return false;
            return Objects.equals(chatFrame, chat.chatFrame);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(chatFrame);
        }
    }

    @SuppressWarnings("ReplaceNullCheck")
    static class ChatIterator implements Iterator<Chat> {
        private Chat current;
        private Chat next;
        private Chat previous = null;

        public ChatIterator(Chat first) {
            this.next = first;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Chat next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            previous = current;
            current = next;
            next = current.right;
            return current;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }

            // 前後のノードを適切につなぎ直す
            if (current.left != null) {
                current.left.right = current.right;
            }
            if (current.right != null) {
                current.right.left = current.left;
            }

            // 現在のポインタを更新
            if (previous == null) {
                next = current.right;
            } else {
                next = previous.right;
            }
            current = previous;
        }
    }

}
