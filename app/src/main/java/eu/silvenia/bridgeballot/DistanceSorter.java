package eu.silvenia.bridgeballot;

import java.util.Comparator;

import eu.silvenia.bridgeballot.network.Bridge;

/**
 * Created by ricardo van der spek on 6/19/2015.
 */
public class DistanceSorter implements Comparator<Bridge> {
    @Override
    public int compare(Bridge c1, Bridge c2) {
        return Double.compare(c1.getDistance(), c2.getDistance());
    }

}
