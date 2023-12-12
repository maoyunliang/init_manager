package com.yitai.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yitai.annotation.excel.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;

/**
 * ClassName: ExcelUtil
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/23 16:56
 * @Version: 1.0
 */
@SuppressWarnings("unused")
public class ExcelUtils {
    private static <E> List<E> getList(List<E> list) {
        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 判断集合是否为空
     *
     * @param c 集合对象
     * @return true-为空，false-不为空
     */
    private static <E> boolean isEmpty(Collection<E> c) {
        return c == null || c.isEmpty();
    }

    /**
     * 判断字符串是否为空
     *
     * @param c 集合对象
     * @return true-为空，false-不为空
     */
    private static boolean isEmpty(String c) {
        return c == null || c.trim().isEmpty();
    }

    private static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    private static <K, V> boolean notEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }


    private static boolean notEmpty(String c) {
        return !isEmpty(c);
    }

    /**
     * 导出文件到本地
     *
     * @param file 本地excel文件，如（C:\excel\用户表.xlsx）
     * @param list 导出数据列表
     * @param c    泛型类
     * @param <T>  泛型
     */
    public static <T> void export(File file, List<T> list, Class<T> c) {
        checkExportFile(file);
        ExportWorkBook myWorkBook = getExportWorkBook(file.getName(), list, c);
        Workbook xssfWorkbook = getExportXSSFWorkbook(myWorkBook);
        export(xssfWorkbook, file);
    }

    /**
     * 导出文件到响应
     *
     * @param response 响应对象
     * @param fileName 导出文件名称（不带尾缀）
     * @param list 导出数据列表
     * @param c    泛型类
     * @param <T>  泛型
     */
    public static <T> void export(HttpServletResponse response, String fileName, List<T> list, Class<T> c) {
        if (isEmpty(fileName)) {
            fileName = String.valueOf(System.currentTimeMillis());
        }
        ExportWorkBook myWorkBook = getExportWorkBook(fileName, list, c);
        Workbook xssfWorkbook = getExportXSSFWorkbook(myWorkBook);
        export(response, xssfWorkbook, fileName);
    }


    /**
     * 将工作簿导出到本地文件
     *
     * @param workbook POI工作簿对象
     * @param file     本地文件对象
     */
    private static void export(Workbook workbook, File file) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            ByteArrayOutputStream ops = new ByteArrayOutputStream();
            workbook.write(ops);
            fos.write(ops.toByteArray());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将工作簿导出到响应
     *
     * @param response 响应流对象
     * @param workbook POI工作簿对象
     * @param fileName 文件名称
     */
    private static void export(HttpServletResponse response, Workbook workbook, String fileName) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName +".xlsx");
            workbook.write(response.getOutputStream());
            workbook.write(os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
////            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//            response.setContentType("application/vnd.ms-excel");
//            response.setCharacterEncoding("utf-8");
////            String name = fileName+ExcelType.XLSX;
//            fileName= URLEncoder.encode(fileName, StandardCharsets.UTF_8);
////            String name = new String(fileName.getBytes("GBK"), "ISO8859_1") + ExcelType.XLSX;
//            response.setHeader("Content-Disposition", "attachment;filename=" + fileName +".xlsx");
//            ServletOutputStream out = response.getOutputStream();
//            workbook.write(out);
//            out.flush();
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 获取导出表格对象
     *
     * @param fileName 文件全名
     * @param list     导出对象集合
     * @param c        泛型类
     * @param <T>      泛型
     * @return ExportWorkBook
     */
    private static <T> ExportWorkBook getExportWorkBook(String fileName, List<T> list, Class<T> c) {
        // ExportSheet
        List<ExportSheet> sheets = new ArrayList<>();
        ExportSheet sheet = ExportSheetFactory.createExportSheet(list, c);
        if (isEmpty(sheet.getName())) {
            sheet.setName(getFileName(fileName));
        }
        sheets.add(sheet);
        // ExportWorkBook
        ExportWorkBook book = new ExportWorkBook();
        book.setName(fileName);
        book.setSheets(sheets);
        return book;
    }

    /**
     * 将工作簿导出到本地文件
     *
     * @param myWorkBook 导出工作簿对象
     * @return POI导出工作簿对象
     */
    public static Workbook getExportXSSFWorkbook(ExportWorkBook myWorkBook) {
        Workbook workbook = new XSSFWorkbook();
        List<ExportSheet> mySheets = myWorkBook.getSheets();
        for (ExportSheet mySheet : mySheets) {
            // 默认样式
            CellStyle headerStyle = getDefaultHeaderStyle(workbook);
            CellStyle dataStyle = getDefaultDataStyle(workbook);
            // 自定义样式
            setCellStyle(headerStyle, mySheet.getHeaderStyle());
            setCellStyle(dataStyle, mySheet.getDataStyle());
            // Sheet：数据
            Sheet sheet = workbook.createSheet(mySheet.getName());
            // Sheet：样式
            setSheetValue(sheet, mySheet, headerStyle, dataStyle);
            // Sheet：批注
            setSheetComment(sheet, mySheet);
            // Sheet：冻结表头
            setFreezeHeader(sheet, mySheet);
            // Sheet：水印
            setWatermark(workbook, sheet, mySheet);
            // Sheet：设置宽高
            setColumnWidth(sheet, mySheet);
        }
        return workbook;
    }

    private static void setColumnWidth(Sheet sheet, ExportSheet mySheet) {
        // 全局列宽（默认15个字符）
        int globalWidth = mySheet.getColumnWidth();
        if (globalWidth <= 0) {
            globalWidth = 15;
        }
        // 指定列宽
        Map<Integer, Integer> columnIndexWidthMap = mySheet.getColumnIndexWidthMap();
        if (notEmpty(columnIndexWidthMap)) {
            for (Map.Entry<Integer, Integer> entry : columnIndexWidthMap.entrySet()) {
                int columnIndex = entry.getKey();
                int columnWidth = entry.getValue();
                if (columnWidth <= 0) {
                    columnWidth = globalWidth;
                }
                sheet.setColumnWidth(columnIndex, columnWidth * 256);
            }
        }
    }

    private static void setWatermark(Workbook workbook, Sheet sheet, ExportSheet mySheet) {
        ExportWatermark watermark = mySheet.getWatermark();
        int type = watermark.getType();
        String src = watermark.getSrc();
        if (!WatermarkType.containCode(type) || type == WatermarkType.NONE.code || ExcelUtils.isEmpty(src)) {
            return;
        }
        byte[] imageBytes = getWaterMarkImageBytes(type, src);
        if (imageBytes == null) {
            return;
        }
        XSSFSheet xssfSheet = (XSSFSheet) sheet;
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        int pictureIndex = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
        XSSFPictureData watermarkPictureData = (XSSFPictureData) workbook.getAllPictures().get(pictureIndex);
        PackagePartName ppn = watermarkPictureData.getPackagePart().getPartName();
        PackageRelationship packageRelationship = xssfSheet.getPackagePart().addRelationship(ppn, TargetMode.INTERNAL, XSSFRelation.IMAGES.getRelation(), null);
        CTWorksheet ctWorksheet = xssfSheet.getCTWorksheet();
        CTSheetBackgroundPicture backgroundPicture = ctWorksheet.addNewPicture();
        System.out.println(packageRelationship.getId());
        backgroundPicture.setId(packageRelationship.getId());
    }

    private static byte[] getWaterMarkImageBytes(int type, String src) {
        try {
            if (type == WatermarkType.TEXT.code) {
                // 文本文字
                return getImageBytesFromText(src);
            }
            if (type == WatermarkType.FILE.code) {
                // 本地图片
                return getImageBytesFromFile(src);
            }
            if (type == WatermarkType.URL.code) {
                // 网络图片
                return getImageBytesFromUrl(src);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getImageBytesFromFile(String imageFilePath) throws IOException {
        File file = new File(imageFilePath);
        if (!file.exists()) {
            throw new RuntimeException(String.format("水印图片%s不存在", imageFilePath));
        }
        // 文件限制为10MB下的图片
        int allowMaxMb = 10;
        int allowMaxByte = 10 * 1024 * 1024;
        if (file.length() > allowMaxByte) {
            throw new RuntimeException(String.format("水印图片%s不能超过%sMB", imageFilePath, allowMaxMb));
        }
        // 水印图片限制为 png,jpg,jpeg
        List<String> allowTypes = Arrays.asList(".png", ".jpg", ".jpeg");
        boolean isMatchType = false;
        String fileName = file.getName();
        for (String allowType : allowTypes) {
            if (fileName.endsWith(allowType)) {
                isMatchType = true;
                break;
            }
        }
        if (!isMatchType) {
            throw new RuntimeException(String.format("水印图片%s格式不正确，请填写%s格式的图片", imageFilePath, allowTypes));
        }
        // 获取图片文件二进制流
        return FileUtils.readFileToByteArray(file);
    }

    public static byte[] getImageBytesFromText(String text) throws IOException {
        int width = 400;
        int height = 300;
        // 创建画板
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 创建画笔
        Graphics2D g2d = bufferedImage.createGraphics();
        // 绘制透明背景
        bufferedImage = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = bufferedImage.createGraphics();
        // 设置绘制颜色和旋转角度
        g2d.setColor(new Color(128, 122, 125, 80));
        g2d.setStroke(new BasicStroke(1));
        Font font = new Font(null, Font.BOLD, 40);
        g2d.setFont(font);
        g2d.rotate(-0.5, (double) bufferedImage.getWidth() / 2, (double) bufferedImage.getHeight() / 2);
        // 获取文本边界和绘制位置
        FontRenderContext context = g2d.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(text, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = -bounds.getY();
        double baseY = y + ascent;
        // 绘制文本
        g2d.drawString(text, (int) x, (int) baseY);
        // 保存绘制结果，并释放资源
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g2d.dispose();
        // 将 BufferedImage 转换为字节数组
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", os);
        return os.toByteArray();
    }

    public static byte[] getImageBytesFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream is = url.openStream();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }

    private static void setFreezeHeader(Sheet sheet, ExportSheet mySheet) {
        if (!mySheet.freezeHeader) {
            return;
        }
        int headerRows = 0;
        List<ExportRow> rows = mySheet.getRows();
        for (ExportRow row : rows) {
            if (row.isHeader()) {
                headerRows++;
            } else {
                break;
            }
        }
        System.out.println("headerRows = " + headerRows);
        sheet.createFreezePane(0, headerRows);
    }

    private static CellStyle getDefaultHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        // 默认表头内容对齐为：居中显示
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        // 默认表头背景为：淡灰色（填充方式为全填充）
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        return headerStyle;
    }

    private static CellStyle getDefaultDataStyle(Workbook workbook) {
        CellStyle dataStyle = workbook.createCellStyle();
        // 默认数据内容对齐为：居左显示
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        return dataStyle;
    }

    /**
     * 自定义样式转化为表格样式
     *
     * @param style   POI样式
     * @param myStyle 自定义样式
     */
    private static void setCellStyle(CellStyle style, ExportCellStyle myStyle) {
        if (myStyle == null) {
            return;
        }
        if (myStyle.getBackgroundColor() > 0) {
            style.setFillBackgroundColor(myStyle.getBackgroundColor());
        }
    }

    /**
     * 设置批注
     *
     * @param poiSheet POI页Sheet对象
     * @param mySheet  导出Sheet对象
     */
    private static void setSheetComment(Sheet poiSheet, ExportSheet mySheet) {
        if (isEmpty(mySheet.getComments())) {
            return;
        }
        Drawing<?> drawing = poiSheet.createDrawingPatriarch();
        for (ExportComment myComment : mySheet.getComments()) {
            int x = myComment.getX();
            int y = myComment.getY();
            String commentStr = myComment.getComment();
            XSSFClientAnchor xssfClientAnchor = new XSSFClientAnchor(0, 0, 0, 0, x, y, x + 2, y + 6);
            xssfClientAnchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
            Comment comment = drawing.createCellComment(xssfClientAnchor);
            comment.setString(new XSSFRichTextString(commentStr));
            poiSheet.getRow(y).getCell(x).setCellComment(comment);
        }
    }

    /**
     * 设置Sheet数据
     *
     * @param sheet   POI页Sheet对象
     * @param mySheet 导出Sheet对象
     */
    private static void setSheetValue(Sheet sheet, ExportSheet mySheet, CellStyle headerStyle, CellStyle dataStyle) {
        List<ExportRow> myRows = getList(mySheet.getRows());
        for (int j = 0; j < myRows.size(); j++) {
            ExportRow myRow = myRows.get(j);
            List<ExportCell> myCells = getList(myRow.getCells());
            Row row = sheet.createRow(j);
            CellStyle tempStyle = myRow.isHeader() ? headerStyle : dataStyle;
            for (int k = 0; k < myCells.size(); k++) {
                ExportCell myCell = myCells.get(k);
                Cell cell = row.createCell(k);
                setCellValue(cell, myCell);
                cell.setCellStyle(tempStyle);
            }
        }
    }

    /**
     * 设置单元格数据
     *
     * @param cell   POI单元格对象
     * @param myCell 导出单元格对象
     */
    private static void setCellValue(Cell cell, ExportCell myCell) {
        // 无值情况，返回空字符串
        Object o = myCell.getValue();
        if (Objects.isNull(o) || isEmpty(o.toString())) {
            cell.setCellValue("");
            return;
        }
        // 字符串类型
        if (o instanceof String) {
            cell.setCellValue((String) o);
            return;
        }
        // Integer类型
        if (o instanceof Integer) {
            cell.setCellValue((Integer) o);
            return;
        }
        // Long类型
        if (o instanceof Long) {
            // 数字类型默认超过10位，excel默认显示科学计数法，因此长度超过10位，则显示为文本格式
            int length = o.toString().length();
            if (length > 10) {
                cell.setCellValue(o.toString());
            } else {
                cell.setCellValue((Long) o);
            }
            return;
        }
        // Double
        if (o instanceof Double || o instanceof Float || o instanceof BigDecimal) {
            // 去除科学计数法显示
            BigDecimal b = new BigDecimal(o.toString());
            String s = b.toPlainString();
            int length = s.length();
            if (length > 10) {
                cell.setCellValue(s);
            } else {
                cell.setCellValue(b.doubleValue());
            }
            return;
        }
        // 日期格式
        if (o instanceof Date) {
            Date date = new Date();
            String dateFormat = myCell.getDateFormat();
            if (isEmpty(dateFormat)) {
                dateFormat = "yyyy-MM-dd HH:mm:ss";
            }
            cell.setCellValue(new SimpleDateFormat(dateFormat).format(date));
            return;
        }
        // 其他格式
        cell.setCellValue(o.toString());
    }

    /**
     * 获取文件名称（不带文件尾缀）
     *
     * @param fileFullName 文件名称（带尾缀）
     * @return 文件名称（不带尾缀）
     */
    public static String getFileName(String fileFullName) {
        int lastDotIndex = fileFullName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return fileFullName.substring(0, lastDotIndex);
        }
        return fileFullName;
    }

    /**
     * 检测导出文件是否OK
     *
     * @param file 到处在excel文件
     */
    public static void checkExportFile(File file) {
        if (Objects.isNull(file)) {
            throw new RuntimeException("文件不能为空");
        }
        if (file.exists()) {
            return;
        }
        String name = file.getName();
        if (!name.endsWith(ExcelType.XLSX.val)) {
            throw new RuntimeException("导出的excel文件请为xlsx类型的文件");
        }
        File parentFile = file.getParentFile();
        if (Objects.nonNull(parentFile) && !parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
            if (!mkdirs) {
                throw new RuntimeException("父级文件夹创建失败");
            }
        }
        try {
            boolean createNewFile = file.createNewFile();
            if (!createNewFile) {
                throw new RuntimeException("文件创建失败");
            }
        } catch (IOException e) {
            throw new RuntimeException("文件创建失败");
        }
    }

    /**
     * 读取excel文件，返回文件第一个sheet页
     *
     * @param file excel文件
     * @return 第一个sheet页
     */
    public static Sheet readSheet(File file) {
        return readManySheet(file).get(0);
    }

    /**
     * 读取excel文件，返回文件第一个sheet页
     *
     * @param file excel文件
     * @return 第一个sheet页
     */
    public static Sheet readSheet(MultipartFile file) {
        return readManySheet(file).get(0);
    }

    /**
     * 读取excel文件，返回所有sheet页
     *
     * @param file excel文件
     * @return 所有sheet页
     */
    public static List<Sheet> readManySheet(File file) {
        Workbook workbook = getWorkBook(file);
        return getSheets(workbook);
    }


    /**
     * 读取excel文件，返回所有sheet页
     *
     * @param file excel文件
     * @return 所有sheet页
     */
    public static List<Sheet> readManySheet(MultipartFile file) {
        Workbook workbook = getWorkBook(file);
        return getSheets(workbook);
    }


    /**
     * 读取exel文件，返回第一个 sheet 页数据，以JSONArray格式返回（JSONArray中每个元素为每行数据）
     *
     * @param file       excel文件
     * @param containKey 是否需要包含键
     *                   （1）当为true时，JSONArray中每个元素为JSONObject对象，其中键为表头(默认第一行为表头)，值为单元格数据，
     *                   例如：[{"k1":"v1","k2":"v2"},{"k1":"v3","k2":"v4"}]
     *                   （2）当为false时，JSONArray中每个元素为每行单元格数据，位置和文件打开时的视图一一对应
     *                   例如：[["k1","k2"],["v1","v2"],["v3","v4"]]
     * @return JSONArray对象
     */
    public static JSONArray read(File file, boolean containKey) {
        Sheet sheet = readSheet(file);
        return pares(sheet, containKey);
    }

    /**
     * 读取exel文件，返回第一个 sheet 页数据，以JSONArray格式返回（JSONArray中每个元素为每行数据）
     *
     * @param file       excel文件
     * @param containKey 是否需要包含键
     *                   （1）当为true时，JSONArray中每个元素为JSONObject对象，其中键为表头(默认第一行为表头)，值为单元格数据，
     *                   例如：[{"k1":"v1","k2":"v2"},{"k1":"v3","k2":"v4"}]
     *                   （2）当为false时，JSONArray中每个元素为每行单元格数据，位置和文件打开时的视图一一对应
     *                   例如：[["k1","k2"],["v1","v2"],["v3","v4"]]
     * @return JSONArray对象
     *
     */
    public static JSONArray read(MultipartFile file, boolean containKey) {
        Sheet sheet = readSheet(file);
        return pares(sheet, containKey);
    }

    /**
     * 读取excel文件，返回第一个sheet页数据，以List<Java对象>格式返回（默认第一行为表头）
     *
     * @param file excel文件
     * @param <T>  泛型对象
     * @return List<Java对象>
     */
    public static <T> List<T> read(File file, Class<T> c) {
        Sheet sheet = readSheet(file);
        return pares(sheet, c);
    }

    /**
     * 读取excel文件，返回第一个sheet页数据，以List<Java对象>格式返回（默认第一行为表头）
     *
     * @param file excel文件
     * @param <T>  泛型对象
     * @return List<Java对象>
     */
    public static <T> List<T> read(MultipartFile file, Class<T> c) {
        Sheet sheet = readSheet(file);
        return pares(sheet, c);
    }


    /**
     * 解析sheet数据，并返回Java对象
     *
     * @param sheet sheet对象
     * @param <T>   泛型对象
     * @return java对象
     */
    public static <T> List<T> pares(Sheet sheet, Class<T> c) {
        if (Objects.isNull(sheet) || Objects.isNull(c) || sheet.getLastRowNum() < 0) {
            return Collections.emptyList();
        }
        // 取得该类所有打了@ExcelImport注解的属性
        List<ImportClassField> importFields = getImportFields(c);
        if (isEmpty(importFields)) {
            return Collections.emptyList();
        }
        // 如果有columnIndex属性，则优先按columnIndex进行解析（这个解析不需要表头）
        if (hasColumnIndex(importFields)) {
            return pares(sheet, c, importFields, 0);
        }
        // 存在表头的话，先补充其他字段的表头下标，再进行解析（因为最终都是按列进行解析的）
        for (Cell cell : sheet.getRow(0)) {
            String value = getCellStringValue(cell);
            if (!isEmpty(value)) {
                for (ImportClassField field : importFields) {
                    if (field.getColumnIndex() < 0 && value.equals(field.getColumnName())) {
                        field.setColumnIndex(cell.getColumnIndex());
                    }
                }
            }
        }
        return pares(sheet, c, importFields, 1);
    }

    /**
     * 解析sheet数据，并返回Java对象
     *
     * @param sheet         sheet对象
     * @param importFields  本次需要解析的列
     * @param rowStartIndex 开始解析的行数
     * @param <T>           泛型对象
     * @return Java对象
     */
    private static <T> List<T> pares(Sheet sheet, Class<T> c, List<ImportClassField> importFields, int rowStartIndex) {
        // 属性按表头索引进行区分
        Map<Integer, ImportClassField> indexFieldMap = new LinkedHashMap<>();
        for (ImportClassField field : importFields) {
            if (field.getColumnIndex() >= 0) {
                indexFieldMap.put(field.getColumnIndex(), field);
            }
        }
        // 开始解析行下标不可大于sheet最大行下标
        int lastRowNum = sheet.getLastRowNum();
        if (rowStartIndex > lastRowNum) {
            return Collections.emptyList();
        }
        // 开始解析
        List<T> ts = new ArrayList<>();
        for (int i = rowStartIndex; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            // 判断是否为空行，非空行才进行数据解析
            if (!isBlankRow(row)) {
                try {
                    T t = getBean(c, row, indexFieldMap);
                    ts.add(t);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return ts;
    }

    /**
     * 行数据转化为java对象
     *
     * @param c             java对象类
     * @param row           行数据对象
     * @param indexFieldMap 关联的属性
     * @return java对象
     */
    private static <T> T getBean(Class<T> c, Row row, Map<Integer, ImportClassField> indexFieldMap) throws Exception {
        T t = c.newInstance();
        for (Map.Entry<Integer, ImportClassField> entry : indexFieldMap.entrySet()) {
            int columnIndex = entry.getKey();
            ImportClassField field = entry.getValue();
            Cell cell = row.getCell(columnIndex);
            setFieldValue(t, cell, field);
        }
        return t;
    }

    /**
     * 对象属性赋值
     *
     * @param t    java对象
     * @param cell 单元格对象
     */
    private static <T> void setFieldValue(T t, Cell cell, ImportClassField importClassField) {
        String value = getCellStringValue(cell);
        if (isEmpty(value)) {
            return;
        }
        // 映射取值
        LinkedHashMap<String, String> kvMap = importClassField.getKvMap();
        if (!kvMap.isEmpty()) {
            for (Map.Entry<String, String> entry : kvMap.entrySet()) {
                if (entry.getValue().equals(value)) {
                    value = entry.getKey();
                    break;
                }
            }
        }
        Field field = importClassField.getField();
        Class<?> type = field.getType();
        try {
            // 按照使用频率优先级赋值判断（包含: String + 8大基本数据类型 + Date + BigDecimal）
            if (type == String.class) {
                field.set(t, value);
            } else if (type == int.class || type == Integer.class) {
                field.set(t, Integer.parseInt(value));
            } else if (type == long.class || type == Long.class) {
                field.set(t, Long.parseLong(value));
            } else if (type == Date.class) {
                String dateFormat = importClassField.getDateFormat();
                Date date = getDate(cell, dateFormat);
                field.set(t, date);
            } else if (type == double.class || type == Double.class) {
                field.set(t, Double.parseDouble(value));
            } else if (type == boolean.class || type == Boolean.class) {
                field.set(t, Boolean.parseBoolean(value));
            } else if (type == BigDecimal.class) {
                field.set(t, new BigDecimal(value));
            } else if (type == float.class || type == Float.class) {
                field.set(t, Float.parseFloat(value));
            } else if (type == short.class || type == Short.class) {
                field.set(t, Short.parseShort(value));
            } else if (type == char.class || type == Character.class) {
                field.set(t, value.charAt(0));
            } else if (type == byte.class || type == Byte.class) {
                field.set(t, Byte.parseByte(value));
            }
            // 其他数据类型根据需要自行添加处理
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取时间类型
     *
     * @return 时间
     */
    private static Date getDate(Cell cell, String dateFormat) {
        CellType cellType = cell.getCellType();
        // 如果单元格自己能够识别这是一个date类型数据，则忽略用户格式样式，直接返回
        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        }
        // 获取表格数据
        String value = getCellStringValue(cell);
        // 如果是纯数字，则为时间戳
        if (Pattern.matches("\\d+", value)) {
            long timestamp = Long.parseLong(value);
            // 长度大于10的时候，则为毫秒级时间戳，毫秒级时间戳需要去掉最后三位
            if (value.length() > 10) {
                timestamp = timestamp / 1000;
            }
            return new Date(timestamp);
        }
        // 下面按照时间格式进行格式化（如果用户填写了时间格式，则优先以用户格式进行校验）
        Date date = null;
        if (!isEmpty(dateFormat)) {
            date = parseDate(value, dateFormat);
        }
        // 尝试时间是否为yyyy-MM-dd HH:mm:ss格式
        if (Objects.isNull(date)) {
            date = parseDate(value, "yyyy-MM-dd HH:mm:ss");
        }
        // 尝试时间是否为yyyy-MM-dd格式
        if (Objects.isNull(date)) {
            date = parseDate(value, "yyyy-MM-dd");
        }
        // 如果还想进行其他时间解析尝试，可以在此自行添加
        return date;
    }

    /**
     * 将字符串格式日期转化为Date时间
     *
     * @param dateStr     字符串日期
     * @param formatStyle 格式化样式
     * @return Date时间
     */
    private static Date parseDate(String dateStr, String formatStyle) {
        try {
            return new SimpleDateFormat(formatStyle).parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断是否为空行
     *
     * @param row 当前行数据
     * @return true-是空行，false-不是空行
     */
    private static boolean isBlankRow(Row row) {
        for (Cell cell : row) {
            if (!isEmpty(getCellStringValue(cell))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断本次导入的对象中是否存在按索性进行解析的
     *
     * @param importFields 本次导入对象的属性
     * @return true-存在；false-不存在
     */
    private static boolean hasColumnIndex(List<ImportClassField> importFields) {
        for (ImportClassField field : importFields) {
            if (field.getColumnIndex() >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定类中所有打了ExcelImport注解的属性（包括父类、祖类）
     *
     * @param c 指定类
     * @return 该类所有打了ExcelImport注解的属性（包括父类、祖类）
     */
    private static List<ImportClassField> getImportFields(Class<?> c) {
        List<ImportClassField> excelImportClassFields = new ArrayList<>();
        List<Field> allClassFields = getAllFields(c);
        for (Field field : allClassFields) {
            ExcelImport excelImport = field.getAnnotation(ExcelImport.class);
            if (Objects.nonNull(excelImport)) {
                field.setAccessible(true);
                ImportClassField importField = new ImportClassField();
                importField.setClassFieldName(field.getName());
                String name = excelImport.name();
                if (ExcelUtils.isEmpty(name)) {
                    name = excelImport.value();
                }
                importField.setColumnName(name);
                importField.setColumnIndex(excelImport.columnIndex());
                importField.setDateFormat(excelImport.dateFormat());
                LinkedHashMap<String, String> kvMap = new LinkedHashMap<>();
                KV[] kvs = excelImport.kvs();
                for (KV kv : kvs) {
                    kvMap.put(kv.k(), kv.v());
                }
                importField.setKvMap(kvMap);
                importField.setField(field);
                excelImportClassFields.add(importField);
            }
        }
        return excelImportClassFields;
    }


    /**
     * 获取一个类的所有属性（包括这个类的父类属性，祖类属性）
     *
     * @param c 所在类
     * @return 类的所有属性
     */
    private static List<Field> getAllFields(Class<?> c) {
        List<Field> allFields = new ArrayList<>();
        while (Objects.nonNull(c) && c != Object.class) {
            allFields.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        }
        return allFields;
    }

    /**
     * 解析sheet数据，并返回JSONArray对象
     *
     * @param sheet      sheet对象
     * @param containKey 是否需要包含键
     *                   （1）当为true时，JSONArray中每个元素为JSONObject对象，其中键为表头(默认第一行为表头)，值为单元格数据，
     *                   例如：[{"k1":"v1","k2":"v2"},{"k1":"v3","k2":"v4"}]
     *                   （2）当为false时，JSONArray中每个元素为每行单元格数据，位置和文件打开时的视图一一对应
     *                   例如：[["k1","k2"],["v1","v2"],["v3","v4"]]
     * @return JSONArray对象
     */
    public static JSONArray pares(Sheet sheet, boolean containKey) {
        if (Objects.isNull(sheet) || sheet.getLastRowNum() < 0) {
            return new JSONArray();
        }
        return containKey ? paresJsonArrayWithKey(sheet) : paresJsonArrayNoKey(sheet);
    }

    /**
     * 解析sheet数据，并返回JSONArray对象，其中键为表头(默认第一行为表头)，值为单元格数据，
     * 例如：[{"k1":"v1","k2":"v2"},{"k1":"v3","k2":"v4"}]
     *
     * @param sheet sheet对象
     * @return 包含键的JSONArray对象
     */
    private static JSONArray paresJsonArrayWithKey(Sheet sheet) {
        // 第一行默认为表头，如果表头无有效数据，则不解析
        JSONArray array = new JSONArray();
        Row header = sheet.getRow(0);
        List<Integer> noEmptyHeaderIndexes = getNoEmptyHeaderIndexes(header);
        if (isEmpty(noEmptyHeaderIndexes)) {
            return array;
        }
        // 如果只有一行数据时，则默认该行为表头行，无数据行
        int lastRowIndex = sheet.getLastRowNum();
        if (lastRowIndex == 0) {
            JSONObject rowObj = getOrderlyJsonObject();
            for (Integer headerIndex : noEmptyHeaderIndexes) {
                String k = getCellStringValue(header.getCell(headerIndex));
                rowObj.put(k, "");
            }
            array.add(rowObj);
            return array;
        }
        // 存在多行数据时，则第一行为表头，其他行为数据
        for (int i = 1; i < lastRowIndex; i++) {
            Row row = sheet.getRow(i);
            JSONObject rowObj = getOrderlyJsonObject();
            for (Integer headerIndex : noEmptyHeaderIndexes) {
                String k = getCellStringValue(header.getCell(headerIndex));
                String v = getCellStringValue(row.getCell(headerIndex));
                rowObj.put(k, v);
            }
            array.add(rowObj);
        }
        return array;
    }

    /**
     * 获取有序的JSONObject对象
     *
     * @return 有序的JSONObject对象
     */
    private static JSONObject getOrderlyJsonObject() {
        return new JSONObject(new LinkedHashMap<>());
    }

    /**
     * 解析sheet数据，并返回JSONArray对象
     *
     * @param sheet sheet对象
     * @return 包含键的JSONArray对象
     */
    private static JSONArray paresJsonArrayNoKey(Sheet sheet) {
        JSONArray array = new JSONArray();
        for (Row row : sheet) {
            JSONArray rowArray = new JSONArray();
            for (Cell cell : row) {
                rowArray.add(getCellStringValue(cell));
            }
            array.add(rowArray);
        }
        return array;
    }

    /**
     * 获取表头不为空的下标集合
     *
     * @param header 表头行
     * @return 表头不为空的下标集合
     */
    private static List<Integer> getNoEmptyHeaderIndexes(Row header) {
        List<Integer> noEmptyHeaderIndexes = new ArrayList<>();
        for (int i = 0; i < header.getLastCellNum(); i++) {
            Cell cell = header.getCell(i);
            String cellValue = getCellStringValue(cell);
            if (!isEmpty(cellValue)) {
                noEmptyHeaderIndexes.add(i);
            }
        }
        return noEmptyHeaderIndexes;
    }

    /**
     * 获取单元格数据
     *
     * @param cell 单元格对象
     * @return 单元格值（以字符串形式返回，如果是时间，则以时间戳格式返回）
     */
    public static String getCellStringValue(Cell cell) {
        if (Objects.isNull(cell) || Objects.isNull(cell.getCellType())) {
            return "";
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    return getCellDateString(cell);
                } else {
                    return new DecimalFormat("0.####################").format(cell.getNumericCellValue());
                }
            }
            case STRING -> {
                return cell.getRichStringCellValue().getString();
            }
            case BOOLEAN -> {
                return String.valueOf(cell.getBooleanCellValue());
            }
            case BLANK -> {
                return "";
            }
            case ERROR -> {
                return FormulaError.forInt(cell.getErrorCellValue()).getString();
            }
            default -> {
                return new DataFormatter().formatCellValue(cell);
            }
        }
    }

    /**
     * 日期单元格解析（格式统一按照国人的习惯显示，即 yyyy-MM-dd HH:mm:ss）
     *
     * @param cell 单元格对象
     * @return 字符串时间
     */
    private static String getCellDateString(Cell cell) {
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        Date date = cell.getDateCellValue();
        String formatStr;
        if (dataFormatString.contains("y") && dataFormatString.contains("d")) {
            if (dataFormatString.contains("h")) {
                formatStr = "yyyy-MM-dd HH:mm:ss";
            } else {
                formatStr = "yyyy-MM-dd";
            }
        } else {
            formatStr = dataFormatString;
        }
        return new SimpleDateFormat(formatStr).format(date);
    }

    private static Workbook getWorkBook(File file) {
        try {
            if (Objects.isNull(file) || !file.exists()) {
                throw new RuntimeException("文件不能为空");
            }
            String fileName = file.getName();
            if (fileName.endsWith(ExcelType.XLSX.val)) {
                return new XSSFWorkbook(file);
            } else if (fileName.endsWith(ExcelType.XLS.val)) {
                return new HSSFWorkbook(Files.newInputStream(file.toPath()));
            } else {
                throw new RuntimeException("文件格式错误");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Workbook getWorkBook(MultipartFile file) {
        try {
            if (Objects.isNull(file) || file.isEmpty()) {
                throw new RuntimeException("文件不能为空");
            }
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            if (fileName.endsWith(ExcelType.XLSX.val)) {
                return new XSSFWorkbook(file.getInputStream());
            } else if (fileName.endsWith(ExcelType.XLS.val)) {
                return new HSSFWorkbook(file.getInputStream());
            } else {
                throw new RuntimeException("文件格式错误");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static List<Sheet> getSheets(Workbook workbook) {
        int maxSheet = workbook.getNumberOfSheets();
        List<Sheet> sheets = new ArrayList<>();
        for (int i = 0; i < maxSheet; i++) {
            sheets.add(workbook.getSheetAt(i));
        }
        return sheets;
    }

    /**
     * excel文件类型
     */
    enum ExcelType {
        // excel 2007 +
        XLSX(".xlsx"),
        // excel 2007 -
        XLS(".xls");

        public final String val;

        ExcelType(String val) {
            this.val = val;
        }
    }


    public enum WatermarkType {

        NONE(0, "无水印"),
        TEXT(1, "文本文字水印"),
        FILE(2, "本地图片水印"),
        URL(3, "网络图片水印");

        public final int code;
        public final String name;

        WatermarkType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static boolean containCode(int code) {
            return NONE.code == code || TEXT.code == code || FILE.code == code || URL.code == code;
        }

    }

    /**
     * 导入注解属性参数
     */
    @Data
    static class ImportClassField {
        // field 原属性
        private Field field;
        // 字段名称
        private String classFieldName;
        // 列名称
        private String columnName;
        // 列索引
        private int columnIndex = -1;
        // 时间格式
        private String dateFormat;
        // 键值对
        private LinkedHashMap<String, String> kvMap;
        // 列宽
        private int columnWidth;
    }

    /**
     * 导出注解属性参数
     */
    @Data
    static class ExportClassField {
        // 字段名称
        private String classFieldName;
        // excel表头名称
        private String columnName;
        // exel列索引
        private int columnIndex = -1;
        // 列宽
        private int columnWidth;
        // 时间格式
        private String dateFormat;
        // 批注
        private String comment;
        // 键值对
        private LinkedHashMap<String, String> kvMap;
        // field 原属性
        private Field field;
    }

    @Data
    static class ExportWorkBook {
        // 文件名称（不用带文件尾缀）
        private String name;
        // 页数据
        private List<ExportSheet> sheets;
    }

    /**
     * 导出模板
     */
    @Data
    static class ExportSheet {
        // 页名称
        private String name;
        // 表头样式
        private ExportCellStyle headerStyle;
        // 数据样式
        private ExportCellStyle dataStyle;
        // 数据
        private List<ExportRow> rows;
        // 批注
        private List<ExportComment> comments;
        // 水印
        private ExportWatermark watermark;
        // 是否冻结表头所在行
        private boolean freezeHeader;
        // 全局列宽
        private int columnWidth;
        // 指定列宽
        private Map<Integer, Integer> columnIndexWidthMap;
    }

    public static class ExportSheetFactory {
        public static <T> ExportSheet createExportSheet(List<T> list, Class<T> c) {
            // 类名注解
            ExcelSheet excelSheet = c.getAnnotation(ExcelSheet.class);
            String name = "";
            boolean freezeHeader = false;
            ExportWatermark watermark = null;
            int columnWidth = 0;
            if (excelSheet != null) {
                name = excelSheet.name();
                if (ExcelUtils.isEmpty(name)) {
                    name = excelSheet.value();
                }
                freezeHeader = excelSheet.freezeHeader();
                Watermark wmk = excelSheet.watermark();
                watermark = new ExportWatermark();
                watermark.setType(wmk.type());
                watermark.setSrc(wmk.src());
                columnWidth = excelSheet.columnWidth();
            }
            // 属性注解
            List<ExportClassField> exportFields = filterExportFields(c);
            Map<Integer, Integer> columnIndexWidthMap = getSheetColumnIndexWidthMap(exportFields);
            List<ExportRow> rows = getSheetRows(exportFields, list);
            List<ExportComment> comments = getSheetComments(exportFields);
            // 构建导出Sheet对象
            ExportSheet exportSheet = new ExportSheet();
            exportSheet.setName(name);
            exportSheet.setHeaderStyle(new ExportCellStyle());
            exportSheet.setDataStyle(new ExportCellStyle());
            exportSheet.setRows(rows);
            exportSheet.setComments(comments);
            exportSheet.setWatermark(watermark);
            exportSheet.setFreezeHeader(freezeHeader);
            exportSheet.setColumnWidth(columnWidth);
            exportSheet.setColumnIndexWidthMap(columnIndexWidthMap);
            return exportSheet;
        }

        private static Map<Integer, Integer> getSheetColumnIndexWidthMap(List<ExportClassField> exportFields) {
            Map<Integer, Integer> columnIndexWidthMap = new HashMap<>();
            for (int i = 0; i < exportFields.size(); i++) {
                columnIndexWidthMap.put(i, exportFields.get(i).getColumnWidth());
            }
            return columnIndexWidthMap;
        }

        private static List<ExportClassField> filterExportFields(Class<?> c) {
            List<ExportClassField> excelImportClassFields = new ArrayList<>();
            List<Field> allClassFields = ExcelUtils.getAllFields(c);
            for (Field field : allClassFields) {
                ExcelExport ex = field.getAnnotation(ExcelExport.class);
                if (Objects.nonNull(ex)) {
                    field.setAccessible(true);
                    ExportClassField exportField = new ExportClassField();
                    exportField.setClassFieldName(field.getName());
                    String name = ex.name();
                    if (ExcelUtils.isEmpty(name)) {
                        name = ex.value();
                    }
                    exportField.setColumnName(name);
                    exportField.setColumnIndex(ex.columnIndex());
                    exportField.setColumnWidth(ex.columnWidth());
                    exportField.setDateFormat(ex.dateFormat());
                    exportField.setComment(ex.comment());
                    LinkedHashMap<String, String> kvMap = new LinkedHashMap<>();
                    KV[] kvs = ex.kvs();
                    for (KV kv : kvs) {
                        kvMap.put(kv.k(), kv.v());
                    }
                    exportField.setKvMap(kvMap);
                    exportField.setField(field);
                    excelImportClassFields.add(exportField);
                }
            }
            return excelImportClassFields;
        }

        private static <T> List<ExportRow> getSheetRows(List<ExportClassField> exportFields, List<T> list) {
            List<ExportRow> rows = new ArrayList<>();
            rows.addAll(initHeaderRows(exportFields));
            rows.addAll(initDataRows(exportFields, list));
            return rows;
        }

        private static List<ExportRow> initHeaderRows(List<ExportClassField> exportFields) {
            List<ExportCell> headerCells = new ArrayList<>();
            for (ExportClassField cf : exportFields) {
                ExportCell cell = new ExportCell();
                cell.setValue(cf.getColumnName());
                headerCells.add(cell);
            }
            ExportRow headerRow = new ExportRow();
            headerRow.setHeader(true);
            headerRow.setCells(headerCells);
            List<ExportRow> headerRows = new ArrayList<>();
            headerRows.add(headerRow);
            return headerRows;
        }

        private static <T> List<ExportRow> initDataRows(List<ExportClassField> exportFields, List<T> list) {
            List<ExportRow> dataRows = new ArrayList<>();
            for (T t : list) {
                List<ExportCell> dataCells = new ArrayList<>();
                for (ExportClassField cf : exportFields) {
                    ExportCell cell = new ExportCell();
                    cell.setDateFormat(cf.getDateFormat());
                    try {
                        Object o = cf.getField().get(t);
                        // 判断该属性是否有映射，如果有映射，则转化为映射值
                        if (!cf.getKvMap().isEmpty()) {
                            o = cf.getKvMap().get(o.toString());
                        }
                        cell.setValue(o);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("获取属性值失败: " + cf.getField().getName(), e);
                    }
                    dataCells.add(cell);
                }
                ExportRow dataRow = new ExportRow();
                dataRow.setHeader(false);
                dataRow.setCells(dataCells);
                dataRows.add(dataRow);
            }
            return dataRows;
        }

        private static List<ExportComment> getSheetComments(List<ExportClassField> exportFields) {
            List<ExportComment> comments = new ArrayList<>();
            for (int i = 0; i < exportFields.size(); i++) {
                ExportClassField cf = exportFields.get(i);
                if (!isEmpty(cf.getComment())) {
                    ExportComment comment = new ExportComment();
                    comment.setX(i);
                    comment.setY(0);
                    comment.setComment(cf.getComment());
                    comments.add(comment);
                }
            }
            return comments;
        }
    }

    @Data
    static class ExportRow {
        private boolean header;
        private List<ExportCell> cells;
    }

    @Data
    static class ExportCellStyle {
        // 背景颜色
        private short backgroundColor;

    }

    @Data
    static
    class ExportCell {
        private Object value;
        private String dateFormat;
    }

    @Data
    static class ExportComment {
        // 横坐标
        private int x;
        // 纵坐标
        private int y;
        // 备注
        private String comment;
    }

    @Data
    static class ExportWatermark {

        /**
         * 1-文本文字水印;
         * 2-本地图片水印;
         * 3-网络图片水印；
         * 更多查看 {@link WatermarkType#code}
         */
        private int type;

        /**
         * 当type=1时：src为文字内容，如：xxx科技有效公司
         * 当type=2时：src为本地文件路径，如：D:\img\icon.png
         * 当type=3时：src为网络图片水印，如：https://profile-avatar.csdnimg.cn/624a0ef43e224cb2bf5ffbcca1211e51_sunnyzyq.jpg
         */
        private String src;

    }
}
