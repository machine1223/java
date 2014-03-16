package com.pairwinter.usage.pom;
import java.io.*;
import java.util.*;

import com.pw.common.json.JackSonUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 14-3-3
 * Time: 下午9:47
 * To change this template use File | Settings | File Templates.
 */
public class PomTest {
        public static void main(String[] args) throws Exception{
            List<Company> list = parseExcel("E:\\songzhen\\data.xls");
            List<Company> list1 = new ArrayList<Company>();
            int length = 0;
            for (Company company : list) {

                if(company.getBoothNum()==null){
                    company.setBoothNum("");
                }else{
                    company.setBoothNum(company.getBoothNum().trim());
                }
                if(company.getNameChina()==null){
                    company.setNameChina("");
                }else{
                    company.setNameChina(company.getNameChina().trim());
                }
                if(company.getNameEnglish()==null){
                    company.setNameEnglish("");
                }else{
                    company.setNameEnglish(company.getNameEnglish().trim());
                }
                if(company.getAddress()==null){
                    company.setAddress("");
                }else{
                    company.setAddress(company.getAddress().trim());
                }
                if(company.getPostcode()==null){
                    company.setPostcode("");
                }else{
                    company.setPostcode(company.getPostcode().trim());
                }
                if(company.getPhoneNum()==null){
                    company.setPhoneNum("");
                }else{
                    company.setPhoneNum(company.getPhoneNum().trim());
                }
                if(company.getFaxNum()==null){
                    company.setFaxNum("");
                } else{
                    company.setFaxNum(company.getFaxNum().trim());
                }
                if(company.getEmail()==null){
                    company.setEmail("");
                }else{
                    company.setEmail(company.getEmail().trim());
                }
                if(company.getWebsite()==null){
                    company.setWebsite("");
                } else{
                    company.setWebsite(company.getWebsite().trim());
                }
                if(company.getSummaryChina()==null){
                    company.setSummaryChina("");
                } else{
                    company.setSummaryChina(company.getSummaryChina().trim());
                }
                if(company.getSummaryEnglish()==null){
                    company.setSummaryEnglish("");
                } else{
                    company.setSummaryEnglish(company.getSummaryEnglish().trim());
                }

//                list1.add(company);
//                if(list1.size()>10){
//                    break;
//                }
            }
//            System.out.println(sb.toString());
//            System.out.println(JackSonUtils.toJson(list));
//            PrintWriter out = new PrintWriter("");
//            out.println(JackSonUtils.toJson(list1));
            BufferedWriter writer = null;
            try
            {
                writer = new BufferedWriter( new FileWriter( "E:/data.js"));
                writer.write("var datas = ");
                writer.write(JackSonUtils.toJson(list));
                writer.close();
            }
            catch ( IOException e)
            {
            }
        }

        /**
         * 解析Excel,读取内容
         * @param path Excel路径
         * @return
         */
        @SuppressWarnings("unchecked")
        public static List parseExcel(String path){
            List<Company> list = new ArrayList();
            File file = null;
            InputStream input = null;
            if(path!=null&&path.length()>7){
                //判断文件是否是Excel(2003、2007)
                String suffix = path.substring(path.lastIndexOf("."),path.length());
                file = new File(path);
                try {
                    input = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    System.out.println("未找到指定的文件！");
                }
                if (".xls".equals(suffix)) {// 2003
                    System.out.println("Excel为2003版本");
                    POIFSFileSystem fileSystem = null;
                    HSSFWorkbook workBook = null;//工作簿
                    try {
                        fileSystem = new POIFSFileSystem(input);
                        workBook = new HSSFWorkbook(fileSystem);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    HSSFSheet sheet = workBook.getSheetAt(0);//获取第一个工作簿
                    list = getContent((Sheet)sheet);
                } else if (".xlsx".equals(suffix)) {// 2007
                    System.out.println("Excel为2007版本");
                    XSSFWorkbook workBook = null;
                    try {
                        workBook = new XSSFWorkbook(input);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    XSSFSheet sheet = workBook.getSheetAt(0);//获取第一个工作簿
                    list = getContent(sheet);
                }
            }else{
                System.out.println("非法的文件路径!");
            }
            return list;
        }

        /**
         * 获取Excel内容
         * @param sheet
         * @return
         */
        @SuppressWarnings("unchecked")
        public static List getContent(Sheet sheet){
            List<Company> list = new ArrayList();
            int rowCount = sheet.getPhysicalNumberOfRows();//行数

            Map<String,Object> bn_nc = new HashMap<String,Object>();
            for(int i=1;i<rowCount;i++){//遍历行，略过标题行，从第二行开始
                Company entity = new Company();
                Row row = sheet.getRow(i);
                if(row == null || row.getCell(0) == null || (row.getCell(0).getStringCellValue() == null || row.getCell(0).getStringCellValue().trim().length()==0) ){
                    continue;
                }
                int cellCount = row.getPhysicalNumberOfCells();
                for(int j=0;j<cellCount;j++){//遍历行单元格
                    Cell cell = row.getCell(j);
                    if(cell == null){
                        continue;
                    }
                    switch(j){
                        case 0:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                entity.setBoothNum(cell.getStringCellValue());
                            }
                            break;
                        case 1:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                entity.setNameChina(cell.getStringCellValue());
                            }
                            break;
                        case 2:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                entity.setNameEnglish(cell.getStringCellValue());
                            }
                            break;
                        case 3:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
//                                entity.set(cell.getStringCellValue());
                            }
                            break;
                        case 4:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                String type = cell.getStringCellValue();
                                entity.setTypes(new ArrayList<String>(Arrays.asList(type)));
                            }
                            break;
                        case 5:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                entity.setAddress(cell.getStringCellValue());
                            }
                            break;
                        case 6:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                entity.setPostcode(cell.getStringCellValue());
                            }
                            break;
                        case 7:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                entity.setPhoneNum(cell.getStringCellValue());
                            }
                            break;
                        case 8:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                entity.setFaxNum(cell.getStringCellValue());
                            }
                            break;
                        case 9:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                entity.setEmail(cell.getStringCellValue());
                            }
                            break;
                        case 10:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                entity.setWebsite(cell.getStringCellValue());
                            }
                            break;
                        case 11:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                entity.setSummaryChina(cell.getStringCellValue());
                            }
                            break;
                        case 12:
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                entity.setSummaryEnglish(cell.getStringCellValue());
                            }
                            break;
                        default:break;
                    }
                }
                if(!bn_nc.containsKey(entity.getBoothNum()+entity.getNameChina())){
                    bn_nc.put(entity.getBoothNum()+entity.getNameChina(),entity);
                    list.add(entity);
                }else{
                    Company first = (Company)bn_nc.get(entity.getBoothNum()+entity.getNameChina());
                    List types = first.getTypes();
                    types.add(entity.getTypes().get(0));
                    first.setTypes(types);
                }
            }
            return list;
        }
    }
