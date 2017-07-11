package bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import constants.Propertie;
import requests.Mysql;
import requests.RiotApi;

public class MessageHandler {

    public MessageHandler () {}

    public void check (TS3Api api, TextMessageEvent e, String message) {
        if (message.equals("!ping")) {
            // simple ping test
            api.sendChannelMessage("Pong");

        } else if (message.equals("!help")) {
            // a bot can't help
            api.sendChannelMessage(Initialize.messages.get(Propertie.HELP));

        } else if (message.equals("!name"))  {
            // short explanation for the name system
            api.sendChannelMessage(Initialize.messages.get(Propertie.NAME));

        } else if (message.startsWith("!name ")) {
            System.out.println("Checking league name...");
            // format !name {League of Legends name}
            String leagueName = message.substring(6);

            // check if name is spelled right, or riot server is down
            if (Mysql.editDatabaseIdentifier(leagueName, e.getInvokerUniqueId(), RiotApi.getIdBySummonerName(leagueName))) {
                api.sendChannelMessage("[b]" + e.getInvokerName() +
                        "[/b] " + Initialize.messages.get(Propertie.SUCCESS));
            } else {
                api.sendChannelMessage(Initialize.messages.get(Propertie.ERROR));
            }

        } else if (message.startsWith("!")) {
            // after all equals, error message
            api.sendChannelMessage(Initialize.messages.get(Propertie.COMMAND));

        }
    }
}