package com.zfoo.storage.util;

import com.zfoo.protocol.util.StringUtils;
import org.apache.poi.ss.usermodel.*;

/**
 * @author islandempty
 * @since 2021/6/22
 **/
public class CellUtils {

    /**
     * 获取单元格的值(Date、Double、Boolean、String)
     *
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell){
        return getCellValue(cell , cell.getCellType());
    }


    public static String getCellStringValue(Cell cell) {
        return getCellValue(cell).toString().trim();
    }
    /**
     * 获取单元格的值
     * 如果单元格值为数字格式，则判断其格式中是否有小数部分，无则返回Long类型，否则返回Double类型
     *
     * @param cell
     * @param cellType
     * @return
     */
    public static Object getCellValue(Cell cell, CellType cellType){
        Object value = null;
        switch (cellType){
            case NUMERIC:
                value = getNumericValue(cell);
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case FORMULA:
                //遇到公式时查找公式结果类型
                value = getCellValue(cell , cell.getCachedFormulaResultType());
                break;
            case BLANK:
                value = StringUtils.EMPTY;
                break;
            case ERROR:
                final FormulaError error = FormulaError.forInt(cell.getErrorCellValue());
                value = (null == error) ? StringUtils.EMPTY :error.getString();
                break;
            default:
                value : cell.getStringCellValue();
        }

        return value;
    }


    /**
     * 获取数字类型的单元值
     *
     * @param cell
     * @return 单元格值，可能为Long、Double、Date
     */
    private static Object getNumericValue(Cell cell){
        //获取数字部分单元格值
        var value = cell.getNumericCellValue();
        //单元格的类型
        var style = cell.getCellStyle();
        if (null == style){
            return value;
        }

        //判断是否为日期
        if (DateUtil.isCellDateFormatted(cell)){
            return cell.getDateCellValue();
        }

        var dataFormatString = style.getDataFormatString();
        //是普通数字
        if (null != dataFormatString && dataFormatString.contains(StringUtils.PERIOD)){
            var longValue = (long)value;
            if (longValue == value){
                //对于没有小数部分的数字类型，转换为Long
                return longValue;
            }
        }
        return value;
    }
}

