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
import io.github.k7t3.horzcv.client.view.LayoutEvent;
import io.github.k7t3.horzcv.client.view.OOPSView;
import io.github.k7t3.horzcv.client.view.Routes;
import io.github.k7t3.horzcv.client.view.Slots;
import org.dominokit.domino.api.client.annotations.presenter.*;
import org.dominokit.domino.api.client.mvp.presenter.ViewablePresenter;

@AutoRoute(token = Routes.OOPS)
@AutoReveal
@Slot(Slots.CONTENT)
@PresenterProxy(name = Presenters.OOPS)
@DependsOn(@EventsGroup(LayoutEvent.class))
public class OOPSPresenter extends ViewablePresenter<OOPSView> implements OOPSView.OOPSUiHandlers {

    /**
     * {@link OOPSPresenter} がユーザーに表示される前に呼び出されるメソッドです。
     * {@link Storages#ERROR} で定義されたキーを使用して、セッションストレージから保存されたエラーメッセージを
     * チェックします。メッセージが見つかった場合は、ストレージから削除され、
     * {@code view.setMessage(String message)} メソッドを通してビューに渡されます。
     * セッションストレージがサポートされていないか、エラーメッセージが見つからない場合は何も実行されません。
     */
    @OnBeforeReveal
    public void beforeReveal() {
        var storage = Storage.getSessionStorageIfSupported();
        String errorMessage;
        if (storage != null && (errorMessage = storage.getItem(Storages.ERROR)) != null) {
            storage.removeItem(Storages.ERROR);
            view.setMessage(errorMessage);
        }
    }

}
