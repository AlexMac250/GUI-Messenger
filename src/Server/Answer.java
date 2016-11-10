package Server;

import ru.universum.Loader.Command;
import ru.universum.Loader.Friend;
import ru.universum.Loader.Message;

class Answer {

    Answer(int idOf, String answer, WorkingServ serv) {

        if(answer.equals("yes")){
            Server.accs.get(idOf).getWorkingServ().newFriend(new Friend(serv.acc.id, serv.acc.login));
            serv.newFriend(new Friend(Server.accs.get(idOf).id,Server.accs.get(idOf).login));
        }else {
            if(Server.accs.get(idOf).isOnline) {
                // говорим человеку который отправил запрос , что его отклонили
                Server.accs.get(idOf).getWorkingServ().send(new Command("noFriended", serv.acc.login));
            }else {
                Server.accs.get(idOf).oflineMes.add(new Message("noFriended",serv.acc.login,"",false));
            }
        }
    }
}