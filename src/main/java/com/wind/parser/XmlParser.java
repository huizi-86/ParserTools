
package com.wind.parser;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import jxl.Cell;
import jxl.Sheet;

/**
 * Created by sunhuihui on 2015/11/12. 1.解析excel中各单元格的数据并存为
 * {@link PersistObjectBase} 2.保存{@link PersistObjectBase}所有信息到XML中。
 */
public class XmlParser {
    private static final String XMLPATH = "excel_prop.xml";
    NameIndexMapping mNameIndexMapping;
    /**
     * 资源目录，存储xml以及excel文件。
     */
    private String MODULE_RESOURCE_DIR;
    private String mOutDir;
    private String newLine = "\n";
    private String TAG = "XmlParser";
    private String mOutFile;
    private String mSrcFile;
    /**
     * {@link PersistFormat}
     */
    private PersistFormat mPersistFormat;
    private String mRootElement, mChildElement;

    public XmlParser() {
        mNameIndexMapping = NameIndexMapping.getInstance();
    }

    /**
     * 自定义输出目录,否则存储到{@link #MODULE_RESOURCE_DIR}
     *
     * @param outdir the space saved output file.
     */
    public void setOutDir(String outdir) {
        this.mOutDir = outdir;
    }

    public File getSrcFile() {
        return new File(MODULE_RESOURCE_DIR, mSrcFile);
    }

    /**
     * 初始化输出格式及xml中对应的节点{@link #mRootElement},{@link #mChildElement}.
     * {@link #mSrcFile}{@link #mOutFile}
     *
     * @param format {@link PersistFormat#APN} {@link PersistFormat#SPN}
     */
    public void initParameters(PersistFormat format) {
        // init resource dir
        MODULE_RESOURCE_DIR = System.getProperty("user.dir") + "/ParserTools/src/main/resources";

        ParserUtils.log(TAG, "MODULE_RESOURCE_DIR = " + MODULE_RESOURCE_DIR);
        mPersistFormat = format;
        mNameIndexMapping.setFormat(mPersistFormat);
        switch (format) {
            case APN:
                mSrcFile = "APN.xls";
                mOutFile = "apns-conf.xml";
                mRootElement = "apns";
                mChildElement = "apn";
                break;
            case SPN:
                mSrcFile = "PLMN.xls";
                mOutFile = "spns_conf.xml";
                mRootElement = "spnOverrides";
                mChildElement = "spnOverride";
                break;
        }
    }

    public File getOutFile() {
        return new File(mOutDir == null ? MODULE_RESOURCE_DIR : mOutDir, mOutFile);
    }

    private File getXmlFile() {
        File srcFile = new File(MODULE_RESOURCE_DIR, XMLPATH);
        ParserUtils.log(TAG, "getXmlFile=" + srcFile.getAbsolutePath());
        return srcFile;
    }

    public void parseIndex() throws ParserConfigurationException, SAXException, IOException {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(getXmlFile(), new SaxHandler());
    }

    /**
     * 根据{@link #mPersistFormat} 来决定使用哪种类型{@link PersistObjectBase}
     *
     * @return Create the object by {@link #mPersistFormat}
     */
    private PersistObjectBase getPersistObject() throws UnSpecifiedFormatException {
        switch (mPersistFormat) {
            case APN:
                return new ApnObject();
            case SPN:
                return new SpnObject();
            default:
                throw new UnSpecifiedFormatException("the mPersistFormat is unspecify");
        }
    }

    /**
     * 将Cell中的数据解析存储到集合中，核心方法
     *
     * @param sheet the data source saved spn or apn with excel.
     * @return the collection contain all objects build from {@param sheet}.
     */
    public Collection<PersistObjectBase> parseXml(Sheet sheet) throws UnSpecifiedFormatException {
        int allRows = sheet.getRows();
        // allRows = 6;
        // The purpose using hashset is for removing the duplicate object.
        HashSet<PersistObjectBase> objects = new HashSet<>(allRows);
        for (int i = 1; i < allRows; i++) {
            // All cells at a row.
            Cell[] rows_cells = sheet.getRow(i);
            PersistObjectBase pb = getPersistObject();
            pb.mRow = i;
            for (Cell c : rows_cells) {
                int columnIndex = c.getColumn();
                // Break this row if more than max index.
                if (columnIndex > mNameIndexMapping.getMaxIndex()) {
                    break;
                }
                String content = c.getContents();

                content = ParserUtils.filterInvalidChars(content);
                pb.bindInfo(columnIndex, content);
            }
            pb.sortAllAttribute();

            // Mnc contains multi mnc. Split it.
            if (pb.isNeedSplit()) {
                objects.addAll(pb.split());
            } else {
                // add new apn into list.
                objects.add(pb);
            }
        }
        return objects;
    }

    @Deprecated
    private void newLine(TransformerHandler th) {
        try {
            th.characters(newLine.toCharArray(), 0, newLine.length());
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把｛@link PersistObjectBase}对象序列化为XML格式。
     *
     * @param fos the output stream for out file
     * @param pobs the collection contained the persisted object.
     */
    public void buildOutXml(FileOutputStream fos, Collection<? extends PersistObjectBase> pobs)
            throws SAXException {
        Result resultXml = new StreamResult(fos); // xml
        SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        try {
            factory.setAttribute(TransformerFactoryImpl.INDENT_NUMBER, 10);
            TransformerHandler th = factory.newTransformerHandler();

            // Set format for XML.
            Transformer transformer = th.getTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); // 编码格式是UTF-8
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // 换行
            transformer.setOutputProperty(OutputKeys.METHOD,"xml");

            /*
              It must be called after setOutputProperty.
             */
            th.setResult(resultXml);
            th.startDocument();
            // root tag begin
            AttributesImpl attr = new AttributesImpl();

            attr.addAttribute(null, null, "version", null, mPersistFormat.getVersion());
            th.startElement(null, null, mRootElement, attr);

            // 将PersistObjectBase序列化为child elements.
            for (PersistObjectBase objectBase : pobs) {
                attr.clear();
                // Check PersistObjectBase validity
                if (!objectBase.isValid()) {
                    continue;
                }

                // 合并 mcc mnc
                if (objectBase instanceof SpnObject) {
                    ((SpnObject) objectBase).mergerMccMnc();
                }
                // bind all attribute
                objectBase.bindAttribute(attr);
                th.startElement("", "", mChildElement, attr);
                th.endElement("", "", mChildElement);
            }

            // root tag end
            th.endElement("", "", mRootElement); //
            th.endDocument(); // 结束xml文档
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
    }

    private class SaxHandler extends org.xml.sax.helpers.DefaultHandler {
        private String TAG = "SaxHandler";
        private int mMaxIndex;

        @Override
        public void startDocument() throws SAXException {
        }

        @Override
        public void endDocument() throws SAXException {
            ParserUtils.log(TAG, "----------parse XML begin---------------");
            ParserUtils.log(TAG, mNameIndexMapping.toString());
            ParserUtils.log(TAG, "----------parse XML end ----------------");
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            boolean isSet = false;
            if (qName.equals("apntag") && mPersistFormat == PersistFormat.APN) {
                isSet = true;
                ++mMaxIndex;
            } else if (qName.equals("spntag") && mPersistFormat == PersistFormat.SPN) {
                isSet = true;
                ++mMaxIndex;
            }
            if (isSet) {
                String name = attributes.getValue("name");
                int index = Integer.parseInt(attributes.getValue("index"));
                mNameIndexMapping.mapNameIndex(name, index);
                mNameIndexMapping.setMaxIndex(mMaxIndex);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
        }
    }

}
