package edu.lmu.cs.networking;

/**
 * Created by qbbii on 22.12.2016.
 */
public class MoveParser {


    public static int parseXToInt(String s){//-1 +0
        int tmpX = 0;

        if (s.charAt(0) == '+')
            tmpX+= Integer.parseInt(String.valueOf(s.charAt(1)));
        else
            tmpX += -1 * Integer.parseInt(String.valueOf(s.charAt(1)));
        return tmpX;
    }
    public static int parseYToInt(String s){//-1 +0


        int tmpY = 0;

        if (s.charAt(2) == '+')
            tmpY+= Integer.parseInt(String.valueOf(s.charAt(3)));
        else
            tmpY += -1 * Integer.parseInt(String.valueOf(s.charAt(3)));
        return tmpY;

    }

    public static String intToString(int x, int y){//-1 +0

        return ((x >= 0)?"+"+x:"-"+(-x)) + ((y>=0)?"+"+y:"-"+(-y));
    }
}
