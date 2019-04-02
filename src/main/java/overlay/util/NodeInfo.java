package overlay.util;

public class NodeInfo {

    private int pTag;
    private int vTag;
    private int[][] pTop;
    private int[][] vTop;

    public NodeInfo(int pTag, int vYag, int[][] pTop, int[][] vTop) {
        this.pTag = pTag;
        this.vTag = vYag;
        this.pTop = pTop;
        this.vTop = vTop;
    }

    public NodeInfo() {
        this.pTag = 0;
        this.vTag = 0;
        this.pTop = new int[2][2];
        this.vTop = new int[2][2];
    }


    public int getpTag() {
        return pTag;
    }

    public void setpTag(int pTag) {
        this.pTag = pTag;
    }

    public int getvTag() {
        return vTag;
    }

    public void setvTag(int vYag) {
        this.vTag = vYag;
    }

    public int[][] getpTop() {
        return pTop;
    }

    public void setpTop(int[][] pTop) {
        this.pTop = pTop;
    }

    public int[][] getvTop() {
        return vTop;
    }

    public void setvTop(int[][] vTop) {
        this.vTop = vTop;
    }

}
