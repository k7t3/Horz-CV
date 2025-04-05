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
import org.dominokit.domino.api.shared.extension.PredefinedSlots;
import org.dominokit.domino.gwt.client.app.DominoGWT;
import org.dominokit.domino.view.DominoViewOptions;
import org.dominokit.domino.view.slots.BodyElementSlot;

import java.util.logging.Logger;

@ClientModule(name = "HorzCV")
public class HorzCVApp implements EntryPoint {

	private static final Logger logger = Logger.getLogger(HorzCVApp.class.getName());

	public void onModuleLoad() {
		var options = DominoViewOptions.getInstance();
		DominoGWT.init(options);

		var app = ClientApp.make();

		// アノテーションプロセッサが生成したプレゼンターの実装をロードする設定を登録
		app.configureModule(new HorzCVModuleConfiguration());

		// Shellを割り当てるスロットの登録
		app.slotsManager().registerSlot(PredefinedSlots.BODY_SLOT, BodyElementSlot.create());

		app.run();

		logger.info("App started");
	}

}
