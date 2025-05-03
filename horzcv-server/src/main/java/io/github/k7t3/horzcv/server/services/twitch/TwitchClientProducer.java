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

package io.github.k7t3.horzcv.server.services.twitch;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class TwitchClientProducer {

    private static final String CLIENT_ID = "twitch_client_id";
    private static final String ACCESS_TOKEN = "twitch_client_secret";

    private TwitchClient client;

    @Produces
    public TwitchClient getClient() {
        if (client == null) {
            client = TwitchClientBuilder.builder()
                    .withClientId(System.getenv(CLIENT_ID))
                    .withClientSecret(System.getenv(ACCESS_TOKEN))
                    .withEnableHelix(true)
                    .build();
        }
        return client;
    }

}
