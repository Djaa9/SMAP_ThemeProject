/*
package djaa9.dk.thepage.smap_themeproject;

import java.util.ArrayList;
import java.util.HashMap;

public class PolylineEncoder {
    private static int floor1e5(double coordinate) {
        return (int) Math.floor(coordinate * 1e5);
    }


    private static String encodeSignedNumber(int num) {
        int sgn_num = num << 1;
        if (num < 0) {
            sgn_num = ~(sgn_num);
        }
        return(encodeNumber(sgn_num));
    }

    private static String encodeNumber(int num) {
        StringBuffer encodeString = new StringBuffer();

        while (num >= 0x20) {
            encodeString.append((char)((0x20 | (num & 0x1f)) + 63));
            num >>= 5;
        }

        encodeString.append((char)(num + 63));

        return encodeString.toString();

    }

    public static HashMap createEncodings(Track track, int level, int
            step) {

        HashMap resultMap = new HashMap();

        StringBuffer encodedPoints = new StringBuffer();
        StringBuffer encodedLevels = new StringBuffer();

        ArrayList trackpointList = (ArrayList) track.getTrackpoint();

        int plat = 0;
        int plng = 0;
        int counter = 0;

        int listSize = trackpointList.size();

        Trackpoint trackpoint;

        for (int i=0; i < listSize; i += step) {
            counter++;
            trackpoint = (Trackpoint) trackpointList.get(i);

            int late5 = floor1e5(trackpoint.getLatDouble());
            int lnge5 = floor1e5(trackpoint.getLonDouble());

            int dlat = late5 - plat;
            int dlng = lnge5 - plng;

            plat = late5;
            plng = lnge5;


            encodedPoints.append(encodeSignedNumber(dlat)).append(encodeSignedNumber(dlng));
            encodedLevels.append(encodeNumber(level));

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("listSize: " + listSize + " step: " + step + "
                    counter: " + counter);
        }

        resultMap.put("encodedPoints", encodedPoints.toString());
        resultMap.put("encodedLevels", encodedLevels.toString());

        return resultMap;

    }
}
*/
