package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String server_address = "localhost";
        int server_port = 8189;
        DataInputStream in;
        DataOutputStream out;
        Scanner xClientTextScan = new Scanner(System.in);
        try (Socket xSocket = new Socket(server_address, server_port)) {
            in = new DataInputStream(xSocket.getInputStream());
            out = new DataOutputStream(xSocket.getOutputStream());
            Thread xThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String msgFromServer = in.readUTF();
                            if (msgFromServer.equals("/end")) {
                                break;
                            }
                            System.out.println("Server: " + msgFromServer);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getStackTrace());
                    }
                }
            });

            Thread xThread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        while (true) {
                            String textToSend = xClientTextScan.nextLine();
                            if (textToSend.equals("/end")) {
                                out.writeUTF(textToSend);
                                break;
                            }
                            out.writeUTF(textToSend);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getStackTrace());
                    }
                }
            });

            xThread.start();
            xThread2.start();
            xThread.join();
            xThread2.join();


        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}
