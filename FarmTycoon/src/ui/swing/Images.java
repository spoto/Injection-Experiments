package ui.swing;

import java.awt.Dimension;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Images {
	private static Map<String, Image> map = new HashMap<String, Image>();
	private static Map<String, Dimension> cachedim = new HashMap<String, Dimension>();
	private static Map<String, Image> cache = new HashMap<String, Image>();

	public static Image getImage(String name) {
		if (map.containsKey(name))
			return map.get(name);
		Image img;
		try {
			img = ImageIO.read(Images.class.getClassLoader().getResource(
					"ui/swing/images/tiles/" + name + ".png"));
		} catch (Exception e) {
			try {
				img = ImageIO.read(Images.class.getClassLoader().getResource(
						"ui/swing/images/tiles/NONE.png"));
			} catch (Exception ee) {
				img = null;
			}
		}
		map.put(name, img);
		return img;
	}
	public static Image getImage(String name, Dimension dim){
		if (cachedim.containsKey(name))
			if (dim.equals(cachedim.get(name)))
				return cache.get(name);
		if (dim.width == 0 || dim.height == 0)
			return null;
		if (!map.containsKey(name))
			getImage(name);
		cache.put(name, map.get(name).getScaledInstance(dim.width, dim.height, Image.SCALE_FAST));
		cachedim.put(name, dim);
		return cache.get(name);
	}

}
