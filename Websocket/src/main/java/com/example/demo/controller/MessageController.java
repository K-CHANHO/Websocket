package com.example.demo.controller;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@ServerEndpoint("/websocket")
public class MessageController extends Socket {
	
	private static final List<Session> session = new ArrayList<Session>();
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/outside")
	public String outside() {
		return "outside";
	}
	
	@OnOpen
	public void open(Session newUser) throws IOException {
		System.out.println("connected");
		session.add(newUser);
		System.out.println("채팅방 인원 : " + session.size());
	}
	
	@OnMessage
	public void getMsg(Session recieveSession, String msg) {
		
		for (int i = 0; i < session.size(); i++) {
			if(!recieveSession.getId().equals(session.get(i).getId())) {
				try {
					session.get(i).getBasicRemote().sendText(recieveSession.getId() + " : " + msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					session.get(i).getBasicRemote().sendText(" 나 : " + msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	@OnClose
	public void close(Session User) throws IOException {
		System.out.println(User.getId() + "퇴장");
		session.remove(User);
	}

}
