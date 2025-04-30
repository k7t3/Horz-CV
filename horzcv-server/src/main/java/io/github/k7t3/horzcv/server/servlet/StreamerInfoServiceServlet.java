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

package io.github.k7t3.horzcv.server.servlet;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import io.github.k7t3.horzcv.server.services.StreamerFinders;
import io.github.k7t3.horzcv.shared.model.StreamerInfoResponse;
import io.github.k7t3.horzcv.shared.service.StreamerInfoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/horzcv/api/streamer")
@ApplicationScoped
public class StreamerInfoServiceServlet extends RemoteServiceServlet implements StreamerInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamerInfoServiceServlet.class);

    @Inject
    private StreamerFinders finders;

    /**
     * コンストラクタ
     */
    public StreamerInfoServiceServlet() {
        LOGGER.info("{} が初期化されました", getClass().getSimpleName());
    }

    @Override
    public StreamerInfoResponse getStreamerInfo(String url) {
        LOGGER.info("ストリーマー情報のリクエストを受信: {}", url);
        try {
            long startTime = System.currentTimeMillis();
            StreamerInfoResponse response = finders.find(url);
            long processingTime = System.currentTimeMillis() - startTime;

            LOGGER.info("ストリーマー情報の取得完了 - URL: {}, 識別: {}, 処理時間: {}ms", url, response.isIdentified(), processingTime);
            return response;
        } catch (Exception e) {
            LOGGER.error("ストリーマー情報の取得中にエラーが発生しました - URL: {}", url, e);
            throw e;
        }
    }

}
