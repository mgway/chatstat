package com.matthewgalloway.stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IrcClient {
	
	@Value("${irc.server}")
	private String IRC_SERVER;
	
	@Value("${irc.port}")
	private int IRC_PORT;
	
	@Value("${irc.nick}")
	private String IRC_NICK;
	
	@Value("${irc.auth}")
	private String IRC_AUTH;
	
	
	static void sendString(BufferedWriter bw, String str) {
		try {
			bw.write(str + "\r\n");
			bw.flush();
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}

	public List<String> connect(String channel) {
		Socket socket = null;
		ArrayList<String> channelMembers = new ArrayList<String>();
		
		try {

			socket = new Socket(IRC_SERVER, IRC_PORT);
			System.out.println("*** Connected to server.");
			
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
			System.out.println("*** Opened OutputStreamWriter.");
			
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader breader = new BufferedReader(inputStreamReader);

			BufferedWriter bwriter = new BufferedWriter(outputStreamWriter);
			System.out.println("*** Opened BufferedWriter.");

			sendString(bwriter, "PASS " + IRC_AUTH);
			sendString(bwriter, "NICK " + IRC_NICK);
			
			String line = null;
			while ((line = breader.readLine()) != null) {
				if (line.indexOf("004") >= 0) {
					// We are now logged in.
					break;
				} else if (line.indexOf("433") >= 0) {
					System.out.println("Nickname is already in use.");
					return null;
				}
			}
			
			Pattern p = Pattern.compile(":.*#.*:(.*)$");
			
			// Otherwise Twitch won't send us /NAMES
			sendString(bwriter, "CAP REQ :twitch.tv/membership");
			sendString(bwriter, "JOIN " + channel);
			while ((line = breader.readLine()) != null) {
				System.out.println(line);
				
				if(line.indexOf("353") >= 0) {
					Matcher m = p.matcher(line);
					if(m.matches()){
						String users = m.group(1);
						for(String user: users.split(" ")) {
							channelMembers.add(user);
						}
					}
				} else if (line.indexOf("366") >= 0) {
					break;
				}
			}

			bwriter.close();
			socket.close();
			
			return channelMembers;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
		
	}
}
