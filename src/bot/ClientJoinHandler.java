package bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import constants.League;
import constants.Propertie;
import requests.Mysql;
import requests.RiotApi;

public class ClientJoinHandler {

    public ClientJoinHandler () {}

    public void check (TS3Api api, ClientJoinEvent e) {
        System.out.println();
        // requesting League of Legends unique identifier from MySQL databse
        long clientAccountId = Mysql.databaseIdentifier(e.getUniqueClientIdentifier());

        // initializing serverTier with UNKNOWN
        League accountLeague;
        // if unique identifier isn't UNKNOWN, requesting League of Legends Solo/Duo Queue Tier from riot api
        if (clientAccountId != 0) {
            accountLeague = RiotApi.getLeagueBySummonerId(clientAccountId);
            System.out.println("Users league from account: " + accountLeague.getName());
        } else {
            if (Boolean.parseBoolean(Initialize.messages.get(Propertie.REMINDER))) {
                // if someone isn't in database
                api.pokeClient(e.getClientId(), Initialize.messages.get(Propertie.NOTINDATABASE));
            }
            return;
        }

        // initializing some variables
        League clientLeague;
        // getting client
        ClientInfo client = api.getClientInfo(e.getClientId());

        try {
            if (client.isInServerGroup(Integer.parseInt(Initialize.servergroups.get(Propertie.BRONCE)))) {
                clientLeague = League.BRONCE;
            } else if (client.isInServerGroup(Integer.parseInt(Initialize.servergroups.get(Propertie.SILVER)))) {
                clientLeague = League.SILVER;
            } else if (client.isInServerGroup(Integer.parseInt(Initialize.servergroups.get(Propertie.GOLD)))) {
                clientLeague = League.GOLD;
            } else if (client.isInServerGroup(Integer.parseInt(Initialize.servergroups.get(Propertie.PLATINUM)))) {
                clientLeague = League.PLATINUM;
            } else if (client.isInServerGroup(Integer.parseInt(Initialize.servergroups.get(Propertie.DIAMOND)))) {
                clientLeague = League.DIAMOND;
            } else if (client.isInServerGroup(Integer.parseInt(Initialize.servergroups.get(Propertie.MASTER)))) {
                clientLeague = League.MASTER;
            } else if (client.isInServerGroup(Integer.parseInt(Initialize.servergroups.get(Propertie.CHALLENGER)))) {
                clientLeague = League.CHALLENGER;
            } else {
                clientLeague = League.UNRANKED;
            }

            System.out.println("Users league from teamspeak: " + clientLeague.getName());

            // assign/remove new/old server group (if it's different)
            if (!clientLeague.equals(accountLeague)) {
                System.out.println("Leagues are different, changing it...");
                int ivoker = e.getClientDatabaseId();

                api.removeClientFromServerGroup(Integer.parseInt(Initialize.servergroups.get(Propertie.getPropertieByName(clientLeague.getName()))), ivoker);
                api.addClientToServerGroup(Integer.parseInt(Initialize.servergroups.get(Propertie.getPropertieByName(accountLeague.getName()))), ivoker);

                System.out.println("League update for " + e.getClientNickname() +
                        " from group " + clientLeague.getName() + " to " + accountLeague.getName() + "\n");
            }
        } catch (NumberFormatException error) {
            error.printStackTrace();
        }
    }
}