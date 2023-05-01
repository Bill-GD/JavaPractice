package endterm;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;

public class FileTransfer extends Frame {
    // globally used variables
    // both
    static int windowX = 350, windowY = 400;
    final static int MAX_PACKET_SIZE = 1024 * 48;
    static boolean isClientMenuOpened = true;
    Component[] clientComponents, serverComponents;

    // client
    static boolean isSending = false, cancelSend = false;
    Clock clock = null;

    MenuItem cancelSendItem;
    Label labelHostname, labelPort, labelFile;
    TextField hostInput, portInputClient;
    TextArea fileInput;
    Button browseFile, buttonSend, buttonCancel;
    Label labelNotification, labelPacket;

    // server
    Server server = null;
    static boolean isServerOpened = false, isCancelled = false;

    MenuItem closeServerItem;
    Label labelPortServer;
    TextField portInputServer;
    Button toggleServer;
    Label labelNotificationServer, labelPacketServer;

    public FileTransfer() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // stop the server & clock when the window is closed
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
        // setup the UI
        setLayout(null);

        MenuBar menuBar = new MenuBar();

        Menu clientMenu = new Menu("Client", false);
        MenuItem clientMenuItem = new MenuItem("Client Menu");
        clientMenuItem.addActionListener(e -> {
            isClientMenuOpened = true;
            setMenuState(isClientMenuOpened);
        });
        clientMenu.add(clientMenuItem);
        cancelSendItem = new MenuItem("Cancel sending");
        cancelSendItem.addActionListener(e -> {
            setSendingState(false);
        });
        clientMenu.add(cancelSendItem);

        Menu serverMenu = new Menu("Server", false);
        MenuItem serverMenuItem = new MenuItem("Server Menu");
        serverMenuItem.addActionListener(e -> {
            isClientMenuOpened = false;
            setMenuState(isClientMenuOpened);
        });
        serverMenu.add(serverMenuItem);
        closeServerItem = new MenuItem("Close Server");
        closeServerItem.addActionListener(e -> {
            try {
                if (isServerOpened)
                    closeServer();
            } catch (Exception ex) {
                if (ex instanceof NumberFormatException)
                    labelNotificationServer.setText("Invalid port");
                else if (ex instanceof SocketException)
                    labelNotificationServer.setText("Can't open server on specified port");
                else if (ex instanceof IllegalThreadStateException)
                    labelNotificationServer.setText("Server already started");
                else
                    labelNotificationServer.setText("Receiving file. Can't close server");
            }
        });
        closeServerItem.setEnabled(isServerOpened);
        serverMenu.add(closeServerItem);

        menuBar.add(clientMenu);
        menuBar.add(serverMenu);
        setMenuBar(menuBar);

        // client side
        labelHostname = new Label("Hostname");
        labelHostname.setBounds(65, 65, 65, 20);
        labelHostname.setIgnoreRepaint(true);
        add(labelHostname);
        hostInput = new TextField("localhost");
        hostInput.setBounds(65, 85, 220, 20);
        hostInput.setIgnoreRepaint(true);
        add(hostInput);

        labelPort = new Label("Port");
        labelPort.setBounds(65, 110, 35, 20);
        labelPort.setIgnoreRepaint(true);
        add(labelPort);
        portInputClient = new TextField("1000");
        portInputClient.setBounds(65, 130, 220, 20);
        portInputClient.setIgnoreRepaint(true);
        add(portInputClient);

        labelFile = new Label("Filepath");
        labelFile.setBounds(65, 155, 50, 20);
        labelFile.setIgnoreRepaint(true);
        add(labelFile);
        fileInput = new TextArea("", 2, 200, TextArea.SCROLLBARS_VERTICAL_ONLY);
        fileInput.setBounds(65, 175, 220, 80);
        fileInput.setIgnoreRepaint(true);
        add(fileInput);

        browseFile = new Button("Browse");
        browseFile.addActionListener((e) -> {
            FileDialog fileDialog = new FileDialog(this, "Choose file", FileDialog.LOAD);
            fileDialog.setVisible(true);
            String dir = fileDialog.getDirectory(), name = fileDialog.getFile();
            if (dir != null && name != null)
                fileInput.setText(dir + name);
        });
        browseFile.setBounds(65, 265, 50, 30);
        browseFile.setIgnoreRepaint(true);
        add(browseFile);

        buttonSend = new Button("Send");
        buttonSend.addActionListener((e) -> {
            labelNotification.setText("");
            try {
                int port = Integer.parseInt(portInputClient.getText());
                if (port < 0 || port > 65535)
                    throw new NumberFormatException();
                new Client(port, hostInput.getText()).start();
            } catch (Exception ex) {
                if (ex instanceof NumberFormatException)
                    labelNotification.setText("Invalid port");
                if (ex instanceof UnknownHostException)
                    labelNotification.setText("Invalid host");
                if (ex instanceof SocketException)
                    labelNotification.setText("Can't connect to port");
                if (ex instanceof IllegalThreadStateException)
                    labelNotification.setText("Already sending file");
            }
        });
        buttonSend.setBounds(150, 265, 50, 30);
        buttonSend.setIgnoreRepaint(true);
        add(buttonSend);

        buttonCancel = new Button("Cancel");
        buttonCancel.addActionListener(e -> {
            setSendingState(false);
        });
        buttonCancel.setBounds(235, 265, 50, 30);
        buttonCancel.setIgnoreRepaint(true);
        add(buttonCancel);

        labelNotification = new Label("", Label.CENTER);
        labelNotification.setBounds(0, windowY - 80, windowX, 30);
        add(labelNotification);

        labelPacket = new Label("", Label.CENTER);
        labelPacket.setBounds(0, windowY - 60, windowX, 30);
        add(labelPacket);

        // server side
        labelPortServer = new Label("Port");
        labelPortServer.setBounds(125, 110, 35, 20);
        labelPortServer.setIgnoreRepaint(true);
        add(labelPortServer);
        portInputServer = new TextField("1000");
        portInputServer.setBounds(125, 130, 100, 20);
        portInputServer.setIgnoreRepaint(true);
        add(portInputServer);

        toggleServer = new Button("Open Server");
        toggleServer.addActionListener((e) -> {
            try {
                if (isServerOpened)
                    closeServer();
                else
                    openServer();
            } catch (Exception ex) {
                if (ex instanceof NumberFormatException)
                    labelNotificationServer.setText("Invalid port");
                else if (ex instanceof SocketException)
                    labelNotificationServer.setText("Can't open server on specified port");
                else if (ex instanceof IllegalThreadStateException)
                    labelNotificationServer.setText("Server already started");
                else
                    labelNotificationServer.setText("Receiving file. Can't close server");
            }
        });
        toggleServer.setBounds(125, 160, 100, 30);
        toggleServer.setIgnoreRepaint(true);
        add(toggleServer);

        labelNotificationServer = new Label("", Label.CENTER);
        labelNotificationServer.setBounds(0, windowY - 80, windowX, 30);
        add(labelNotificationServer);

        labelPacketServer = new Label("", Label.CENTER);
        labelPacketServer.setBounds(0, windowY - 60, windowX, 30);
        add(labelPacketServer);

        clientComponents = new Component[] {
                labelHostname, labelPort, labelFile, hostInput, portInputClient, fileInput, browseFile, buttonSend,
                buttonCancel, labelNotification, labelPacket
        };
        serverComponents = new Component[] {
                labelPortServer, toggleServer, portInputServer, labelNotificationServer, labelPacketServer
        };

        setMenuState(isClientMenuOpened);
        setSendingState(isSending);
    }

    public void setMenuState(boolean clientMenuState) {
        for (var item : clientComponents)
            item.setVisible(clientMenuState);
        for (var item : serverComponents)
            item.setVisible(!clientMenuState);
    }

    public void openServer() throws Exception {
        int port = Integer.parseInt(portInputServer.getText());
        if (port < 0 || port > 65535)
            throw new NumberFormatException();
        server = new Server(port);
        server.start();
        isServerOpened = true;
        toggleServer.setLabel("Close Server");
        closeServerItem.setEnabled(isServerOpened);
    }

    public void closeServer() throws Exception {
        if (server.isReceiving)
            throw new Exception();
        server.stopServer();
        isServerOpened = false;
        toggleServer.setLabel("Open Server");
        labelPacketServer.setText("");
        closeServerItem.setEnabled(isServerOpened);
    }

    public static String formatFileSize(long bytes) {
        if (bytes <= 0)
            return "0B";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };

        int digitGroups = (int) (Math.log(bytes) / Math.log(1024));

        return new DecimalFormat("#,##0.#").format(bytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void setSendingState(boolean value) {
        isSending = value;
        cancelSend = !value;
        buttonCancel.setEnabled(value);
        cancelSendItem.setEnabled(value);
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

    // runs in a different thread to avoid blocking AWT
    class Client extends Thread {
        int destinationPort = 0;
        DatagramSocket socket;
        InetAddress destinationAddress;

        public Client(int destinationPort, String destinationIP) throws Exception {
            this.destinationPort = destinationPort;
            socket = new DatagramSocket();
            destinationAddress = InetAddress.getByName(destinationIP);
        }

        public void run() {
            try {
                File fileToSend = new File(fileInput.getText().replaceAll("\"", ""));

                if (!fileToSend.exists()) {
                    labelNotification.setText("File doesn't exist");
                    socket.close();
                    return;
                }

                setSendingState(true);
                clock = new Clock();
                clock.start();

                // process/send file metadata
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
                socket.send(new DatagramPacket(bytesToSend, bytesToSend.length, destinationAddress, destinationPort));

                // partition & send file content
                byte[] filePart = new byte[MAX_PACKET_SIZE];

                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToSend));

                int i = 0;
                while (bis.read(filePart, 0, MAX_PACKET_SIZE) != -1) {
                    socket.send(new DatagramPacket(
                            filePart,
                            MAX_PACKET_SIZE,
                            destinationAddress,
                            destinationPort));
                    labelPacket.setText((i + 1) + " / " + numberOfPieces + " - " +
                            formatFileSize(i * MAX_PACKET_SIZE) + " / " + formatFileSize(fileLength) +
                            String.format(" (%.1f", ((double) (i + 1) * 100 / numberOfPieces)) + "%)");
                    Thread.sleep(100);
                    filePart = new byte[MAX_PACKET_SIZE];
                    i++;

                    if (cancelSend) {
                        labelNotification.setText("Cancelled");
                        labelPacket.setText("");
                        setSendingState(false);

                        byte[] cancelBytes = "cancelled".getBytes();
                        socket.send(new DatagramPacket(
                                cancelBytes,
                                cancelBytes.length,
                                destinationAddress,
                                destinationPort));

                        break;
                    }
                }
                if (!cancelSend)
                    labelNotification.setText("Sent to "
                            + destinationAddress.getHostAddress()
                            + ':' + destinationPort
                            + " (" + clock.getTime() + ')');

                bis.close();
                socket.close();
                clock.stopClock();
                fileToSend = null;
                clock = null;
                labelPacket.setText("");
                cancelSend = false;
                setSendingState(false);
            } catch (Exception ex) {
                labelNotification.setText("Problem occurred when sending file");
            }
        }
    }

    // runs in a different thread to avoid blocking AWT
    class Server extends Thread {
        int port;
        DatagramSocket serverSocket;
        byte[] receiveData;
        boolean isRunning = false, isReceiving = false;
        InetAddress address = null;

        public Server(int port) throws Exception {
            this.port = port;
            serverSocket = new DatagramSocket(port);
            labelNotificationServer.setText("Server opened (" +
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

                    isReceiving = true;
                    labelNotificationServer.setText("Receiving file...");
                    labelPacketServer.setText("");

                    // process the metadata received
                    ObjectInputStream ois = new ObjectInputStream(
                            new ByteArrayInputStream(receivePacket.getData(), 0, receivePacket.getLength()));
                    FileInfo fileInfo = (FileInfo) ois.readObject();

                    String filepath = "D:\\Received_Files";
                    new File(filepath).mkdirs();
                    String fileDir = filepath;
                    filepath += "\\" + fileInfo.name;
                    File endFile = new File(filepath);

                    // process file content received
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filepath));
                    receiveData = new byte[MAX_PACKET_SIZE];

                    int i = 0;
                    for (i = 0; i < fileInfo.numberOfPackets; i++) {
                        receivePacket = new DatagramPacket(
                                receiveData,
                                receiveData.length);
                        serverSocket.receive(receivePacket);

                        if ((new String(receivePacket.getData(), 0, "cancelled".length())).equals("cancelled")) {
                            isCancelled = true;
                            break;
                        }

                        labelPacketServer.setText((i + 1) + " / " + fileInfo.numberOfPackets + " - " +
                                formatFileSize((i + 1) * MAX_PACKET_SIZE) + " / " + formatFileSize(fileInfo.size));

                        bos.write(receiveData, 0, (i == fileInfo.numberOfPackets - 1)
                                ? fileInfo.lastByteLength
                                : MAX_PACKET_SIZE);
                    }
                    bos.flush();
                    bos.close();

                    if (isCancelled) {
                        endFile.delete();
                        labelNotificationServer.setText("Client cancelled file transfer");
                        labelPacketServer.setText("");
                    }
                    if (!isCancelled) {
                        labelNotificationServer.setText("Saved at: " + fileDir);
                        labelPacketServer.setText("Received: "
                                + i + " / " + fileInfo.numberOfPackets +
                                " (" + formatFileSize(fileInfo.size) + ")");
                    }
                    isReceiving = false;
                    isCancelled = false;
                } catch (Exception e) {
                    if (e instanceof SocketException)
                        labelNotificationServer.setText("Server closed (" + port + ")");
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
        FileTransfer fileTransfer = new FileTransfer();
        fileTransfer.setSize(windowX, windowY);
        fileTransfer.setTitle("File Transfer (UDP)");
        fileTransfer.setResizable(false);
        fileTransfer.setLocationRelativeTo(null);
        fileTransfer.setVisible(true);
    }
}

// stores file metadata
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
