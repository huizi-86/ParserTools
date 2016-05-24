package com.wind.parser;

import org.xml.sax.helpers.AttributesImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sunhuihui on 2015/11/12. SPN或APN对象的基类，抽象出了{@link #bindInfo}
 * {@link #addPersist(PersistAttribute)}
 */
public abstract class PersistObjectBase implements Cloneable {

    public int mRow; // help to debug error.
    /**
     * 可以在数组中自己添加更多的分割符
     */
    private static final String SPLIT_FLAG = ParserUtils.SPLIT_FLAG;
    private static final String TAG = "PersistObjectBase";
    protected NameIndexMapping mNameIndexMapping;
    protected String mMnc, mMcc;
    /**
     * 将SPN或是APN中的所有属性都存为一个List集合，在保存时循环写入XML
     */
    protected ArrayList<PersistAttribute> mAllAttributeSaved = new ArrayList<>();

    public PersistObjectBase() {
        mNameIndexMapping = NameIndexMapping.getInstance();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.getClass().getSimpleName());
        sb.append("{");
        for (PersistAttribute pas : mAllAttributeSaved) {
            String value = pas.value;
            if (value != null && !value.equals("")) {
                sb.append(pas.tag + ":" + pas.value + " ,");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 根据索引值对每个APN或SPN中所有的字段排序.
     */
    public void sortAllAttribute() {
        Collections.sort(mAllAttributeSaved);
    }

    /**
     * 对于SPN和APN绑定方式不一定，分别实现，绑定信息后， 把每个字段保存{@link #addPersist}
     * {@link #mAllAttributeSaved}
     *
     * @param index
     * @param text
     */
    protected abstract void bindInfo(int index, String text);

    /**
     * 在保存最后的XML时，只需要把{@link #mAllAttributeSaved}遍例即可
     *
     * @param abi
     */
    protected final void bindAttribute(AttributesImpl abi) {
        for (PersistAttribute persistApn : mAllAttributeSaved) {
            String value = persistApn.value;
            abi.addAttribute(null, null, persistApn.tag, null, value);
        }
    }

    /**
     * 参考{@link #bindInfo}
     *
     * @param attribute
     */
    protected final void addPersist(PersistAttribute attribute) {
        mAllAttributeSaved.add(attribute);
        if (attribute.index == mNameIndexMapping.getMncIndex()) {
            mMnc = attribute.value;
        } else if (attribute.index == mNameIndexMapping.getMccIndex()) {
            mMcc = attribute.value;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PersistObjectBase that = (PersistObjectBase) o;

        return mAllAttributeSaved.equals(that.mAllAttributeSaved);

    }

    @Override
    public int hashCode() {
        return mAllAttributeSaved.hashCode();
    }

    public String getMnc() {
        return mMnc;
    }

    public void setMnc(String mnc) {
        mAllAttributeSaved.stream().filter(ap -> ap.index == mNameIndexMapping.getMncIndex())
                .forEach(ap -> ap.value = mnc);
        mMnc = mnc;
    }

    public String getMcc() {
        return mMcc;
    }

    public void setMcc(String mcc) {
        mAllAttributeSaved.stream().filter(ap -> ap.index == mNameIndexMapping.getMccIndex())
                .forEach(ap -> ap.value = mcc);
        mMcc = mcc;
    }

    /**
     * 判断MCC或是MNC是否需要分割为多个
     *
     * @return
     */
    public boolean isNeedSplit() {
        return containSeparate(mMnc) || containSeparate(mMcc);
    }

    /**
     * 判断内容是否需要分割，主要是考虑到多mcc与mnc的分割. 目前分割方式主要是:{@link #SPLIT_FLAG}，可以在数组中自己添加
     *
     * @param text
     * @return
     */
    private boolean containSeparate(String text) {
        // These try blockes are for debug.
        try {
            for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);
                for (int j = 0; j < SPLIT_FLAG.length(); j++) {
                    if (SPLIT_FLAG.charAt(j) == ch)
                        return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("row="+mRow);
        }
        return false;
    }

    /**
     * First step split it by mcc, next split by mnc.
     *
     * @param tempPob Split result saved here.
     * @throws CloneNotSupportedException
     */
    private void splitByMcc(List<PersistObjectBase> tempPob) throws CloneNotSupportedException {
        List<PersistObjectBase> temp = new ArrayList<>();
        String[] mccString = mMcc.split(SPLIT_FLAG);
        for (String mcc : mccString) {
            PersistObjectBase newApn = this.clone();
            newApn.setMcc(mcc);
            temp.add(newApn);
        }
        // Split it again for the tempApns by mnc.
        if (containSeparate(mMnc)) {
            for (PersistObjectBase pob : temp) {
                String[] mncStrings = pob.getMnc().split(SPLIT_FLAG);
                for (String mnc : mncStrings) {
                    PersistObjectBase newApn = pob.clone();
                    newApn.setMnc(mnc);
                    tempPob.add(newApn);
                }
            }
        } else {
            tempPob.addAll(temp);
        }
    }

    /**
     * @param tempPob Split result saved here.
     */
    private void splitByMnc(List<PersistObjectBase> tempPob) throws CloneNotSupportedException {
        String[] mncStrings = mMnc.split(SPLIT_FLAG);
        for (String mnc : mncStrings) {
            PersistObjectBase newApn = this.clone();
            newApn.setMnc(mnc);
            tempPob.add(newApn);
        }
    }

    public List<PersistObjectBase> split() {
        List<PersistObjectBase> tempPob = new ArrayList<>();
        try {
            // split by mcc first
            if (containSeparate(mMcc)) {
                splitByMcc(tempPob);
            } else {
                // Only split by mnc.
                splitByMnc(tempPob);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return tempPob;
    }

    @Override
    protected PersistObjectBase clone() throws CloneNotSupportedException {
        PersistObjectBase pb = (PersistObjectBase) super.clone();
        // 对于list中内容需要深clone
        pb.mAllAttributeSaved = new ArrayList<>();
        for (PersistAttribute pa : mAllAttributeSaved)
            pb.mAllAttributeSaved.add(pa.clone());
        return pb;
    }

    /**
     * {@link #mMcc} {@link #mMnc} must be not null.
     * And It must be formatted to integer.
     *
     * @return
     */
    public boolean isValid() {
        if (ParserUtils.isInvalidChar(mMcc) || ParserUtils.isInvalidChar(mMnc)) {
            ParserUtils.log(TAG, "mcc mnc must not contain null.");
            return false;
        } else {
            // Check whether it can be parse to integer.
            try {
                Integer.parseInt(mMcc);
                Integer.parseInt(mMnc);
            } catch (NumberFormatException e) {
                ParserUtils.log(TAG, "mcc or mnc must be number <" + mMcc + " , " + mMnc + ">");
                return false;
            }
        }
        return true;
    }
}
