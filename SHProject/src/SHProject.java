import model.DataModel;
import view.MainFrame;


public class SHProject {

	private static MainFrame mainFrame;
	private static DataModel dataModel;
	
	public static void main(String[] args) {
		dataModel = new DataModel();
		dataModel.init();
		mainFrame = new MainFrame(dataModel);
		mainFrame.init();
	}
	
}
