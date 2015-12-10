
package com.wind.parser;

import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainClass {

    private static final int SHEET_INDEX = 0;
    private final String TAG = "MainClass";

    public static void main(String[] args) {
        MainClass mc = new MainClass();
        mc.constructXml(PersistFormat.SPN);
    }

    void constructXml(PersistFormat format) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            XmlParser xpp = new XmlParser();
            xpp.initParameters(format);

            fis = new FileInputStream(xpp.getSrcFile());

            Workbook workbook = Workbook.getWorkbook(fis);
            ParserUtils.log(TAG, "SHEET_INDEX must at 0 ===>" + SHEET_INDEX);
            Sheet sheet = workbook.getSheet(SHEET_INDEX);
            int rowsCount = sheet.getRows();
            ParserUtils.log(TAG, "rowsCount from Sheet= " + rowsCount);

            xpp.parseIndex();
            Collection<PersistObjectBase> pobs = xpp.parseXml(sheet);
            //
            fos = new FileOutputStream(xpp.getOutFile());
            xpp.buildOutXml(fos, pobs);

        } catch (UnSpecifiedFormatException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
