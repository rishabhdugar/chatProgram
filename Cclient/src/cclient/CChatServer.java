/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cclient;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author igloo
 */
public class CChatServer implements Runnable,ActionListener{
    private JFrame jfm;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private JTextArea jta;
    private JScrollPane jscrlp;
    private JTextField jtfInput;
    private JButton jbtnSend;
    public CChatServer(){
        
        jfm=new JFrame("Chat Server");
        jfm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfm.setLayout(new FlowLayout());
        jfm.setSize(300, 320);
        
        Thread myThread=new Thread(this);
        myThread.start();
        
        jta=new JTextArea(15, 15);
        jta.setEditable(false);
        jta.setLineWrap(true);
        
        jscrlp=new JScrollPane(jta);
        jtfInput=new JTextField(15);
        jtfInput.addActionListener(this);
        
        jbtnSend=new JButton("Send");
        jbtnSend.addActionListener(this);
        
        jfm.getContentPane().add(jscrlp);
        jfm.getContentPane().add(jtfInput);
        jfm.getContentPane().add(jbtnSend);
        jfm.setVisible(true);
        
    }

    @Override
     public void run(){
         try{
             serverSocket=new ServerSocket(4444);
             clientSocket=serverSocket.accept();
             oos=new ObjectOutputStream(clientSocket.getOutputStream());
             ois=new ObjectInputStream(clientSocket.getInputStream());
             while(true){
              //client receive message from this
                 Object input=ois.readObject();
                 jta.setText(jta.getText()+"Client says:"+(String)input+"\n");
                 
             }
         }
         catch(IOException e){
             e.printStackTrace();
         
         }
         catch(ClassNotFoundException e){
             e.printStackTrace();
         }
         
         
    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getActionCommand().equals("Send") || ae.getSource() instanceof JTextField){
        try{
            oos.writeObject(jtfInput.getText());
            jta.setText(jta.getText()+"You say:"+jtfInput.getText()+"\n");
        }
        catch(IOException e){
            e.printStackTrace();
        
        }
        
        
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new CChatServer();
             //   System.out.println("hi");
            }
        });
        
    }
    
}
