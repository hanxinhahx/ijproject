package test;

import java.util.Random;

public class RandomTesdt {
    public static void main(String[] args) {
        Random random=new Random();
        for (int i=1;i<=10;i++){
            System.out.println((char)(random.nextInt(66)+58));
        }
    }
}
