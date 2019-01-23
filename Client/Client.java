//package Client;

import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 
  

class Client{  

public static void main(String args[])throws Exception
{ 
	String address = "";
	Scanner sc=new Scanner(System.in);
	System.out.println("Enter Server Address: ");
	address=sc.nextLine();
	//create the socket on port 5000
	Socket s=new Socket(address,5000);  
	DataInputStream din=new DataInputStream(s.getInputStream());  
	DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
	BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
	while(true)
	{ 	System.out.println("in loop");
		System.out.println(din.readUTF());
		String str="",filename="";  
		try{
			str=br.readLine();  
			dout.writeUTF(str); 
			if(str.equals("receive"))
			{
				System.out.println("sent receive");
				dout.flush();
				System.out.println(din.readUTF());
				str=br.readLine();
				dout.writeUTF(str); 
				dout.flush();  
				System.out.println("Receving file: "+str);
				filename=str;
				System.out.println("Saving as file: "+filename);
				long sz=Long.parseLong(din.readUTF());
				System.out.println ("File Size: "+(sz)+"KB");
				byte b[]=new byte[1024];
				System.out.println("Receving file..");
				FileOutputStream fos=new FileOutputStream(filename);
				int bytesRead;
				do
				{
					bytesRead = din.read(b);
					fos.write(b,0,bytesRead);
				}while(!(bytesRead<1024));
				System.out.println("Comleted");
				fos.close(); 
			}
			else if(str.equals("send"))
			{	System.out.println(din.readUTF());
				str=br.readLine(); filename=str;
				dout.writeUTF(str); dout.flush();
 				try{
					System.out.println("Sending File: "+filename);
					//System.out.println(System.getProperty("user.dir"));
					//filename="C:\Users\Sravanthi\java\cn\Client\"+filename;
					File file = new File(filename);
				        long length = file.length();
					//dout.writeUTF(length.toString());
				        byte[] bytes = new byte[1024];
        				FileInputStream in = new FileInputStream(file);		
 				        int count;
        				while ((count = in.read(bytes)) != -1) 
					{
        					dout.write(bytes, 0, count);
        				}
					dout.writeUTF("-1");
					System.out.println("..ok"); 
					dout.flush(); 
					in.close();
					System.out.println("Send Complete");
					dout.flush();  
				}
				catch(Exception e)
				{
					e.printStackTrace();
					System.out.println("An error occured");
				}
			}
			else if(br.readLine().equals("Exit"))
			{	 System.out.println("Exiting"); 
				 dout.writeUTF("Exit");
				 break; 
			}
  
		}
		catch(EOFException e)
		{
			//do nothing
		}
	}

    }
}
