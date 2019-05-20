package model.data_structures;
import java.util.Comparator;

import model.vo.VOIntersections;
public class ComparadorXAccidentes implements Comparator<VOIntersections> {

	@Override
	public int compare(VOIntersections pV1, VOIntersections pV2) {

		Double primera = pV1.getAcomulado();
		Double segunda = pV2.getAcomulado();

		return (int) (primera - segunda);
	}

}
