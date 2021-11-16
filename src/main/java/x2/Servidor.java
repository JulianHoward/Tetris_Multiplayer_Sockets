package x2;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Servidor extends JFrame {
    int port = 0;
    ServerSocket ss;
    HashMap clienteColl = new HashMap();

    public Servidor() {
        try {
            this.ss = new ServerSocket(2089);
            (new Servidor.ClientAccept()).start();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Servidor();
    }

    class PrepareClientList extends Thread {
        PrepareClientList() {
        }

        public void run() {
            try {
                String ids = "";
                Set k = Servidor.this.clienteColl.keySet();

                Iterator itr;
                String key;
                for(itr = k.iterator(); itr.hasNext(); ids = ids + key + ",") {
                    key = (String)itr.next();
                }

                if (ids.length() != 0) {
                    ids = ids.substring(0, ids.length() - 1);
                }

                itr = k.iterator();

                while(itr.hasNext()) {
                    key = (String)itr.next();

                    try {
                        (new DataOutputStream(((Socket)Servidor.this.clienteColl.get(key)).getOutputStream())).writeUTF(":;.,/=" + ids);
                    } catch (Exception var6) {
                        Servidor.this.clienteColl.remove(key);
                    }
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }

        }
    }

    class MsgRead extends Thread {
        Socket s;
        String ID;

        MsgRead(Socket s, String ID) {
            this.s = s;
            this.ID = ID;
        }

        public void run() {
            while(!Servidor.this.clienteColl.isEmpty()) {
                try {
                    String i = (new DataInputStream(this.s.getInputStream())).readUTF();
                    System.out.println(i);
                    Set k = Servidor.this.clienteColl.keySet();
                    Iterator itr = k.iterator();

                    while(itr.hasNext()) {
                        String key = (String)itr.next();
                        if (!key.equalsIgnoreCase(this.ID)) {
                            try {
                                (new DataOutputStream(((Socket)Servidor.this.clienteColl.get(key)).getOutputStream())).writeUTF(i);
                            } catch (Exception var6) {
                            }
                        }
                    }

                    if (i.contains("-")) {
                        String[] nomb = i.split("-");
                        Servidor.this.clienteColl.remove(nomb[1]);
                    }
                } catch (Exception var7) {
                }
            }

        }
    }

    class ClientAccept extends Thread {
        ClientAccept() {
        }

        public void run() {
            while(true) {
                try {
                    Socket s = Servidor.this.ss.accept();
                    String i = (new DataInputStream(s.getInputStream())).readUTF();
                    DataOutputStream dout;
                    if (Servidor.this.clienteColl.containsKey(i)) {
                        dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("Ya estas registrado");
                    } else {
                        Servidor.this.clienteColl.put(i, s);
                        dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("");
                        (Servidor.this.new MsgRead(s, i)).start();
                        (Servidor.this.new PrepareClientList()).start();
                    }
                } catch (Exception var4) {
                }
            }
        }
    }
}
