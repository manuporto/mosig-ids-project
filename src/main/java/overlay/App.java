/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package overlay;

import java.io.IOException;

public class App {

    public String getGreeting() {
        return "Baibai";
    }

    public static void main(String[] args) throws IOException {
        ClientListener cl = new ClientListener();
        cl.start();
    }
}
