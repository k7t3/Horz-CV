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

package io.github.k7t3.horzcv.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import io.github.k7t3.horzcv.shared.model.StreamerInfoResponse;

/**
 * 入力されたURLから、ストリーマーの情報を取得するサービス。
 */
@RemoteServiceRelativePath("api/streamer")
public interface StreamerInfoService extends RemoteService {

    /**
     * 入力されたURLからストリーマーの情報を取得するメソッド。
     * @param url 入力されたURL
     * @return ストリーマーの情報
     * @implSpec パラメータが空もしくは<code>null</code>のときは
     *           {@link StreamerInfoResponse#EMPTY}を返す。
     */
    StreamerInfoResponse getStreamerInfo(String url);

}
