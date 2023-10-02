package comp5703.sydney.edu.au.learn.util;

public abstract class TimeCalculateUtil {

    public static String getTimeElapsed(Long offerSentTime) {
        // 将秒为单位的时间戳转换为毫秒为单位的时间戳
        offerSentTime = offerSentTime * 1000;

        // 获取当前时间戳（以毫秒为单位）
        long currentTimestamp = System.currentTimeMillis();

        // 计算时间差（以毫秒为单位）
        long timeDifferenceMillis = currentTimestamp - offerSentTime;

        // 将时间差转换为秒、分钟、小时和天
        long timeDifferenceSeconds = timeDifferenceMillis / 1000;
        long timeDifferenceMinutes = timeDifferenceSeconds / 60;
        long timeDifferenceHours = timeDifferenceMinutes / 60;
        long timeDifferenceDays = timeDifferenceHours / 24;

        // 根据时间差的大小来决定显示的格式
        if (timeDifferenceSeconds < 60) {
            return timeDifferenceSeconds + " seconds ago";
        } else if (timeDifferenceMinutes < 60) {
            return timeDifferenceMinutes + " minutes ago";
        } else if (timeDifferenceHours < 24) {
            return timeDifferenceHours + " hours ago";
        } else {
            return timeDifferenceDays + " days ago";
        }
    }

}
