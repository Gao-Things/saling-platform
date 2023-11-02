package comp5703.sydney.edu.au.learn.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public abstract class TimeCalculateUtil {

    public static String getTimeElapsed(Long offerSentTime) {

//        // 获取当前时间戳（以毫秒为单位）
//        long currentTimestamp = System.currentTimeMillis();
//
//        // 计算时间差（以毫秒为单位）
//        long timeDifferenceMillis = currentTimestamp - offerSentTime;
//
//        // 将时间差转换为秒、分钟、小时和天
//        long timeDifferenceSeconds = timeDifferenceMillis / 1000;
//        long timeDifferenceMinutes = timeDifferenceSeconds / 60;
//        long timeDifferenceHours = timeDifferenceMinutes / 60;
//        long timeDifferenceDays = timeDifferenceHours / 24;
//
//        SimpleDateFormat sdf;
//
//        // 根据时间差的大小来决定显示的格式
//        if (timeDifferenceDays < 1) {
//            sdf = new SimpleDateFormat("HH:mm");
//            return sdf.format(new Date(offerSentTime));
//        } else {
//            sdf = new SimpleDateFormat("MM-dd");
//            return sdf.format(new Date(offerSentTime));
//        }

        // 创建 SimpleDateFormat 对象，格式为月-日 小时:分钟
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());

        // 直接使用 SimpleDateFormat 格式化 offerSentTime
        return sdf.format(new Date(offerSentTime));
    }

    public static String convertTimestampToDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.ENGLISH);  // 使用英语作为地区设置
        return sdf.format(date);
    }

    public static String getTimeDifference(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        if (diff < TimeUnit.MINUTES.toMillis(1)) {
            return diff / 1000 + " S";
        } else if (diff < TimeUnit.HOURS.toMillis(1)) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(minutes);
            return minutes + " M " + seconds + " S";
        } else if (diff < TimeUnit.DAYS.toMillis(1)) {
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            long remainDiff = diff - TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(remainDiff);
            return hours + " H " + minutes + " M";
        } else {
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            return days + " Day";
        }
    }

}
