import java.net.*;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
public class Client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    //Delcare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new FontUIResource("Times New Roman",Font.PLAIN,20);

//192.168.43.148
    public Client(){
        try {
            System.out.println("Sending request to server...");
            socket= new Socket("192.168.43.148",7777);
            System.out.println("Connecting done.");
            br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            // startwriting();
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("key released"+ e.getKeyCode());
                if(e.getKeyCode()==10){
                    // System.out.println("you have pressed enter button");
                    String contentToSend =messageInput.getText();
                    messageArea.append("Me : "+ contentToSend+ "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
           
            
            
        });
    }
    private void createGUI(){
        this.setTitle(" Client messager");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("chatlogo.png"));

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        this.setLayout(new BorderLayout());
        //adding the component to layout
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);

        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);

    } 
    public void startReading(){
        Runnable r1=()->{
            System.out.println("Reader started..");
            try {
            while(true){
                
                    String msg =br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Client terminated the chat");
                        JOptionPane.showMessageDialog(this, "server Terminated the chat" );
                        messageInput.setEnabled(false);

                        socket.close();
                        break;
                    }
                    // System.out.println("Server : " + msg);\
                    messageArea.append("Server : "+ msg+"\n ");
                } 
            }catch (Exception e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                System.out.println("Connection is Closed");
            }

        };
        new Thread(r1).start();
    }
    public void startwriting(){
        Runnable r2 = ()-> {
            System.out.println("Writer started...");
            try {
            while(!socket.isClosed()){
                
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        // System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }
                }
                 
            }catch (Exception e) {
                // TODO: handle exception
                // e.printStackTrace();
                // System.out.println("Connection  is Closed");
            }
            System.out.println("Connection  is Closed");
        };
        new Thread(r2).start(); 
    }
    public static void main(String[] args) {
        System.out.println("This is Client side");
        new Client();
    }
}
