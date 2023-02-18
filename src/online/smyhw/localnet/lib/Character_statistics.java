package online.smyhw.localnet.lib;

/**
 * 统计字符串内一共出现过多少目标字符
 * 可以接受字符数组或String对象
 *
 * @author smyhw
 */
public class Character_statistics {
    public static int find(String input, char target) {
        int re = 0;
        char[] input_s = input.toCharArray();
        re = find(input_s, target);
        return re;
    }

    public static int find(char[] input, char target) {
        int re = 0;
        int temp = 0;
        while (true) {
            if (temp >= input.length) {
                break;
            }
            if (input[temp] == target) {
                re++;
            }
            temp++;
        }
        return re;
    }
}
