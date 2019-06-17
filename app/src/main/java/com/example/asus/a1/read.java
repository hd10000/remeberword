package com.example.asus.a1;
import java.util.*;
import jxl.Workbook;
import jxl.Sheet;
import jxl.WorkbookSettings;

import android.content.res.*;


import android.util.*;
import android.nfc.*;
import android.widget.*;
import java.io.*;
import android.content.*;

import static com.example.asus.a1.MainActivity.TAG;


public class read
{
    WorkbookSettings workbookSettings;
    Workbook workbook;
    Sheet sheet;
    int length;
    String[] line;
    jxl.Cell[] tRow;//中间行 用于两行交换的时候作中间行

    public ArrayList<String[]> getXlsData(String filename, int index,Context context) {
        ArrayList<String[]> wordList = new ArrayList<String[]>();

        try {
//            Workbook workbook = Workbook.getWorkbook(file);//
            openxls(filename,index,context);
            int sheetNum = workbook.getNumberOfSheets();
            int sheetRows = sheet.getRows();
            int sheetColumns = sheet.getColumns();
            line=new String[sheet.getColumns()];
            for (int row = 0; row < sheetRows; row++) {
                line[0]=sheet.getCell(0, row).getContents();
                line[1]=sheet.getCell(1, row).getContents();
                wordList.add(line);
            }

            workbook.close();

        } catch (Exception e) {

        	
        }
        return wordList;
    }
    public void openxls(String filename, int index,Context context){
        workbookSettings = new WorkbookSettings();
        workbookSettings.setEncoding("utf-8");
   try {
       workbook = Workbook.getWorkbook(new File(filename),workbookSettings);

   }catch (Exception e){
       Toast.makeText(context,
               "error:打开失败",
               Toast.LENGTH_LONG).show();
       e.printStackTrace();
   }
        sheet = workbook.getSheet(0);

    }
    public String[] readline(String filename,int index,Context context,int[] indexs){
        word w = new word();
        openxls(filename,index,context);
        line=new String[sheet.getColumns()];
        Log.d(TAG, "linelength: "+line.length);
        for (int i=0;i<line.length-1;i++){
            line[i]=sheet.getCell(indexs[i], index).getContents();
        }
        workbook.close();
         return line;
    }
    public void swapRow(int difIndex, int index){
        tRow = sheet.getRow(index);

    }
}
