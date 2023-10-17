package comp5703.sydney.edu.au.learn.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class TimeCalculateUtil {

    public static String getTimeElapsed(Long offerSentTime) {

        // 获取当前时间戳（以毫秒为单位）
        long currentTimestamp = System.currentTimeMillis();

        // 计算时间差（以毫秒为单位）
        long timeDifferenceMillis = currentTimestamp - offerSentTime;

        // 将时间差转换为秒、分钟、小时和天
        long timeDifferenceSeconds = timeDifferenceMillis / 1000;
        long timeDifferenceMinutes = timeDifferenceSeconds / 60;
        long timeDifferenceHours = timeDifferenceMinutes / 60;
        long timeDifferenceDays = timeDifferenceHours / 24;

        SimpleDateFormat sdf;

        // 根据时间差的大小来决定显示的格式
        if (timeDifferenceDays < 1) {
            sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(new Date(offerSentTime));
        } else {
            sdf = new SimpleDateFormat("MM-dd");
            return sdf.format(new Date(offerSentTime));
        }
    }

}
