package zzjz.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * @author 房桂堂
 * @description Test3
 * @date 2019/1/15 15:35
 */
public class Test3 {

    public static void main(String[] args) throws IOException {


        String[] jobs = new String[]{"老师", "工人", "律师", "工程师", "学生"};

        String[] locations = new String[]{"江西省", "四川省", "安徽省", "广西壮族自治区", "广东省"};

        String[] education = new String[]{"本科", "初中", "高中", "小学", "硕士"};

        String[] collage = new String[]{"南京大学", "南京邮电大学", "复旦大学", "上海交通大学", "中国海洋大学"};

        String[] marriage = new String[]{"未婚", "已婚"};

        String[] health = new String[]{"健康", "亚健康", "不健康"};

        String[] poem = new String[]{"碧玉妆成一树高，万条垂下绿丝绦。不知细叶谁裁出，二月春风似剪刀。",
                                    "黄河远上白云间，一片孤城万仞山。羌笛何须怨杨柳，春风不度玉门关。",
                                    "荆吴相接水为乡，君去春江正渺茫。日暮征帆何处泊？天涯一望断人肠。",
                                    "琵琶起舞换新声，总是关山旧别情。撩乱边愁听不尽，高高秋月照长城。",
                                    "秦时明月汉时关，万里长征人未还。但使龙城飞将在，不教胡马度阴山。"};

        String pathname = "D:\\zzjz_work\\bigdata\\largetxt2.txt";
        File file = new File(pathname);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(pathname));

        for (int i = 1; i < 100000000; i++) {
            String line = i + "," + randInt(10, 90) + "," + jobs[i % 5] + "," + marriage[i % 2] + "," + education[i % 5] + "," +
                    randInt(4000, 20000) + "," + locations[i % 5] + "," + health[i % 3] + "," + collage[i % 5] + "," +
                    poem[i % 5];
            writer.write(line + "\r\n");
        }
        writer.close();

    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
