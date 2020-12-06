package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;

import java.util.ArrayList;


public class WrongNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        String word = user.getName();

        if (query.equals("да")) {
            Event event = Event.HELP;

            if (user.getStateLearn().getValue() != null) {
                word = user.getStateLearn().getValue().getEn();
                event = Event.FIRST_EN_WORD;
            }

            if (user.getStateLearn().getKey().equals("")) {
                event = Event.CHANGE_TOPIC;
                return move(event).action(word,
                        new ArrayList<>(user.getUserVocabularies().keySet()));
            }

            return move(event).action(word);
        }

        return move(Event.EXIT).action(word);
    }
}
