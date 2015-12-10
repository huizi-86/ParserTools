
package com.wind.parser;

/**
 * Created by sunhuihui on 2015/11/6. 同步Excel中的SPN/APN各字段的索引与xml中参数index.
 */

public class NameIndexMapping {

    private static NameIndexMapping mNameIndexMapping;

    private int mOperatorIndex;
    private int mMccIndex;
    private int mCarrierIndex;
    private int mApnIndex;
    private int mMncIndex;
    private int mProxyIndex;
    private int mPortIndex;
    private int mApnTypeIndex;
    private int mMmsProxyIndex;
    private int mMmsPortIndex;
    private int mAuthenType;
    private int mPasswordIndex;
    private int mUserIndex;
    private int mMmscIndex;
    private int mServerIndex;
    private int mMvnoTypeIndex;
    private int mMvnoMatchDataIndex;
    private int mMaxIndex;

    private int mSpnIndex;
    private PersistFormat mXMLFormat;

    public static NameIndexMapping getInstance() {
        if (mNameIndexMapping == null)
            mNameIndexMapping = new NameIndexMapping();
        return mNameIndexMapping;
    }

    public int getSpnIndex() {
        return mSpnIndex;
    }

    public void setSpnIndex(int spnIndex) {
        mSpnIndex = spnIndex;
    }

    public void setFormat(PersistFormat format) {
        mXMLFormat = format;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" mMcc=" + mMccIndex +
                " mMnc=" + mMncIndex);
        if (mXMLFormat == PersistFormat.APN) {
            sb.append(" mOperatorIndex=" + mOperatorIndex +
                    ", mMccIndex=" + mMccIndex +
                    ", mCarrierIndex=" + mCarrierIndex +
                    ", mApnIndex=" + mApnIndex +
                    ", mMncIndex=" + mMncIndex +
                    ", mProxyIndex=" + mProxyIndex +
                    ", mPortIndex=" + mPortIndex +
                    ", mApnTypeIndex=" + mApnTypeIndex +
                    ", mMmsProxyIndex=" + mMmsProxyIndex +
                    ", mMmsPortIndex=" + mMmsPortIndex +
                    ", mAuthenType=" + mAuthenType +
                    ", mPasswordIndex=" + mPasswordIndex +
                    ", mUserIndex=" + mUserIndex +
                    ", mMmscIndex=" + mMmscIndex +
                    ", mServerIndex=" + mServerIndex +
                    ", mMvnoTypeIndex=" + mMvnoTypeIndex +
                    ", mMvnoMatchDataIndex=" + mMvnoMatchDataIndex);
        } else {
            sb.append(" spn=" + mSpnIndex
            );
        }

        return "NameIndexMapping{" +
                "mMaxIndex=" + mMaxIndex +
                sb.toString()
                + '}';
    }

    /**
     * 保存每个{@link PersistFormat}各字段在excel中位置
     *
     * @param name
     * @param index
     */
    public void mapNameIndex(String name, int index) {
        if (name.equals("mcc")) {
            setMccIndex(index);
        } else if (name.equals("mnc")) {
            setMncIndex(index);
        }
        if (mXMLFormat == PersistFormat.SPN) {
            if (name.equals("spn")) {
                setSpnIndex(index);
            }
        } else {
            if (name.equals("operator")) {
                setOperatorIndex(index);
            } else if (name.equals("carrier")) {
                setCarrierIndex(index);
            } else if (name.equals("apn")) {
                setApnIndex(index);
            } else if (name.equals("proxy")) {
                setProxyIndex(index);
            } else if (name.equals("port")) {
                setPortIndex(index);
            } else if (name.equals("mms_proxy")) {
                setMmsProxyIndex(index);
            } else if (name.equals("mms_port")) {
                setMmsPortIndex(index);
            } else if (name.equals("mmsc")) {
                setMmscIndex(index);
            } else if (name.equals("server")) {
                setServerIndex(index);
            } else if (name.equals("user")) {
                setUserIndex(index);
            } else if (name.equals("password")) {
                setPasswordIndex(index);
            } else if (name.equals("authenType")) {
                setAuthenTypeIndex(index);
            } else if (name.equals("apnType")) {
                setApnTypeIndex(index);
            } else if (name.equals("mvno_typeype")) {
                setMvnoTypeIndex(index);
            } else if (name.equals("mvno_match_data")) {
                setMvnoMatchDataIndex(index);
            }
        }
    }

    public int getMncIndex() {
        return mMncIndex;
    }

    public void setMncIndex(int mncIndex) {
        mMncIndex = mncIndex;
    }

    public int getApnTypeIndex() {
        return mApnTypeIndex;
    }

    public void setApnTypeIndex(int apnTypeIndex) {
        mApnTypeIndex = apnTypeIndex;
    }

    public int getMmsProxyIndex() {
        return mMmsProxyIndex;
    }

    public void setMmsProxyIndex(int mmsProxyIndex) {
        mMmsProxyIndex = mmsProxyIndex;
    }

    public int getMmsPortIndex() {
        return mMmsPortIndex;
    }

    public void setMmsPortIndex(int mmsPortIndex) {
        mMmsPortIndex = mmsPortIndex;
    }

    public int getAuthenTypeIndex() {
        return mAuthenType;
    }

    public void setAuthenTypeIndex(int authenTicationIndex) {
        mAuthenType = authenTicationIndex;
    }

    public int getPasswordIndex() {
        return mPasswordIndex;
    }

    public void setPasswordIndex(int passwordIndex) {
        mPasswordIndex = passwordIndex;
    }

    public int getUserIndex() {
        return mUserIndex;
    }

    public void setUserIndex(int userIndex) {
        mUserIndex = userIndex;
    }

    private void setOperatorIndex(int operatorIndex) {
        mOperatorIndex = operatorIndex;
    }

    public int getMccIndex() {
        return mMccIndex;
    }

    private void setMccIndex(int mccIndex) {
        mMccIndex = mccIndex;
    }

    public int getCarrierIndex() {
        return mCarrierIndex;
    }

    private void setCarrierIndex(int carrierIndex) {
        mCarrierIndex = carrierIndex;
    }

    public int getApnIndex() {
        return mApnIndex;
    }

    private void setApnIndex(int apnIndex) {
        mApnIndex = apnIndex;
    }

    public int getProxyIndex() {
        return mProxyIndex;
    }

    public void setProxyIndex(int proxyIndex) {
        mProxyIndex = proxyIndex;
    }

    public int getPortIndex() {
        return mPortIndex;
    }

    public void setPortIndex(int portIndex) {
        mPortIndex = portIndex;
    }

    public int getMmscIndex() {
        return mMmscIndex;
    }

    public void setMmscIndex(int mmscIndex) {
        mMmscIndex = mmscIndex;
    }

    public int getServerIndex() {
        return mServerIndex;
    }

    public void setServerIndex(int serverIndex) {
        mServerIndex = serverIndex;
    }

    public int getMvnoTypeIndex() {
        return mMvnoTypeIndex;
    }

    public void setMvnoTypeIndex(int mvnoTypeIndex) {
        mMvnoTypeIndex = mvnoTypeIndex;
    }

    public int getMvnoMatchDataIndex() {
        return mMvnoMatchDataIndex;
    }

    public void setMvnoMatchDataIndex(int mvnoMatchDataIndex) {
        mMvnoMatchDataIndex = mvnoMatchDataIndex;
    }

    public int getMaxIndex() {
        return mMaxIndex;
    }

    public void setMaxIndex(int maxIndex) {
        mMaxIndex = maxIndex;
    }
}
