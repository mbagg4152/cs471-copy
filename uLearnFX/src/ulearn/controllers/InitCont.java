package ulearn.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ulearn.utils.JsonReader;

import javax.swing.*;
import java.io.IOException;

import static javafx.fxml.FXMLLoader.load;
import ulearn.utils.UTypeHolder;

public class InitCont {
	final String T_STU = "student";
	final String T_ADM = "admin";
	final String T_EDU = "educator";
	// initial width & height of windows
	final int INIT_WD = 800;
	final int INIT_HT = 450;
	public UTypeHolder holder;
	// for login
	public Label title = new Label();
	public Label unameLabel = new Label();
	public Label pwdLabel = new Label();
	public TextField unameBox = new TextField();
	public PasswordField pwdBox = new PasswordField();
	public Button loginBtn = new Button();

	// for login_popup
	public Label uNameInfo = new Label();
	public Label pwdInfo = new Label();
	public Label dispUName = new Label();
	public Label dispPwd = new Label();
	public Button goHomeBtn = new Button();

	String layoutPath = "";

	@FXML private void loginBtnAction(ActionEvent event) throws IOException {
		event.consume();
		String id = unameBox.getText();
		holder = new UTypeHolder();
		if (verifyUser(id, pwdBox.getText())) {
			String path = determineLayout(id);
			setupOnClick(path, loginBtn, INIT_WD, INIT_HT, true);
		} else {
			try { // change look & feel for error pop up
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Incorrect Username or Password");
		}
	}

	private Boolean verifyUser(String username, String password) {
		// Compare pass against username pass
		JsonReader database = new JsonReader();
		database.readJson();
		return password.equals(database.getSingleItem("users", "userId", username, "password"));
	}

	String determineLayout(String userId) {
		JsonReader database = new JsonReader();
		String userType = database.getSingleItem("users", "userId", userId, "userType");
		switch (userType) {
			case T_ADM:
				layoutPath = "../layout/main_screen_admin.fxml";
				break;
			case T_EDU:
				layoutPath = "../layout/main_screen_educator.fxml";
				break;
			case T_STU:
				layoutPath = "../layout/main_screen_student.fxml";
				break;
			default:
				layoutPath = "../layout/main_screen_all.fxml";
		}
		return layoutPath;

	}

	void setupOnClick(String layout, Button btn, int width, int height, boolean resize) throws IOException {
		Parent par = FXMLLoader.load(getClass().getResource(layout));
		Stage stage = (Stage) btn.getScene().getWindow();
		Scene scene = new Scene(par, width, height);
		scene.setFill(null);
		stage.setScene(scene);
		stage.setResizable(resize);
		stage.show();
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
		stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 4);
	}
}
