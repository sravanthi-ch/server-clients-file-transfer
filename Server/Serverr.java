import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 
  

// ClientHandler class 
class ClientHandler extends Thread  
{ 
    
    final DataInputStream dis; 
    final DataOutputStream dos; 
    final Socket s; 
      
  
    // Constructor 
    public ClientHandler(Socket s,DataInputStream dis, DataOutputStream dos)  
    { 
        this.s = s; 
        this.dis = dis; 
        this.dos = dos;
	//this.dis = new DataInputStream(s.getInputStream()); 
        //this.dos = new DataOutputStream(s.getOutputStream());  
    } 
  
    @Override
    public void run()
    { System.out.println("in run()");
        String received; 
        int count;
	byte[] bytes = new byte[1024];
        while (true)  
        { 
            try { //System.out.println("in try");
  
                // Ask user what he wants 
                dos.writeUTF("receive or send file? Or Exit?"); 
                // try{ this.sleep(100);}
		 //catch(InterruptedException e)
		 //{ }
                // receive the answer from client 
                received = dis.readUTF(); //System.out.println("received");
                  
                if(received.equals("Exit")) 
                {  
                    System.out.println("Client " + this.s + " sends exit..."); 
                    System.out.println("Closing this connection."); 
                    this.s.close(); 
                    System.out.println("Connection closed"); 
                    break; 
                } 
                
		String filename; long sz;
                switch (received) { 
                  
                    case "send" : 
			dos.writeUTF("filename?");
                        filename=dis.readUTF();
			System.out.println("Receving file: "+filename);
	
			System.out.println("Saving as file: "+filename);
			System.out.println("Receving file..");
			FileOutputStream out = new FileOutputStream(filename);
    		        do{
				count=dis.read(bytes);
				//System.out.println("stuck in while");
        		        out.write(bytes, 0, count);
       			   }while(!(count<1024));
			System.out.println("Comleted");
			out.close();
                        break; 
                          
                    case "receive" : 
			System.out.println("in receive case");
                        dos.writeUTF("file name?");
			filename = dis.readUTF();
			try{
				System.out.println("Sending File: "+filename);
				File f=new File(filename);
				FileInputStream fin=new FileInputStream(f);
				sz=(int) f.length();
				dos.writeUTF(Long.toString(sz)); 
				dos.flush(); 
				System.out.println ("Size: "+sz);
				//System.out.println ("Buf size: "+ss.getReceiveBufferSize());
        			while ((count = fin.read(bytes)) != -1) 
				   {
      				     dos.write(bytes, 0, count);
     				   }
				dos.writeUTF("-1");
				fin.close();
				System.out.println("..ok"); 
				dos.flush(); 
 				System.out.println("Send Complete");
				dos.flush();  
			  }
			  catch(Exception e)
			  {
				e.printStackTrace();
				System.out.println("An error occured");
			  }
                        break; 
                          
                    default: 
                        dos.writeUTF("Invalid input"); 
                        break; 
                } 
            } 
	    catch (IOException e) 
	    { 
                e.printStackTrace(); 
            } 
        }         
    } 
} 
// Server class 
public class Serverr  
{ 
    public static void main(String[] args) throws IOException  
    { 
          ServerSocket ss = new ServerSocket(5000,1); 
	  //ss.bind(new InetSocketAddress("127.0.0.1", 0));---not req as already bound!
          System.out.println("socket created"); 
        
        while (true)  
        { 
            Socket s=null;
              
            try 
            { 
                s = ss.accept(); 
                  
                System.out.println("A new client is connected : " + s); 
                  
                // obtaining input and out streams 
                DataInputStream dis = new DataInputStream(s.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
                  
                System.out.println("Assigning new thread for this client"); 
  
                // create a new thread object 
                
  		System.out.println("invoking thread");
		new ClientHandler(s, dis, dos).start();
                // Invoking the start() method 
                //t.start(); 
                  
            } 
            catch (Exception e){ 
                s.close(); 
                e.printStackTrace(); 
            } 
        } 
    } 
} 