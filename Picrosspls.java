
import java.awt.*;

import static java.lang.System.exit;

public class Picrosspls {
    int res;
    boolean usermap[][];
    String usermapx,usermapy;
    int mapx[][],mapy[][];
    Font f = new Font(StdDraw.getFont().getName(),StdDraw.getFont().getStyle(),10);
    final Font bigfont = new Font(StdDraw.getFont().getName(),StdDraw.getFont().getStyle(),20);

    public Picrosspls(String usermapx, String usermapy){
        this.usermapy = usermapy;
        this.usermapx = usermapx;
        init();
    }
    public Picrosspls(){
    }

    public static void main(final String[] args) {
        String usermapx = "3,4,5,4,5,6,3-2-1,2-2-5,4-2-6,8-2-3,8-2-1-1,2-6-2-1,4-6,2-4,1";
        String usermapy = "3,5,4-3,7,5,3,5,1-8,3-3-3,7-3-2,5-4-2,8-2,10,2-3,6";
        //String usermapy = "3,1,1-2,1-1";
        //String usermapx = "1-2,1,3,2";
        //String usermapx="3-1,2-1,2,0,3-1";
        //String usermapy="1-1,2-1,2-1,1,3-1";
        Picrosspls p = new Picrosspls(usermapx, usermapy);
    }

    private void init() {
        mapx = parseUsermapString (usermapx);
        mapy = parseUsermapString (usermapy);
        res = mapx.length;
        f = new Font(StdDraw.getFont().getName(),StdDraw.getFont().getStyle(),10+(int)(10*(1.0/res)));
        usermap = new boolean[res+1][res+1];
        StdDraw.show(0);
        StdDraw.setPenColor(StdDraw.BLACK);
        drawField();
        loadMap();
        game();
    }

    public void game () {
        while (true) {
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            if (StdDraw.mousePressed()) {
                int xround = (int)(x*(res+1)) ;
                int yround = (int)(y*(res+1)) ;
                if (xround<=res && xround>0 && yround<res && yround>=0) {
                    usermap[xround][yround] = !usermap[xround][yround]; //toggle field
                    //System.out.println(xround+","+yround);
                }
                else if (xround==0 && yround==res){
                    if(checkUser(true,true) && checkUser(false,true)) {
                        StdDraw.clear();
                        StdDraw.setFont(bigfont);
                        StdDraw.text(0.5,0.5,"Congratulations!");
                        StdDraw.show(300);
                        try {
                            Thread.sleep(300);
                        } catch (Exception e) {}
                        while (true) {
                            if (StdDraw.mousePressed()) {
                                exit(0);
                            }
                        }

                    }
                }
                StdDraw.clear();
                drawField();
                StdDraw.show(100);
            }
            StdDraw.show(10);
        }
    }

    public boolean checkUser (boolean checkx, boolean visual){
        for (int i=0; i<res; i++) {
            int clusterlength = checkx ? mapx[i][0] : mapy[i][0];
            int currentlength = 0;
            int cluster = 0;
            int clusteramount = 0;
            boolean linefinished = false;

            for (int x=0;x<(checkx ? mapx[i].length : mapy[i].length);x++) {
                if ((checkx ? mapx[i][x] : mapy[i][x])!=0) clusteramount++;
            }

            for (int j=0; (checkx ? j<=res : j<res); j++) {
                int ilocal = checkx ? i+1 : j+1;
                int jlocal = checkx ? res - j : res-i-1;

                if (visual) {
                    double ix = (double)((ilocal+1))/(res+1)-0.5/(res+1);
                    double iy = (double)(jlocal+1)/(res+1)-0.5/(res+1);
                    double radius = 0.5/(res+1);
                    StdDraw.setPenColor(StdDraw.YELLOW);
                    StdDraw.filledSquare(ix,iy,radius);
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.square(ix,iy,radius);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.show();
                }

                if (usermap[ilocal][jlocal] && linefinished) {
                    return false;
                }
                if (usermap[ilocal][jlocal])
                    currentlength++;
                if ((!(usermap[ilocal][jlocal]) && (currentlength-1>=0)) ||(currentlength!=0 && (checkx ? j==res : j+1==res))){
                    if (currentlength==clusterlength) {
                        cluster++;
                        if (cluster < clusteramount) {
                            clusterlength = checkx ? mapx[i][cluster] : mapy[i][cluster];
                        } else {
                            linefinished = true;
                        }
                        currentlength = 0;
                    } else {
                        return false;
                    }
                }
            }
            if (cluster!= clusteramount) {
                return false;
            }
        }
        if (visual) {
            StdDraw.clear();
            drawField();
        }
        return true;
    }

    public void loadMap() {
        for (int i=0;i<res;i++) {
            String tempx = "";
            String tempy = "";
            for (int j=0;j<mapx[0].length;j++) {
                if (mapx[i][j]!=0)
                    tempx += mapx[i][j] + ("-");
            }
            for (int j=0;j<mapy[0].length;j++) {
                if (mapy[i][j]!=0)
                    tempy += mapy[i][j] + ("-");
            }
            if (tempx.length()-1>=0)
                tempx = tempx.substring(0,tempx.length()-1);
            else
                tempx = "0";
            if (tempy.length()-1>=0)
                tempy = tempy.substring(0,tempy.length()-1);
            else
                tempy = "0";
            StdDraw.setFont(f);
            StdDraw.textRight((i+2.0)/(res+1),1-0.5/(res+1),tempx);
            StdDraw.textRight(1.0/(res+1),((res-i)-0.5)/(res+1),tempy);
        }
    }

    public void drawField() {
        StdDraw.setFont(f);
        StdDraw.text(0.5/(res+1),1-(0.5/(res+1)),"Verify");
        for (int x=0;x<=res;x++) {
            for (int y=0;y<=res;y++) {
                double ix = (double)(x+1)/(res+1)-0.5/(res+1);
                double iy = (double)(y+1)/(res+1)-0.5/(res+1);
                double radius = 0.5/(res+1);
                if ( usermap[x][y]) {
                    StdDraw.filledSquare(ix,iy,radius);
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.square(ix,iy,radius);
                    StdDraw.setPenColor(StdDraw.BLACK);
                } else {
                    StdDraw.square(ix,iy,radius);
                }
            }
        }
        loadMap();
    }

    public static int [][] parseUsermapString (String map) {
        int [][] output;
        int maxamount = 1;
        int commaamount = 1;
        int currentamount = 0;
        boolean neuefolge = true;
        for (int i = 0; i<map.length(); i++) {
            if (map.charAt(i)=='-') {
                if (neuefolge) {
                    currentamount = 2;
                    neuefolge = false;
                } else {
                    currentamount++;
                }
                if (currentamount>maxamount)
                    maxamount = currentamount;
            } else if (map.charAt(i)==',') {
                neuefolge = true;
                currentamount = 0;
                commaamount++;
            }
        }
        output = new int[commaamount][maxamount];
        String currentnumber = "";
        for (int i = 0, comma=0, number=0; i<map.length(); i++) {
            if (map.charAt(i)==',') {
                number = 0;
                comma++;
                currentnumber = "";
            } else if (map.charAt(i)!='-' && i+1<map.length() && ((map.charAt(i+1)!='-') && (map.charAt(i+1)!=','))) {
                currentnumber += map.charAt(i);
            } else if (map.charAt(i)!='-') {
                currentnumber += map.charAt(i);
                try {
                    output[comma][number] = Integer.parseInt(currentnumber);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                    exit(-1);
                }
                number++;
                currentnumber = "";
            }
        }
        return output;
    }


}
