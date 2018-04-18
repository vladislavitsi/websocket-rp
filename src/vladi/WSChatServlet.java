package vladi;
import java.io.IOException;
import java.util.ArrayList;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws")
public class WSChatServlet {
    private static ArrayList<Session> sessionList = new ArrayList<>();
    private static ArrayList<String> messageHistory = new ArrayList<>();

    @OnOpen
    public void onOpen(Session session){
        try{
            sessionList.add(session);
            RemoteEndpoint.Basic remote = session.getBasicRemote();
            for (String message : messageHistory) {
                remote.sendText(message);
            }
        }catch(IOException e){}
    }

    @OnClose
    public void onClose(Session session){
        sessionList.remove(session);
    }

    @OnMessage
    public void onMessage(String message){
        if (message != null && !message.trim().isEmpty()) {
            try{
                messageHistory.add(message);
                for(Session session : sessionList){
                    //asynchronous communication
                    session.getBasicRemote().sendText(message);
                }
            }catch(IOException e){
            }
        }
    }
}