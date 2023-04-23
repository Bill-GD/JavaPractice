package endterm;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class FileTransfer extends Frame {
    Server server = null;
    static int windowX = 450, windowY = 300;
    boolean isServerOpened = false;

    Label labelNotification;
    Button toggleServer;
    TextField hostInput, portInputClient, portInputServer;
    TextArea fileInput;

    public FileTransfer(int mode) {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setLayout(null);

        // client side
        Label labelHostname = new Label("Hostname:");
        labelHostname.setBounds(30, 50, 65, 20);
        add(labelHostname);
        hostInput = new TextField("localhost");
        hostInput.setBounds(100, 50, 150, 20);
        add(hostInput);

        Label labelPort = new Label("Port:");
        labelPort.setBounds(30, 80, 40, 20);
        add(labelPort);
        portInputClient = new TextField("1000");
        portInputClient.setBounds(100, 80, 70, 20);
        add(portInputClient);

        Label labelFile = new Label("Filepath:");
        labelFile.setBounds(30, 110, 60, 20);
        add(labelFile);
        fileInput = new TextArea("D:\\text.txt", 2, 200, TextArea.SCROLLBARS_VERTICAL_ONLY);
        fileInput.setBounds(100, 110, 150, 60);
        add(fileInput);

        // send using the information given
        Button buttonSend = new Button("Send");
        buttonSend.addActionListener((e) -> {
            labelNotification.setText("");
            int port = Integer.parseInt(portInputClient.getText());
            try {
                int bindPort = port > 1 ? port - 1 : port + 1;
                DatagramSocket socket = new DatagramSocket(bindPort);
                InetAddress destinationAddress = InetAddress.getByName(hostInput.getText());

                File fileToSend = new File(fileInput.getText().replaceAll("\"", ""));

                if (!fileToSend.exists()) {
                    labelNotification.setText("File doesn't exist");
                    socket.close();
                    return;
                }
                // process file metadata to send
                FileInfo fileInfo = new FileInfo(fileToSend.getName(), fileToSend.length());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(fileInfo);

                byte[] bytesToSend = baos.toByteArray();
                socket.send(new DatagramPacket(bytesToSend, bytesToSend.length, destinationAddress, port));

                // process file content to send
                FileReader fr = new FileReader(fileToSend);
                int c;
                String contentToSend = "";
                while ((c = fr.read()) != -1) {
                    contentToSend += (char) c;
                }
                fr.close();
                bytesToSend = contentToSend.getBytes();
                socket.send(new DatagramPacket(bytesToSend, bytesToSend.length, destinationAddress, port));

                labelNotification.setText("Sent to " + destinationAddress.getHostAddress() + ':' + port);
                socket.close();
            } catch (Exception ex) {
                labelNotification.setText(ex.getMessage());
            }
        });
        buttonSend.setBounds(100, 180, 50, 30);
        add(buttonSend);

        // server side
        Label labelPortServer = new Label("Port:");
        labelPortServer.setBounds(280, 50, 30, 20);
        add(labelPortServer);
        portInputServer = new TextField("1000");
        portInputServer.setBounds(320, 50, 90, 20);
        add(portInputServer);

        toggleServer = new Button("Open Server");
        toggleServer.addActionListener((e) -> {
            try {
                if (isServerOpened) {
                    server.stopServer();
                    isServerOpened = false;
                    toggleServer.setLabel("Open Server");
                } else {
                    int port = Integer.parseInt(portInputServer.getText());
                    server = new Server(port);
                    server.start();
                    isServerOpened = true;
                    toggleServer.setLabel("Close Server");
                }
            } catch (Exception ex) {
                labelNotification.setText("Can't open server");
            }
        });
        toggleServer.setBounds(320, 80, 90, 30);
        add(toggleServer);

        labelNotification = new Label("", Label.CENTER);
        labelNotification.setBounds(0, windowY - 60, windowX, 50);
        add(labelNotification);
    }

    class Server extends Thread {
        int port;
        DatagramSocket serverSocket;
        byte[] receiveData;
        boolean isRunning = true;

        public Server(int port) throws Exception {
            this.port = port;
            serverSocket = new DatagramSocket(port);
            labelNotification.setText("Server opened (" +
                    InetAddress.getLocalHost().getHostAddress() +
                    ":" + port + ")");
        }

        public void run() {
            isRunning = true;
            while (isRunning) {
                try {
                    receiveData = new byte[4096];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);

                    System.out.println("Received packet: " + receivePacket.getAddress().getHostAddress() + ':' + port);

                    // process the metadata received
                    ObjectInputStream ois = new ObjectInputStream(
                            new ByteArrayInputStream(receivePacket.getData(), 0, receivePacket.getLength()));
                    FileInfo fileInfo = (FileInfo) ois.readObject();

                    String filepath = "D:\\Received_Files";
                    new File(filepath).mkdirs();

                    // process file content received
                    filepath += "\\" + fileInfo.name;
                    FileWriter fw = new FileWriter(filepath);

                    receiveData = new byte[(int) fileInfo.size + 1];
                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);

                    String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    labelNotification.setText("Received file: " + filepath);
                    fw.write(message);

                    fw.flush();
                } catch (Exception e) {
                    if (e instanceof SocketException)
                        labelNotification.setText("Server closed (" + port + ")");
                    isRunning = false;
                }
            }
            serverSocket.close();
        }

        public void stopServer() {
            isRunning = false;
            serverSocket.close();
        }
    }

    public static void main(String[] args) {
        FileTransfer fileTransfer = new FileTransfer(1);
        fileTransfer.setSize(windowX, windowY);
        fileTransfer.setTitle("File Transfer (UDP)");
        fileTransfer.setResizable(false);
        fileTransfer.setLocationRelativeTo(null);
        fileTransfer.setVisible(true);
    }
}

class FileInfo implements Serializable {
    String name;
    long size;

    FileInfo(String name, long size) {
        this.name = name;
        this.size = size;
    }
}