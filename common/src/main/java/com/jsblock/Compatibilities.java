package com.jsblock;

import java.util.Objects;

/**
 * Check MTR Version compatibilities
 * @author LX86
 * @since 1.1.4
 */
public class Compatibilities {

    /**
     * If MTR Version meets the minimum requirement for this mod to run
     * @param ogMinVersion Minimum MTR version
     * @param ogCurVersion Current MTR version
     * @since 1.1.4
     * @return true: Version lower than the min requirement<br>
     * false: Meets the min requirement
     */
    public static boolean lowerThanMin(String ogMinVersion, String ogCurVersion) {
        /* Can't determine if version is unavail */
        if(ogCurVersion == null || ogMinVersion == null) return false;

        String minVersion = ogMinVersion.split("-")[0];
        String curVersion = ogCurVersion.split("-")[0];

        /* Same version */
        if (Objects.equals(minVersion, curVersion)) {
            return false;
        }

        String[] splittedA = minVersion.split("\\.");
        String[] splittedB = curVersion.split("\\.");

        if (splittedA.length != splittedB.length) {
            return false;
        }

        for (int i = 0; i < splittedA.length; i++) {
            int min = Integer.parseInt(splittedA[i]);
            int cur = Integer.parseInt(splittedB[i]);
            if(min < cur) {
                return false;
            } else if (min > cur) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method sends out an error message to the console alerting them this version is not compatible.<br>
     * @param curVer Current MTR Version
     * @param minVer Minimum MTR Version
     */
    protected static void incompatible(String curVer, String minVer) {
        Joban.LOGGER.fatal("[Joban Client] This version of JCM is incompatible with MTR " + curVer + ".");
        Joban.LOGGER.fatal("[Joban Client] Please install MTR " + minVer + " and try again.");
        Joban.LOGGER.fatal("");
    }
}
