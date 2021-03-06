package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import user.User;

public class StatisticNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user){
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        String word = user.getName();

        Event event = Event.WRONG;

        if (query.contains("тема")) {
            String[] arr = query.split(" ");

            if (arr.length >= 2 && user.getMyVocabularies().containsKey(arr[1])) {
                word = user.getMyVocabularies().get(arr[1])
                        .getSelectionStatistic();
                word = arr[1] + " - " + word;
            } else if (user.getMyVocabularies().containsKey(user.getStateLearn().getKey())) {
                word = user.getMyVocabularies().get(user.getStateLearn().getKey())
                        .getSelectionStatistic();
                word = user.getStateLearn().getKey() + " - " + word;
            } else
                word = "тут пока пусто";

            event = Event.STAT_STR;
        } else if (query.startsWith("слова")) {
            if (user.getMyVocabularies().containsKey(user.getStateLearn().getKey()))
                word = user.getMyVocabularies().get(user.getStateLearn().getKey())
                        .getWordsStatistic(5);
            else
                word = "тут пока пусто";

            event = Event.STAT_STR;
        }

        return move(event).action(word);
    }
}