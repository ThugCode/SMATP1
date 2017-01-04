package Main;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class ImageCutter {

	/***
	 * Get image from folder "img" by title
	 * @param title
	 * @return
	 */
	public static ImageIcon getImageIcon(String title) {
		
		File file = new File("");
		Image source = new ImageIcon(file.getAbsoluteFile()+"/img/"+title).getImage();
		return new ImageIcon(source.getScaledInstance(Common.IMAGE_SIZE, Common.IMAGE_SIZE, Image.SCALE_SMOOTH));
	}

	/***
	 * Cut image 
	 * @param title
	 * @return
	 */
	public static ArrayList<ImageIcon> getListImages(String title) {
		
		File file = new File("");
		Image source = new ImageIcon(file.getAbsoluteFile()+"/img/"+title).getImage();
		source = new ImageIcon(source.getScaledInstance(Common.IMAGE_SIZE, Common.IMAGE_SIZE, Image.SCALE_SMOOTH)).getImage();
		BufferedImage bufferedImage = new BufferedImage(Common.IMAGE_SIZE, Common.IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
		bufferedImage.getGraphics().drawImage(source, 0, 0, null);
		
		BufferedImage piece;
		ArrayList<ImageIcon> list = new ArrayList<ImageIcon>();
		for(int i=0; i<Common.N; i++) {
			for(int j=0; j<Common.N; j++) {
				piece = bufferedImage.getSubimage(j*(Common.IMAGE_SIZE/Common.N), i*(Common.IMAGE_SIZE/Common.N), Common.IMAGE_SIZE/Common.N, Common.IMAGE_SIZE/Common.N);
				list.add(new ImageIcon(piece));
			}
		}
		
		return list;
	}
}
