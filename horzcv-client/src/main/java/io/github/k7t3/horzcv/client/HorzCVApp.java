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

package io.github.k7t3.horzcv.client;

import com.google.gwt.core.client.EntryPoint;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.annotations.ClientModule;
import org.dominokit.domino.gwt.client.app.DominoGWT;
import org.dominokit.domino.view.DominoViewOptions;

import java.util.logging.Logger;

@ClientModule(name = "HorzCV")
public class HorzCVApp implements EntryPoint {

	private static final Logger LOGGER = Logger.getLogger(HorzCVApp.class.getName());

	public void onModuleLoad() {
		var options = DominoViewOptions.getInstance();
		options.setRootPath(""); // 実行環境と合わせる
		DominoGWT.init(options);

		var app = ClientApp.make();

		// アノテーションプロセッサが生成する本モジュールのを登録して実行する
		app.configureModule(new HorzCVModuleConfiguration());
		app.run();

		LOGGER.info("App started");
	}

}
