package endterm;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class FileTransfer extends Frame {
    Server server = null;
    static int windowX = 550, windowY = 350;
    boolean isServerOpened = false;
    Clock clock = null;

    final static int MAX_PACKET_SIZE = 1024 * 48;

    Label labelNotification, labelPacket;
    Button toggleServer;
    TextField hostInput, portInputClient, portInputServer;
    TextArea fileInput;

    public FileTransfer(int mode) {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (server != null) {
                    server.stopServer();
                    server = null;
                }
                if (clock != null) {
                    clock.stopClock();
                    clock = null;
                }
                System.exit(0);
            }
        });
        setLayout(null);

        // client side
        Label labelHostname = new Label("Hostname:");
        labelHostname.setBounds(40, 50, 65, 20);
        labelHostname.setIgnoreRepaint(true);
        add(labelHostname);
        hostInput = new TextField("localhost");
        hostInput.setBounds(110, 50, 150, 20);
        hostInput.setIgnoreRepaint(true);
        add(hostInput);

        Label labelPort = new Label("Port:");
        labelPort.setBounds(40, 80, 40, 20);
        labelPort.setIgnoreRepaint(true);
        add(labelPort);
        portInputClient = new TextField("1000");
        portInputClient.setBounds(110, 80, 70, 20);
        portInputClient.setIgnoreRepaint(true);
        add(portInputClient);

        Label labelFile = new Label("Filepath:");
        labelFile.setBounds(40, 110, 60, 20);
        labelFile.setIgnoreRepaint(true);
        add(labelFile);
        fileInput = new TextArea("D:\\text.txt", 2, 200, TextArea.SCROLLBARS_VERTICAL_ONLY);
        fileInput.setBounds(40, 140, 220, 60);
        fileInput.setIgnoreRepaint(true);
        add(fileInput);

        Button browseFile = new Button("Browse");
        browseFile.addActionListener((e) -> {
            FileDialog fileDialog = new FileDialog(this, "Choose file", FileDialog.LOAD);
            fileDialog.setVisible(true);
            String dir = fileDialog.getDirectory(), name = fileDialog.getFile();
            if (dir != null && name != null)
                fileInput.setText(dir + name);
        });
        browseFile.setBounds(270, 140, 50, 30);
        browseFile.setIgnoreRepaint(true);
        add(browseFile);

        // send using the information given
        Button buttonSend = new Button("Send");
        buttonSend.addActionListener((e) -> {
            labelNotification.setText("");
            int port = Integer.parseInt(portInputClient.getText());
            if (port < 0 || port > 65535)
                throw new NumberFormatException();
            try {
                new Client(port).start();
            } catch (Exception ex) {
                if (ex instanceof NumberFormatException)
                    labelNotification.setText("Invalid port");
                if (ex instanceof SocketException)
                    labelNotification.setText("Can't connect to port " + port);
                if (ex instanceof IllegalThreadStateException)
                    labelNotification.setText("Already sending file");
            }
        });
        buttonSend.setBounds(40, 210, 50, 30);
        buttonSend.setIgnoreRepaint(true);
        add(buttonSend);

        // server side
        Label labelPortServer = new Label("Port:");
        labelPortServer.setBounds(360, 50, 30, 20);
        labelPortServer.setIgnoreRepaint(true);
        add(labelPortServer);
        portInputServer = new TextField("1000");
        portInputServer.setBounds(400, 50, 90, 20);
        portInputServer.setIgnoreRepaint(true);
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
                    if (port < 0 || port > 65535)
                        throw new NumberFormatException();
                    server = new Server(port);
                    server.start();
                    isServerOpened = true;
                    toggleServer.setLabel("Close Server");
                }
            } catch (Exception ex) {
                if (ex instanceof NumberFormatException)
                    labelNotification.setText("Invalid port");
                if (ex instanceof SocketException)
                    labelNotification.setText("Can't close server");
                if (ex instanceof IllegalThreadStateException)
                    labelNotification.setText("Server already started");
            }
        });
        toggleServer.setBounds(400, 80, 90, 30);
        toggleServer.setIgnoreRepaint(true);
        add(toggleServer);

        labelNotification = new Label("", Label.CENTER);
        labelNotification.setBounds(0, windowY - 80, windowX, 30);
        add(labelNotification);

        labelPacket = new Label("", Label.CENTER);
        labelPacket.setBounds(0, windowY - 60, windowX, 30);
        add(labelPacket);
    }

    class Clock extends Thread {
        int second, hour, minute;
        boolean isRunning = false;
        String time = "";

        Clock() {
            second = minute = hour = 0;
        }

        public void run() {
            isRunning = true;
            while (isRunning) {
                try {
                    second++;
                    if (second == 60) {
                        second = 0;
                        minute++;
                    }
                    if (minute == 60) {
                        minute = 0;
                        hour++;
                    }
                    if (hour == 24)
                        hour = 0;
                    time = String.format("%02ds", second);
                    if (minute > 0)
                        time = String.format("%02dm", minute) + time;
                    if (hour > 0)
                        time = String.format("%02dh", hour) + time;
                    labelNotification
                            .setText("Sending file... (" + time + ")");
                    repaint();
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }

        public String getTime() {
            return time;
        }

        public void stopClock() {
            isRunning = false;
        }
    }

    class Client extends Thread {
        int port = 0, bindPort = 0;
        DatagramSocket socket;

        public Client(int port) throws SocketException {
            this.port = port;
            this.bindPort = port > 1 ? port - 1 : port + 1;
            socket = new DatagramSocket(bindPort);
        }

        public void run() {
            try {
                InetAddress destinationAddress = InetAddress.getByName(hostInput.getText());

                File fileToSend = new File(fileInput.getText().replaceAll("\"", ""));

                if (!fileToSend.exists()) {
                    labelNotification.setText("File doesn't exist");
                    socket.close();
                    return;
                }

                clock = new Clock();
                clock.start();

                // process/send file metadata to send
                long fileLength = fileToSend.length();
                int numberOfPieces = (int) (fileLength / MAX_PACKET_SIZE);
                int lastByteLength = (int) (fileLength % MAX_PACKET_SIZE);
                if (lastByteLength > 0) {
                    numberOfPieces++;
                }
                FileInfo fileInfo = new FileInfo(
                        fileToSend.getName(),
                        fileLength,
                        numberOfPieces,
                        lastByteLength);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(fileInfo);

                byte[] bytesToSend = baos.toByteArray();
                socket.send(new DatagramPacket(bytesToSend, bytesToSend.length, destinationAddress, port));

                // process/send & partition file content
                byte[] filePart = new byte[MAX_PACKET_SIZE];

                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToSend));

                int i = 0;
                while (bis.read(filePart, 0, MAX_PACKET_SIZE) != -1) {
                    socket.send(new DatagramPacket(
                            filePart,
                            MAX_PACKET_SIZE,
                            destinationAddress,
                            port));
                    labelPacket.setText((i + 1) + " / " + numberOfPieces +
                            String.format(" (%.1f", ((double) (i + 1) * 100 / numberOfPieces)) + "%)");
                    Thread.sleep(100);
                    filePart = new byte[MAX_PACKET_SIZE];
                    i++;
                }

                labelNotification.setText(
                        "Sent to " + destinationAddress.getHostAddress() + ':' + port + " (" + clock.getTime() + ')');

                fileToSend = null;
                bis.close();
                socket.close();
                clock.stopClock();
                clock = null;
                labelPacket.setText("");
            } catch (Exception ex) {
                labelNotification.setText("Problem occurred when sending file");
            }
        }
    }

    class Server extends Thread {
        int port;
        DatagramSocket serverSocket;
        byte[] receiveData;
        boolean isRunning = true;
        InetAddress address = null;

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
                    address = receivePacket.getAddress();
                    serverSocket.receive(receivePacket);

                    labelNotification.setText("Receiving file...");

                    // process the metadata received
                    ObjectInputStream ois = new ObjectInputStream(
                            new ByteArrayInputStream(receivePacket.getData(), 0, receivePacket.getLength()));
                    FileInfo fileInfo = (FileInfo) ois.readObject();

                    String filepath = "D:\\Received_Files";
                    new File(filepath).mkdirs();
                    filepath += "\\" + fileInfo.name;

                    // process file content received
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filepath));
                    receiveData = new byte[MAX_PACKET_SIZE];

                    int i = 0;
                    for (i = 0; i < (fileInfo.numberOfPackets - 1); i++) {
                        receivePacket = new DatagramPacket(
                                receiveData,
                                receiveData.length,
                                address, port);
                        serverSocket.receive(receivePacket);
                        bos.write(receiveData, 0, MAX_PACKET_SIZE);
                    }
                    receivePacket = new DatagramPacket(
                            receiveData,
                            receiveData.length,
                            address, port);
                    serverSocket.receive(receivePacket);
                    bos.write(receiveData, 0, fileInfo.lastByteLength);
                    bos.flush();

                    labelNotification.setText("Received file: " + filepath);
                    labelPacket.setText("Received: "
                            + (i + 1) + " / " + fileInfo.numberOfPackets);
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
    public String name;
    public long size;
    int numberOfPackets;
    int lastByteLength;

    public FileInfo(String name, long size, int numberOfPackets, int lastByteLength) {
        this.name = name;
        this.size = size;
        this.numberOfPackets = numberOfPackets;
        this.lastByteLength = lastByteLength;
    }
}
