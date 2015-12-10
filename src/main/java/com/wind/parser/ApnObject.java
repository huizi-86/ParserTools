

package com.wind.parser;

/**
 * Created by sunhuihui on 2015/11/12.
 */
public class ApnObject extends PersistObjectBase {

    ApnObject() {
    }

    // None:0, PAP:1 CHAP:2 PAP_CHAP:3
    private String formatAuthType(String content) {
        if ("PAP/CHAP".equals(content)) {
            content = "3";
        } else if ("PAP".equals(content)) {
            content = "1";
        } else if ("CHAP".equals(content)) {
            content = "2";
        } else {
            content = "0";
        }
        return content;
    }

    @Override
    protected void bindInfo(int index, String text) {
        // Apn type default value is "default".
        // *, mms,default,wap, supl
        NameIndexMapping map = mNameIndexMapping;
        if (index == map.getApnTypeIndex() && ParserUtils.isInvalidChar(text)) {
            text = "default";
        } else if (index == map.getAuthenTypeIndex()) {
            // authenType default is 0.
            text = formatAuthType(text);
        } else if (ParserUtils.isInvalidChar(text)) {
            return;
        }

        text = text.replaceAll("&", "&amp;");

        if (index == map.getCarrierIndex()) {
            addPersist(new PersistAttribute(index, "carrier", text));
        } else if (index == map.getApnIndex()) {
            addPersist(new PersistAttribute(index, "apn", text));
        } else if (index == map.getMccIndex()) {
            addPersist(new PersistAttribute(index, "mcc", text));
        } else if (index == map.getMncIndex()) {
            addPersist(new PersistAttribute(index, "mnc", text));
        } else if (index == map.getProxyIndex()) {
            addPersist(new PersistAttribute(index, "proxy", text));
        } else if (index == map.getServerIndex()) {
            addPersist(new PersistAttribute(index, "server", text));
        } else if (index == map.getPortIndex()) {
            addPersist(new PersistAttribute(index, "port", text));
        } else if (index == map.getMmsProxyIndex()) {
            addPersist(new PersistAttribute(index, "mmsproxy", text));
        } else if (index == map.getMmsPortIndex()) {
            addPersist(new PersistAttribute(index, "mmsport", text));
        } else if (index == map.getMmscIndex()) {
            addPersist(new PersistAttribute(index, "mmsc", text));
        } else if (index == map.getUserIndex()) {
            addPersist(new PersistAttribute(index, "user", text));
        } else if (index == map.getPasswordIndex()) {
            addPersist(new PersistAttribute(index, "password", text));
        } else if (index == map.getAuthenTypeIndex()) {
            addPersist(new PersistAttribute(index, "authtype", text));
        } else if (index == map.getApnTypeIndex()) {
            // remove all space in text.
            addPersist(new PersistAttribute(index, "type", text.replaceAll("\\s*", "")));
        }
        // mvno
        else if (index == map.getMvnoTypeIndex()) {
            addPersist(new PersistAttribute(index, "mvno_type", text));
        } else if (index == map.getMvnoMatchDataIndex()) {
            addPersist(new PersistAttribute(index, "mvno_match_data", text));
        }
    }
}