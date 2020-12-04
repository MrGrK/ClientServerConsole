package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        boolean xWork = true;

        try (ServerSocket xServer = new ServerSocket(8189)) {
            System.out.println("Сервер запущен...");
            try (Socket xSocket = xServer.accept()) {
                Scanner xServerScan = new Scanner(System.in);
                System.out.println("Клиент " + xSocket.getRemoteSocketAddress() + " подключен.");
                DataInputStream in = new DataInputStream(xSocket.getInputStream());

                DataOutputStream out = new DataOutputStream(xSocket.getOutputStream());

                Thread xThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            String strFromClient = null;
                            try {
                                strFromClient = in.readUTF();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (strFromClient.equals("/end")) {
                                break;
                            }
                            System.out.println("Client: " + strFromClient);
                        }
                    }
                });

                Thread xThread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            String strFromServer = null;
                            try {
                                strFromServer = xServerScan.nextLine();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (strFromServer.equals("/end")) {
                                try {
                                    out.writeUTF(strFromServer);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            try {
                                out.writeUTF(strFromServer);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
                xThread.start();
                xThread2.start();
                xThread.join();
                xThread2.join();
                /*new Thread(()->{

                    while (true) {
                        String strToSend = xServerScan.nextLine();
                        if(strToSend.equals("/end")){
                            break;
                        }
                        xScanFromServerMsg.println ("Server: " +strToSend);

                    }
                });*/
                System.out.println("Конец!");
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
