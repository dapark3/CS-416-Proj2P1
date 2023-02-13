import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientUDP {
    private final DatagramSocket socket;

    //constructor
    public ClientUDP() throws SocketException{
        socket = new DatagramSocket();
    }

    //main() handles command-line arguments
    public static void main(String[] args){
        if (args.length != 2){
            System.out.println("Syntax: ClientTCP <ServerIP> <ServerPort>");
            return;
        }
        try {
            InetAddress serverIP = InetAddress.getByName(args[0]);
            int serverPort = Integer.parseInt(args[1]);
            ClientUDP client = new ClientUDP();
            client.service(serverIP, serverPort);
        }catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }//end of main()

    public void service(InetAddress serverIP, int serverPort)
            throws IOException, InterruptedException {
        String message="";
        //create a datagram packet containing the message
        byte[] buffer = message.getBytes();
        DatagramPacket request = new DatagramPacket(
                buffer,         // raw data be encapsulate
                buffer.length, //size of the raw data
                serverIP,      //destination IP address
                serverPort     //destination port number
        );
        //send the datagram packet to server
        socket.send(request);
        //receive the reply packet from server
        DatagramPacket reply = new DatagramPacket(
                new byte[32], 32);
        socket.receive(reply);
        byte[] replyMessage = reply.getData();
        //Convert byte array to integer
        ByteBuffer buffer1 = ByteBuffer.wrap(replyMessage);
        int replyMessageInt = buffer1.getInt();
        long secsSinceEpoch = 2147483647+replyMessageInt;
        secsSinceEpoch += 2147483647;
        System.out.println("Seconds since 1900: " + (secsSinceEpoch) + " Seconds");
        //convert integer to a calendar format
        Calendar calendar = Calendar.getInstance();
        calendar.set(1900, Calendar.JANUARY,1);
        calendar.add(Calendar.SECOND, 2147483647);
        calendar.add(Calendar.SECOND, 2147483647);
        calendar.add(Calendar.SECOND, replyMessageInt);
        //convert calendar to date format for date string
        DateFormat sdf = SimpleDateFormat.getInstance();
        sdf.setCalendar(calendar);
        System.out.println("Current Date: "+sdf.getCalendar().getTime());
    }
}