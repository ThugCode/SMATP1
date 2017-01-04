package Main;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class ImageCutter {

	public static ImageIcon getImageIcon(String title) {
		
		File file = new File("");
		Image source = new ImageIcon(file.getAbsoluteFile()+"/img/"+title).getImage();
		return new ImageIcon(source.getScaledInstance(500, 500, Image.SCALE_SMOOTH));
	}

	public static ArrayList<ImageIcon> getListImages(String title) {
		
		File file = new File("");
		Image source = new ImageIcon(file.getAbsoluteFile()+"/img/"+title).getImage();
		source = new ImageIcon(source.getScaledInstance(500, 500, Image.SCALE_SMOOTH)).getImage();
		BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		bufferedImage.getGraphics().drawImage(source, 0, 0, null);
		
		BufferedImage piece;
		ArrayList<ImageIcon> list = new ArrayList<ImageIcon>();
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				piece = bufferedImage.getSubimage(j*100, i*100, 100, 100);
				list.add(new ImageIcon(piece));
			}
		}
		
		return list;
	}
}
