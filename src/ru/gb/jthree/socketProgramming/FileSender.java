package ru.gb.jthree.socketProgramming;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class FileSender {

    public static void main(String[] args) throws IOException {
        FileSender client = new FileSender();
        SocketChannel socketChannel = client.CreateChannel();
//        client.sendFile(socketChannel);
        client.readFile(socketChannel);

    }

    private void sendFile(SocketChannel socketChannel) throws IOException {

        //Read a file from disk. Its filesize is 54.3 MB (57,006,053 bytes)
        // receive the same size                 54.3 MB (57,006,053 bytes)
        Path path = Paths.get("C:\\Users\\mi\\Desktop\\Client\\Construction3D.mp4");
        FileChannel inChannel = FileChannel.open(path);

        ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (inChannel.read(buffer) > 0) {
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
        socketChannel.close();
    }

    private void readFile(SocketChannel socketChannel) throws IOException {
        //Try to create a new file
        Path path = Paths.get("C:\\Users\\mi\\Desktop\\Client\\Construction3D_copy.mp4");
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

    private SocketChannel CreateChannel() throws IOException {
        //Remember that is code only works on blocking mode
        SocketChannel socketChannel = SocketChannel.open();

        //we don't need call this function as default value of blocking mode = true
        socketChannel.configureBlocking(true);

        SocketAddress sockAddr = new InetSocketAddress("DESKTOP-04QQCJ1", 9000);
        socketChannel.connect(sockAddr);
        return socketChannel;
    }

}
