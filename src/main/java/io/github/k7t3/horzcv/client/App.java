package io.github.k7t3.horzcv.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import gwt.material.design.client.theme.dark.CoreDarkThemeLoader;
import io.github.k7t3.horzcv.client.presenter.StateController;
import io.github.k7t3.horzcv.client.view.NavigationBar;

import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint {

	private static final Logger logger = Logger.getLogger(App.class.getName());

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

//		ThemeLoader.loadAsync(ThemeBrown.INSTANCE, new ThemeLoader.ThemeAsyncCallback() {
//			@Override
//			public void onSuccess(int i) {
//				// Do something when the theme is loaded
//			}
//
//			@Override
//			public void onFailure(Throwable reason) {
//				// Do something when the theme fails to load
//			}
//		});

		logger.info("App started");

		var loader = new CoreDarkThemeLoader();
		loader.load();

		var header = RootPanel.get("header");
		header.add(new NavigationBar());

		var controller = new StateController();
		controller.launch();
	}
}
