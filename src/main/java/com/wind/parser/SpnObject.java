package com.wind.parser;

import java.util.Iterator;

/**
 * Created by sunhuihui on 2015/11/12.
 */
public class SpnObject extends PersistObjectBase {

    SpnObject() {
    }

    @Override
    protected void bindInfo(int index, String text) {
        NameIndexMapping map = mNameIndexMapping;

        if (index == map.getMccIndex()) {
            mMcc = text;
            addPersist(new PersistAttribute(index, "mcc", mMcc));
        } else if (index == map.getMncIndex()) {
            mMnc = text;
            addPersist(new PersistAttribute(index, "mnc", mMnc));
        } else if (index == map.getSpnIndex()) {
            addPersist(new PersistAttribute(index, "spn", text.replaceAll("\\s*", "")));
        }
    }

    /**
     * PLMN,MCC MNC是分开存储的，结果需要把mcc mnc合并为一个numeric，
     * 在序列化XML时，再将Mccmnc合并。
     */
    protected void mergerMccMnc() {
        //merge mcc and mcc to numeric
        Iterator<PersistAttribute> iterator = mAllAttributeSaved.iterator();
        String mcc = null;
        String mnc = null;
        while (iterator.hasNext()) {
            PersistAttribute pa = iterator.next();
            if (pa.tag.equals("mcc")) {
                mcc = pa.value;
                iterator.remove();
            } else if (pa.tag.equals("mnc")) {

                mnc = pa.value;
                iterator.remove();
            }
        }
        mAllAttributeSaved.add(new PersistAttribute(0, "numeric", mcc + mnc));
    }
}