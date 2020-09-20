package ru.gb.jthree.socketProgramming;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class FileReceiver {

    public static void main(String[] args) throws IOException {
        FileReceiver server = new FileReceiver();
        SocketChannel socketChannel = server.createServerSocketChannel();
//        server.readFileFromSocketChannel(socketChannel);
        server.writeFileToSocketChannel(socketChannel);

    }

    private void readFileFromSocketChannel(SocketChannel socketChannel) throws IOException {
        //Try to create a new file
        Path path = Paths.get("C:\\Users\\mi\\Desktop\\Server\\construction-copy.mp4");
        FileChannel fileChannel = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)
        );
        //Allocate a ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (socketChannel.read(buffer) > 0) {
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();
        }
        fileChannel.close();
        System.out.println("Receving file successfully!");
        socketChannel.close();
    }

    private void writeFileToSocketChannel(SocketChannel socketChannel) throws IOException {

        //Read a file from disk. Its filesize is 54.3 MB (57,006,053 bytes)
        // receive the same size                 54.3 MB (57,006,053 bytes)
        Path path = Paths.get("C:\\Users\\mi\\Desktop\\Server\\construction-copy.mp4");
        FileChannel inChannel = FileChannel.open(path);

        ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (inChannel.read(buffer) > 0) {
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
        socketChannel.close();
    }

    private SocketChannel createServerSocketChannel() throws IOException {

        ServerSocketChannel serverSocket = ServerSocketChannel.open().bind(new InetSocketAddress(9000));
        SocketChannel s = null;
        s = serverSocket.accept();
        System.out.println("connection established .." + s.getRemoteAddress());
        return s;
    }

}