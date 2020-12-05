package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.KeyboardBot;
import common.MessageBot;
import common.User;
import org.json.simple.parser.ParseException;
import vocabulary.Selection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SetNameVocabularies extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        Event event = checkCommand(query, user);
        String word = user.getName();

        if (event == Event.CHANGE_TOPIC) {
            user.delLastAddMyVocabularies();
            return move(event).action(word,
                    new KeyboardBot(new ArrayList<>(user.getMyVocabularies().keySet())));
        }

        if (event != Event.NONE) {
            user.delLastAddMyVocabularies();
            return move(event).action(word);
        }

        if (user.getMyVocabularies().containsKey(query))
            user.setLastAddMyVocabularies("My_" + user.getMyVocabularies() + "_" + query);
        else
            user.setLastAddMyVocabularies(query);

        if (user.getStateLearn().getKey().equals("")) {
            event = Event.CHANGE_TOPIC;
            return move(event).action(word,
                    new KeyboardBot(new ArrayList<>(user.getMyVocabularies().keySet())));
        }
        else{
            Selection vocabulary = user.getMyVocabularies().get(user.getStateLearn().getKey());
            user.setStateLearn(vocabulary.getEnWord(user));
            word = user.getStateLearn().getValue().getEn();
            event = Event.FIRST_EN_WORD;
        }

        return move(event).action(word);
    }
}
