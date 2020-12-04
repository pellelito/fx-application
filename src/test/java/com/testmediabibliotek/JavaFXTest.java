package com.testmediabibliotek;

import org.junit.Before;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sourceforge.marathon.javafxagent.Wait;

public abstract class JavaFXTest {

	public static class ApplicationHelper extends Application {

		public static void startApplication() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Application.launch(ApplicationHelper.class);
				}
			}).start();
		}

		private Stage primaryStage;

		@Override
		public void start(Stage primaryStage) throws Exception {
			this.primaryStage = primaryStage;
			JavaFXTest.applicationHelper = this;
		}

		public void startGUI(Scene scene, Pane pane) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					primaryStage.hide();
					if (scene == null)
						primaryStage.setScene(new Scene(pane));
					else
						primaryStage.setScene(scene);
					primaryStage.sizeToScene();
					primaryStage.show();
				}
			});
			new Wait("Waiting for applicationHelper to be initialized") {
				@Override
				public boolean until() {
					try {
						if (scene == null)
							return primaryStage.getScene().getRoot() == pane;
						else
							return primaryStage.getScene() == scene;
					} catch (Throwable t) {
						return false;
					}
				}
			};
		}

		public Stage getPrimaryStage() {
			return primaryStage;
		}
	}

	private static ApplicationHelper applicationHelper;

	public JavaFXTest() {
	}

	@Before
	public void startGUI() throws Throwable {
		if (applicationHelper == null) {
			ApplicationHelper.startApplication();
		}
		new Wait("Waiting for applicationHelper to be initialized") {
			@Override
			public boolean until() {
				return applicationHelper != null;
			}
		};
		if (applicationHelper == null) {
			throw new RuntimeException("Application Helper = null");
		}
		Scene scene = getScene();
		Pane pane = getMainPane();
		if (scene == null && pane == null) {
			throw new RuntimeException("Should override one of getMainPane() or getScene()");
		}
		if (scene != null && pane != null) {
			throw new RuntimeException("Should override only one of getMainPane() or getScene()");
		}
		applicationHelper.startGUI(scene, pane);
		new Wait() {
			@Override
			public boolean until() {
				return applicationHelper.getPrimaryStage().isShowing();
			}
		}.wait("Waiting for the primary stage to be displayed.", 10000);
	}

	protected Pane getMainPane() {
		return null;
	}

	protected Scene getScene() {
		return null;

	}

	public Stage getPrimaryStage() {
		return applicationHelper.getPrimaryStage();
	}

}
