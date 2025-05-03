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

import io.github.k7t3.horzcv.client.model.EmbeddedChatFrame;
import io.github.k7t3.horzcv.client.model.LiveStreaming;
import io.github.k7t3.horzcv.client.model.StreamingService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ChatListViewImplTest {

    @Test
    public void testChatLinkedList() {
        // ChatViewはモックで代用
        ChatView view1 = mock(ChatView.class);
        ChatView view2 = mock(ChatView.class);
        ChatView view3 = mock(ChatView.class);

        // テスト用のチャットリンクリストを作成
        ChatListViewImpl.Chat chat1 = createChat("chat1", view1);
        ChatListViewImpl.Chat chat2 = createChat("chat2", view2);
        ChatListViewImpl.Chat chat3 = createChat("chat3", view3);

        // リンクを設定
        linkChats(chat1, chat2, chat3);

        // リンクが正しく設定されていることを検証
        assertSame(null, chat1.left);
        assertSame(chat2, chat1.right);
        
        assertSame(chat1, chat2.left);
        assertSame(chat3, chat2.right);
        
        assertSame(chat2, chat3.left);
        assertSame(null, chat3.right);
        
        // isSameViewメソッドのテスト
        assertTrue(chat1.isSameView(view1));
        assertFalse(chat1.isSameView(view2));
    }

    @Test
    public void testChatIterator() {
        // 3つのチャットで構成されるリンクリストを作成
        ChatListViewImpl.Chat first = createLinkedChats(3);
        
        // イテレータを使用してすべての要素を取得
        List<ChatListViewImpl.Chat> chats = new ArrayList<>();
        for (ChatListViewImpl.Chat chat : first) {
            chats.add(chat);
        }
        
        // サイズと内容を検証
        assertEquals(3, chats.size());
        assertEquals("chat0", chats.get(0).chatFrame.getStream().getName());
        assertEquals("chat1", chats.get(1).chatFrame.getStream().getName());
        assertEquals("chat2", chats.get(2).chatFrame.getStream().getName());
    }

    @Test
    public void testChatIteratorRemove() {
        // 5つのチャットで構成されるリンクリストを作成
        ChatListViewImpl.Chat first = createLinkedChats(5);
        
        // イテレータを使用して2番目の要素を削除
        Iterator<ChatListViewImpl.Chat> iterator = first.iterator();
        iterator.next(); // 最初の要素に移動
        ChatListViewImpl.Chat second = iterator.next(); // 2番目の要素に移動
        assertEquals("chat1", second.chatFrame.getStream().getName());
        iterator.remove(); // 2番目の要素を削除
        
        // 削除後のリンク構造を確認
        ChatListViewImpl.Chat newSecond = first.right;
        assertEquals("chat2", newSecond.chatFrame.getStream().getName());
        assertSame(first, newSecond.left);
        
        // 残りの要素を確認
        List<String> names = new ArrayList<>();
        for (ChatListViewImpl.Chat chat : first) {
            names.add(chat.chatFrame.getStream().getName());
        }
        assertEquals(List.of("chat0", "chat2", "chat3", "chat4"), names);
    }
    
    @Test
    public void testChatIteratorRemoveFirst() {
        // 3つのチャットで構成されるリンクリストを作成
        ChatListViewImpl.Chat first = createLinkedChats(3);
        
        // 最初の要素を削除
        Iterator<ChatListViewImpl.Chat> iterator = first.iterator();
        iterator.next(); // 最初の要素に移動
        iterator.remove(); // 最初の要素を削除
        
        // 残りの要素を確認
        ChatListViewImpl.Chat newFirst = first.right; // 元のfirstの次の要素が新しいfirstになる
        assertNull(newFirst.left); // 新しいfirstの左はnullになるはず
        
        List<String> names = new ArrayList<>();
        for (ChatListViewImpl.Chat chat : newFirst) {
            names.add(chat.chatFrame.getStream().getName());
        }
        assertEquals(List.of("chat1", "chat2"), names);
    }
    
    @Test
    public void testChatIteratorRemoveLast() {
        // 3つのチャットで構成されるリンクリストを作成
        ChatListViewImpl.Chat first = createLinkedChats(3);
        
        // 最後の要素を削除
        Iterator<ChatListViewImpl.Chat> iterator = first.iterator();
        iterator.next(); // 1つ目
        iterator.next(); // 2つ目
        iterator.next(); // 3つ目（最後）
        iterator.remove(); // 最後の要素を削除
        
        // 残りの要素を確認
        List<String> names = new ArrayList<>();
        for (ChatListViewImpl.Chat chat : first) {
            names.add(chat.chatFrame.getStream().getName());
        }
        assertEquals(List.of("chat0", "chat1"), names);
        
        // 2つ目の要素の右がnullになっていることを確認
        ChatListViewImpl.Chat second = first.right;
        assertNull(second.right);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testChatIteratorRemoveWithoutNext() {
        ChatListViewImpl.Chat first = createLinkedChats(3);
        Iterator<ChatListViewImpl.Chat> iterator = first.iterator();
        // next()を呼ばずにremove()を呼ぶと例外が発生する
        iterator.remove();
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testChatIteratorNextBeyondEnd() {
        ChatListViewImpl.Chat first = createLinkedChats(2);
        Iterator<ChatListViewImpl.Chat> iterator = first.iterator();
        iterator.next(); // 1つ目
        iterator.next(); // 2つ目
        // 要素がないのにnext()を呼ぶと例外が発生する
        iterator.next();
    }
    
    // テスト用のChat作成ヘルパーメソッド
    private ChatListViewImpl.Chat createChat(String name, ChatView view) {
        LiveStreaming stream = new LiveStreaming();
        stream.setName(name);
        stream.setService(StreamingService.YOUTUBE);
        stream.setId("id-" + name);
        
        return new ChatListViewImpl.Chat(new EmbeddedChatFrame(stream), view);
    }
    
    // リンクリストの作成ヘルパーメソッド
    private void linkChats(ChatListViewImpl.Chat... chats) {
        for (int i = 0; i < chats.length; i++) {
            if (i > 0) {
                chats[i].left = chats[i - 1];
            }
            if (i < chats.length - 1) {
                chats[i].right = chats[i + 1];
            }
        }
    }
    
    // n個のチャットを含むリンクリストを作成するヘルパーメソッド
    private ChatListViewImpl.Chat createLinkedChats(int count) {
        ChatListViewImpl.Chat[] chats = new ChatListViewImpl.Chat[count];
        
        for (int i = 0; i < count; i++) {
            ChatView view = mock(ChatView.class);
            chats[i] = createChat("chat" + i, view);
        }
        
        linkChats(chats);
        return chats[0]; // 先頭要素を返す
    }
}
