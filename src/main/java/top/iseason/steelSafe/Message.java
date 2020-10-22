package top.iseason.steelSafe;

public class Message {
    public static String replace(String message, String... replace) {
        if (replace.length % 2 != 0) {
            throw new IllegalArgumentException("replace 参数应为2的倍数！");
        }
        for (int count = 0; count < replace.length; count += 2) {
            message = message.replace(replace[count], replace[count + 1]);//新的替换旧的
        }
        return message;
    }
}
