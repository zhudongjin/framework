package com.hstypay.sandbox.support.text;

import com.hstypay.sandbox.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据格式化
 *
 * @author Tinnfy Lee
 */
public class DataFormatter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataFormatter.class);

    /**
     * 格式化日期
     *
     * @param date
     * @param format 格式字串，与java SimpleDateFormat的格式串相同
     * @return
     */
    public static String formatDate(Date date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Throwable t) {
            LOGGER.error("formatDate fail,{},{}", format, t.toString());
        }

        return Constant.EMPTY;
    }

    /**
     * 格式化float类型数字
     *
     * @param data
     * @param style DecimalFormat的格式化字串风格
     * @return
     */
    public static String formatDecimal(float data, String style) {
        try {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        } catch (Throwable t) {
            LOGGER.error("formatDate fail,{},{},{}", data, style, t.toString());
        }

        return Constant.EMPTY;
    }

    /**
     * 格式化Float类型数字
     *
     * @param data
     * @param style DecimalFormat的格式化字串风格
     * @return
     */
    public static String formatDecimal(Float data, String style) {
        try {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        } catch (Throwable t) {
            LOGGER.error("formatDate fail,{},{},{}", data, style, t.toString());
        }

        return Constant.EMPTY;
    }

    /**
     * 格式化double类型数字
     *
     * @param data
     * @param style DecimalFormat的格式化字串风格
     * @return
     */
    public static String formatDecimal(double data, String style) {
        try {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        } catch (Throwable t) {
            LOGGER.error("formatDate fail,{},{},{}", data, style, t.toString());
        }

        return Constant.EMPTY;
    }

    /**
     * 格式化Double类型数字
     *
     * @param data
     * @param style DecimalFormat的格式化字串风格
     * @return
     */
    public static String formatDecimal(Double data, String style) {
        try {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        } catch (Throwable t) {
            LOGGER.error("formatDate fail,{},{},{}", data, style, t.toString());
        }

        return Constant.EMPTY;
    }

    /**
     * 格式化BigDecimal类型数字
     *
     * @param data
     * @param style DecimalFormat的格式化字串风格
     * @return
     */
    public static String formatDecimal(BigDecimal data, String style) {
        try {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        } catch (Throwable t) {
            LOGGER.error("formatDate fail,{},{},{}", data, style, t.toString());
        }

        return Constant.EMPTY;
    }

    /**
     * 格式化long类型数字
     *
     * @param data
     * @param style DecimalFormat的格式化字串风格
     * @return
     */
    public static String formatDecimal(long data, String style) {
        try {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        } catch (Throwable t) {
            LOGGER.error("formatDate fail,{},{},{}", data, style, t.toString());
        }

        return Constant.EMPTY;
    }

    /**
     * 格式化Long类型数字
     *
     * @param data
     * @param style DecimalFormat的格式化字串风格
     * @return
     */
    public static String formatDecimal(Long data, String style) {
        try {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        } catch (Throwable t) {
            LOGGER.error("formatDate fail,{},{},{}", data, style, t.toString());
        }

        return Constant.EMPTY;
    }

    /**
     * 格式化int类型数字
     *
     * @param data
     * @param style DecimalFormat的格式化字串风格
     * @return
     */
    public static String formatDecimal(int data, String style) {
        try {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        } catch (Throwable t) {
            LOGGER.error("formatDate fail,{},{},{}", data, style, t.toString());
        }

        return Constant.EMPTY;
    }

    /**
     * 格式化Integer类型数字
     *
     * @param data
     * @param style DecimalFormat的格式化字串风格
     * @return
     */
    public static String formatDecimal(Integer data, String style) {
        try {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        } catch (Throwable t) {
            LOGGER.error("formatDate fail,{},{},{}", data, style, t.toString());
        }

        return Constant.EMPTY;
    }

    /**
     * 金额的分转换为元
     *
     * @param data
     * @return
     */
    public static BigDecimal fen2yuan(long data) {
        try {
            BigDecimal money = new BigDecimal(data);
            return money.movePointLeft(2);
        } catch (Throwable t) {
            LOGGER.error("fen2yuan fail,{},{}", data, t.toString());
        }

        return new BigDecimal("0.00");
    }

    /**
     * 金额的分转换为元
     *
     * @param data
     * @return
     */
    public static BigDecimal fen2yuan(Long data) {
        try {
            if (data == null)
                return new BigDecimal("0.00");

            BigDecimal money = new BigDecimal(data);
            return money.movePointLeft(2);
        } catch (Throwable t) {
            LOGGER.error("fen2yuan fail,{},{}", data, t.toString());
        }

        return new BigDecimal("0.00");
    }

    /**
     * 金额的元转换为分
     *
     * @param data
     * @param roundingMode 舍入模式 0-UP,1-DOWN,2-CEILING,3-FLOOR,4-HALF_UP,
     *                     5-HALF_DOWN,6-HALF_EVEN,7-UNNECESSARY
     * @return
     */
    public static Long yuan2fen(BigDecimal data, int roundingMode) {
        try {
            if (data == null)
                return 0L;

            data = data.movePointRight(2);
            data = data.setScale(0, roundingMode);
            return new Long(data.longValue());
        } catch (Throwable t) {
            LOGGER.error("fen2yuan fail,{},{}", data, t.toString());
            throw new RuntimeException("yuan2fen convert fail", t);
        }
    }

    /**
     * 金额的元转换为分
     *
     * @param data
     * @param roundingMode 舍入模式
     * @return
     */
    public static Long yuan2fen(BigDecimal data, RoundingMode roundingMode) {
        try {
            if (data == null)
                return 0L;

            data = data.movePointRight(2);
            data = data.setScale(0, roundingMode);
            return new Long(data.longValue());
        } catch (Throwable t) {
            LOGGER.error("fen2yuan fail,{},{}", data, t.toString());
            throw new RuntimeException("yuan2fen convert fail", t);
        }
    }

    /**
     * 金额的元转换为分，元必须是精确的2位小数，否则抛出转换异常
     *
     * @param data
     * @return
     */
    public static Long yuan2fen(BigDecimal data) {
        try {
            if (data == null)
                return 0L;

            data = data.movePointRight(2);
            data = data.setScale(0, BigDecimal.ROUND_UNNECESSARY);
            return new Long(data.longValue());
        } catch (Throwable t) {
            LOGGER.error("fen2yuan fail,{},{}", data, t.toString());
            throw new RuntimeException("yuan2fen convert fail", t);
        }
    }

    /**
     * 分转换为元，返回字串类型
     *
     * @param data
     * @return
     */
    public static String fen2yuanStr(long data) {
        try {
            BigDecimal money = new BigDecimal(data);
            money = money.movePointLeft(2);
            return money.toString();
        } catch (Throwable t) {
            LOGGER.error("fen2yuan fail,{},{}", data, t.toString());
        }

        return Constant.EMPTY;
    }

    /**
     * 分转换为元，返回字串类型
     *
     * @param data
     * @return
     */
    public static String fen2yuanStr(Long data) {
        if (data == null)
            return Constant.EMPTY;

        return fen2yuanStr(data.longValue());
    }

    public static String movePointLeft(BigDecimal data, int n) {
        try {
            if (data == null)
                return Constant.EMPTY;

            data = data.movePointLeft(n);
            return data.toString();
        } catch (Throwable t) {
            LOGGER.error("movePointLeft fail,{},{}", data, t.toString());
        }

        return Constant.EMPTY;
    }

    public static String movePointLeft(long data, int n) {
        BigDecimal bd = new BigDecimal(data);
        return movePointLeft(bd, n);
    }

    public static String movePointLeft(Long data, int n) {
        BigDecimal bd = new BigDecimal(data);
        return movePointLeft(bd, n);
    }

    public static String movePointLeft(int data, int n) {
        BigDecimal bd = new BigDecimal(data);
        return movePointLeft(bd, n);
    }

    public static String movePointLeft(Integer data, int n) {
        BigDecimal bd = new BigDecimal(data);
        return movePointLeft(bd, n);
    }

    public static String movePointRight(BigDecimal data, int n) {
        try {
            if (data == null)
                return Constant.EMPTY;

            data = data.movePointRight(n);
            return data.toString();
        } catch (Throwable t) {
            LOGGER.error("movePointRight fail,{},{}", data, t.toString());
        }

        return Constant.EMPTY;
    }

    public static String movePointRight(long data, int n) {
        BigDecimal bd = new BigDecimal(data);
        return movePointRight(bd, n);
    }

    public static String movePointRight(Long data, int n) {
        BigDecimal bd = new BigDecimal(data);
        return movePointRight(bd, n);
    }

    public static String movePointRight(int data, int n) {
        BigDecimal bd = new BigDecimal(data);
        return movePointRight(bd, n);
    }

    public static String movePointRight(Integer data, int n) {
        BigDecimal bd = new BigDecimal(data);
        return movePointRight(bd, n);
    }

    public static void main(String[] args) {
        System.out.println(DataFormatter.formatDate(new Date(), "yyyy-MM-dd"));

        System.out.println(DataFormatter.formatDecimal(100, "# 0,0"));
        System.out.println(DataFormatter.formatDecimal(new BigDecimal(100), "# 0,0.00"));

        System.out.println(DataFormatter.fen2yuan(1001));
        System.out.println(DataFormatter.fen2yuan(new Long(1023)));

        System.out.println(DataFormatter.yuan2fen(new BigDecimal("10.23")));
        System.out.println(DataFormatter.yuan2fen(new BigDecimal("10.231"), BigDecimal.ROUND_DOWN));
        System.out.println(DataFormatter.yuan2fen(new BigDecimal("10.235"), BigDecimal.ROUND_DOWN));
        System.out.println(DataFormatter.yuan2fen(new BigDecimal("10.235"), BigDecimal.ROUND_UP));
        System.out.println(DataFormatter.yuan2fen(new BigDecimal("10.231"),
                BigDecimal.ROUND_HALF_UP));

        System.out.println(DataFormatter.fen2yuanStr(1001));
        System.out.println(DataFormatter.fen2yuanStr(new Long(1023)));

        System.out.println(DataFormatter.movePointLeft(1023L, 1));
        System.out.println(DataFormatter.movePointLeft(new Long(1023), 1));
        System.out.println(DataFormatter.movePointLeft(1053, 4));
        System.out.println(DataFormatter.movePointLeft(new Integer(1033), 4));
        System.out.println(DataFormatter.movePointLeft(new BigDecimal("123.54"), 4));

        System.out.println(DataFormatter.movePointRight(1023L, 1));
        System.out.println(DataFormatter.movePointRight(new Long(1023), 1));
        System.out.println(DataFormatter.movePointRight(1053, 4));
        System.out.println(DataFormatter.movePointRight(new Integer(1033), 4));
        System.out.println(DataFormatter.movePointRight(new BigDecimal("123.222254"), 4));
    }
}
