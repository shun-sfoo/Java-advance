package com.matrix.neo;

import edu.princeton.cs.algs4.StdIn;

public class Solution1 {

    //1.1.9
    // 将一个正整数N用二进制表示并转换为一个String类型的值
    // Integer.toBinaryString()
    private static void toBinaryString(int N) {
        String s = "";
        for (int i = N; i > 0; i /= 2) {
            s = (i % 2) + s;
        }
        System.out.println(s);
    }

    // 1.1.11
    // 打印出一个二位布尔数组的内容。其中 * 表示为真， 空格表示为假， 打印出行号和列号
    private static void printBooleanArray(boolean[][] array) {
        int row = array.length;
        int col = array[0].length;

        System.out.printf("%2s", "\\");
        for (int i = 0; i < col; i++)
            System.out.printf("%2s", i);

        System.out.println();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j <= col; j++) {
                if (j == 0) {
                    System.out.printf("%2s", i);
                } else {
                    if (array[i][j - 1]) {
                        System.out.printf("%2s", "*");
                    } else {
                        System.out.printf("%2s", "_");
                    }
                }
            }
            System.out.println();
        }

    }

    // 1.1.13
    // 编写一段代码，打印出一个M行N列的二维数组的转置
    private static void MxN2NxM(Integer[][] mn) {
        int row = mn.length;
        int col = mn[0].length;
        Integer[][] newArray = new Integer[col][row];

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                newArray[i][j] = mn[j][i];
            }
        }
        printArray(newArray);
    }

    private static void printArray(Object[][] array) {
        int row = array.length;
        int col = array[0].length;
        System.out.printf("%2s", "\\");
        for (int i = 0; i < col; i++)
            System.out.printf("%2s", i);

        System.out.println();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j <= col; j++) {
                if (j == 0) {
                    System.out.printf("%2s", i);
                } else {
                    System.out.printf("%2s", array[i][j - 1]);
                }
            }
            System.out.println();
        }
    }

    // 1.1.14
    // 返回不大于log2N的最大整数
    private static int lg(int N) {
        int p = 0;
        for (int i = 2; i <= N; i *= 2) {
            p++;
        }
        return p;
    }

    // 1.1.15
    private static int[] histogram(int[] array, int M) {
        int[] newArray = new int[M];
        for (int e : array) {
            if (e >= M)
                continue;
            newArray[e]++;
        }
        return newArray;
    }

    // 1.1.19 fibonacci
    public static int fibonacci(int N, int[] array) {

        if (N == 0) return 0;
        if (N == 1) return 1;

        if (array[N] != 0)
            return array[N];
        array[N] = fibonacci(N - 2, array) + fibonacci(N - 1, array);
        return array[N];
    }


    public static int binarySearch(int[] array, int lo, int hi, int key, int depth) {
        if (lo > hi)
            return -1;
        System.out.printf("lo : %d, hi : %d, depth : %d%n", lo, hi, depth);
        int mid = lo + (hi - lo) / 2;
        if (key > array[mid]) {
            binarySearch(array, mid + 1, hi, key, depth + 1);
        } else if (key < array[mid]) {
            binarySearch(array, lo, mid - 1, key, depth + 1);
        }
        return mid;
    }

    public static int euclid(int p, int q) {
        if (q == 0)
            return p;

        System.out.printf("p : %d, q : %d\n", p, q);
        return euclid(q, (p % q));
    }


    // 1.2.6 判断两个字符串是否护卫回环变位
    // 字符串是s中的字符循环移动任意位置后能够得到另一个字符串t， s被称为t的回环变位
    public boolean isCircularRotation(String s, String t) {
        // 12345 + 12345
        // 34512
        return s.length() == t.length() && (s + s).indexOf(t) > 0;
    }

    public static void main(String[] args) {
        toBinaryString(4);
        System.out.println();
        boolean[][] array = {
                {true, false, true, false},
                {false, false, true, false},
                {true, false, true, false},
        };
        printBooleanArray(array);
        System.out.println();

        Integer[][] mn = {
                {1, 1, 1, 1, 1},
                {2, 2, 2, 2, 2},
                {3, 3, 3, 3, 3},
                {4, 4, 4, 4, 4},
        };

        printArray(mn);
        System.out.println();
        MxN2NxM(mn);
        System.out.println();

        int N = 10;
        int[] fib = new int[N + 1];
        System.out.println(fibonacci(N, fib));

        System.out.println(euclid(1_111_111, 1_234_567));
    }

}
