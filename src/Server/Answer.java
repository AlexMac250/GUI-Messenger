package Server;

import ru.universum.Loader.Command;
import ru.universum.Loader.Friend;

public class Answer {
    int idOf = 0;
    String answer = "";
    WorkingServ serv;
    public Answer(int idOf, String answer, WorkingServ serv) {
        this.serv = serv;
        this.idOf = idOf;
        this.answer = answer;
        if(answer.equals("yes")){
            Server.accs.get(idOf).getWorkingServ().newFriend(new Friend(serv.acc.id, serv.acc.login));
            serv.newFriend(new Friend(Server.accs.get(idOf).id,Server.accs.get(idOf).login));
        }else {
            serv.send(new Command("friended", "false"));
        }
    }
}