import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class MainController {
	public int count = 0;
	public void chooseFile(ActionEvent event) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose an image with Faces!");		
		fc.setInitialDirectory(new File("H:\\Eclipse Workspace\\JFaceDetection\\Images"));
		File inputImg = fc.showOpenDialog(null);
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		String imgFile = inputImg.getPath();
		
		Mat src = Imgcodecs.imread(imgFile);
		
		String xmlFile = "XMLs/lbpcascade_frontalface.xml";
		CascadeClassifier cc = new CascadeClassifier(xmlFile);
		
		MatOfRect faceDetection = new MatOfRect();
		cc.detectMultiScale(src, faceDetection);
		
		for(Rect rect: faceDetection.toArray()) {
			Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height) , new Scalar(0, 0, 255), 3);
		}
		
		Imgcodecs.imwrite("Images/success.jpg", src);
		System.out.println("Image Detection Finished");
		System.out.println(String.format("Detected faces: %d", faceDetection.toArray().length));	
		System.out.println("Hi");
		count = faceDetection.toArray().length;
	}
	
	public void output(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Output.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = new Stage();
			primaryStage.setTitle("Detecting Faces!");
			primaryStage.setScene(scene);
			
			Label label = new Label("Detected faces :: "+ count); 
			Popup popup = new Popup(); 
			label.setStyle(" -fx-background-color: white;"); 
			popup.getContent().add(label); 
			label.setMinWidth(80); 
			label.setMinHeight(50); 
			popup.setAutoHide(true);
			popup.show(primaryStage);

			primaryStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void exit(ActionEvent event) {
		System.exit(-1);
	}
}
