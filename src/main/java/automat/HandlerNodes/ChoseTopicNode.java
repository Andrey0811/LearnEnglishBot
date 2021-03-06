package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import user.User;

import java.util.ArrayList;

public class ChoseTopicNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        String word = user.getName();
        Event event;

        if (user.getMyVocabularies().containsKey(query)
                && user.getMyVocabularies().get(query).getSize() != 0) {
            user.setStateLearn(query);
            word = getFirstWord(query, user);
            event = Event.FIRST_EN_WORD;
        } else {
            user.getMyVocabularies().remove(query);
            return move(Event.WRONG_TOPIC).action(
                    word, new ArrayList<>(user.getMyVocabularies().keySet()));
        }

        return move(event).action(word);
    }
}
