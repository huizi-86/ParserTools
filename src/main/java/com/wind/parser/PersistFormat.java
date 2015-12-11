
package com.wind.parser;

/**
 * Created by Alen on 2015/11/27.
 */
public enum PersistFormat {
    /**
     * Now apn 's version is 1.0.
     * spn's version is 8.
     */
    SPN(8), APN(1);
    private int mVersion;

    PersistFormat(int version) {
        mVersion = version;
    }

    public String getVersion() {
        return String.valueOf(mVersion);
    }
}
