package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import io.github.k7t3.horzcv.client.view.Routes;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.history.StateToken;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RPCの基本的なコールバックインターフェース。
 * <p>
 *     RPC呼び出しが正常に実行できなかったとき、接続エラーのページへ移動する。
 * </p>
 * <p>
 *     互換性のないRPC呼び出しを行ったとき、ページは自動的にリロードされる。
 * </p>
 * @param <T> RPC呼び出しのタイプ
 */
@FunctionalInterface
public interface BasicAsyncCallback<T> extends AsyncCallback<T> {

    Logger LOGGER = Logger.getLogger(BasicAsyncCallback.class.getName());

    static <T> BasicAsyncCallback<T> of(Consumer<T> callback) {
        return callback::accept;
    }

    /**
     * コールバックを変換するメソッド
     * @param before 変換する関数
     */
    default <R> BasicAsyncCallback<R> compose(Function<R, T> before) {
        return new BasicAsyncCallback<>() {
            @Override
            public void onSuccess(R result) {
                BasicAsyncCallback.this.onSuccess(before.apply(result));
            }

            @Override
            public void onFailure(Throwable caught) {
                BasicAsyncCallback.this.onFailure(caught);
            }
        };
    }

    @Override
    default void onFailure(Throwable caught) {
        if (caught instanceof InvocationException) {
            // 正常に実行できなかった場合にスローされる
            // 1. サーバーへのネットワーク接続が利用できない
            // 2. ホストWebサーバーが利用できない
            // 3. サーバーが利用できない
            //
            // サーバー内で適切にハンドリングされない例外が発生すると
            // 内部エラー的な使われ方もするらしいのでRPCの実装時は気を付ける
            
            var errorMessage = "Failed to connect to the server: " + caught.getMessage();

            LOGGER.severe(errorMessage);
            
            // セッションストレージが有効な場合はエラーメッセージを保存
            var storage = Storage.getSessionStorageIfSupported();
            if (storage != null) {
                storage.setItem(Storages.ERROR, errorMessage);
            }

            // エラーページに遷移
            var history = ClientApp.make().getHistory();
            history.fireState(StateToken.of(Routes.OOPS));

            return;
        }

        if (caught instanceof IncompatibleRemoteServiceException) {
            // 意図していたサービスとは異なっていたとき
            // デプロイされるサービスが更新されたタイミングとか
            // クライアントを更新するためにページをリロードする。

            LOGGER.warning("Incompatible remote service: " + caught.getMessage());

            Window.alert("The application has been updated. The page will now reload.");
            Window.Location.reload();

            return;
        }

        // 意図していない例外のとき
        LOGGER.log(Level.SEVERE, "Unexpected error: " + caught.getMessage(), caught);
    }

}
