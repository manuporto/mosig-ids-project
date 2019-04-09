package overlay.util;

import java.io.File;

public class GraphicalVisualization
{
    public static void main(String[] args)
    {
        GraphicalVisualization p = new GraphicalVisualization();
        p.start();
//		p.start2();
    }

    /**
     * Construct a DOT graph in memory, convert it
     * to image and store the image in the file system.
     */
    private void start()
    {
        GraphViz gv = new GraphViz();
        gv.addln(gv.start_graph());
       /*Ring Physical Topology
        gv.addln("1 -- 3;");
        gv.addln("2 -- 3;");
        gv.addln("2 -- 4;");
        gv.addln("2 -- 5;");
        gv.addln("4 -- 5;");
        */
       /*Ring Virtual Topology */
        gv.addln("1 -- 2;");
        gv.addln("1 -- 4;");
        gv.addln("2 -- 5;");
        gv.addln("3 -- 4;");
        gv.addln("3 -- 5;");

        gv.addln(gv.end_graph());
        System.out.println(gv.getDotSource());

        gv.increaseDpi();   // 106 dpi

        String type = "pdf";
        String repesentationType= "dot";

        File out = new File("res/RingVTopology"+"."+ type);   // Linux
        //      File out = new File("c:/eclipse.ws/graphviz-java-api/out." + type);    // Windows
        gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
    }

    /**
     * Read the DOT source from a file,
     * convert to image and store the image in the file system.
     */
    private void start2()
    {
        String dir = "/home/jabba/Dropbox/git.projects/laszlo.own/graphviz-java-api";     // Linux
        String input = dir + "/sample/simple.dot";
        //	   String input = "c:/eclipse.ws/graphviz-java-api/sample/simple.dot";    // Windows

        GraphViz gv = new GraphViz();
        gv.readSource(input);
        System.out.println(gv.getDotSource());

        String type = "gif";
        //    String type = "dot";
        //    String type = "fig";    // open with xfig
        //    String type = "pdf";
        //    String type = "ps";
        //    String type = "svg";    // open with inkscape
        //    String type = "png";
        //      String type = "plain";


        String repesentationType= "dot";
        //		String repesentationType= "neato";
        //		String repesentationType= "fdp";
        //		String repesentationType= "sfdp";
        // 		String repesentationType= "twopi";
        //		String repesentationType= "circo";

        File out = new File("/tmp/simple." + type);   // Linux
        //	   File out = new File("c:/eclipse.ws/graphviz-java-api/tmp/simple." + type);   // Windows
        gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
    }
}
