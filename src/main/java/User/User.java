package User;

import automat.HandlerNode;
import common.Event;
import common.Tuple;
import org.json.simple.JSONObject;
import vocabulary.Selection;
import vocabulary.Word;

import java.util.HashMap;

public class User {
    private final String userName;
    private final String id;
    private final HashMap<String, Selection> myVocabularies;
    private Tuple<Event, HandlerNode> stateDialog;
    private Tuple<String, Integer> stateLearn;
    private Tuple<String, Integer> stateAddVocabulary;
    private Integer countWordsVocabulary;

    public User(HashMap<String, Selection> startVocabularies) {
        userName = "";
        id = "-1";
        this.myVocabularies = startVocabularies;
        stateAddVocabulary = new Tuple<>("", -1);
    }

    public User(String name, String id,
                HashMap<String, Selection> startVocabularies) {
        userName = name;
        stateDialog = new Tuple<>(Event.FIRST_START, null);
        stateLearn = new Tuple<>("", -1);
        stateAddVocabulary = new Tuple<>("", -1);
        this.id = id;
        this.myVocabularies = startVocabularies;
    }

    public User(String name, String id,
                HashMap<String, Selection> startVocabularies,
                Tuple<String, Integer> stateLearn) {
        userName = name;
        stateDialog = new Tuple<>(Event.SECOND_START, null);
        this.stateLearn = stateLearn;
        stateAddVocabulary = new Tuple<>("", -1);
        this.id = id;
        this.myVocabularies = startVocabularies;
    }

    public String getName() {
        return userName;
    }

    public String getId() {
        return this.id;
    }

    public Tuple<Event, HandlerNode> getStateDialog() {
        return stateDialog;
    }

    public void setStateDialog(Event event) {
        stateDialog.setKey(event);
    }

    public void setStateDialog(HandlerNode handler) {
        stateDialog.setValue(handler);
    }

    public Tuple<String, Integer> getStateLearn() {
        return stateLearn;
    }

    public void setStateLearn(String name) {
        stateLearn.setKey(name);
    }

    public void setStateLearn(Integer word) {
        stateLearn.setValue(word);
    }

    public Word getCurrentLearnWord() {
        Selection vocabulary = myVocabularies.get(stateLearn.getKey());
        return vocabulary.getWord(stateLearn.getValue());
    }

    public HashMap<String, Selection> getUserVocabularies() {
        return myVocabularies;
    }

    public void addVocabulary(String name, Selection selection) {
        stateAddVocabulary.setTuple(name, -1);
        myVocabularies.put(name, selection);
    }

    public String getNextWordForAdd(boolean isKnow) {
        if (!isKnow)
            countWordsVocabulary--;

        stateAddVocabulary.setValue(stateAddVocabulary.getValue() + 1);
        if (stateAddVocabulary.getValue() >= myVocabularies.get(stateAddVocabulary.getKey()).getSize()
                || countWordsVocabulary == 0) {
            myVocabularies.get(stateAddVocabulary.getKey())
                    .delAllStartIdx(stateAddVocabulary.getValue());
            return "";
        }

        return getCurrentWord();
    }

    public void setUserVocabulary(String name) {
        Selection temp = myVocabularies.get(stateAddVocabulary.getKey());
        myVocabularies.remove(stateAddVocabulary.getKey());
        myVocabularies.put(name, temp);
        stateAddVocabulary.setTuple("", -1);
    }

    public void delUserVocabulary() {
        myVocabularies.remove(stateAddVocabulary.getKey());
        stateAddVocabulary = new Tuple<>("", 0);
    }

    public void delWordFromUserVocabulary() {
        stateAddVocabulary.setValue(stateAddVocabulary.getValue() - 1);
        myVocabularies.get(stateAddVocabulary.getKey())
                .delWord(stateAddVocabulary.getValue());
    }

    public void setCountWordsForAdd(int count) {
        countWordsVocabulary = count > 0
                ? count
                : 20;
    }

    public void setTranslateWord(String translate) {
        myVocabularies.get(stateAddVocabulary.getKey())
                .setTranslateWord(stateAddVocabulary.getValue(), translate);
    }

    public String getCurrentWord() {
        return myVocabularies.get(stateAddVocabulary.getKey())
                .getWithoutStat(stateAddVocabulary.getValue());
    }

    public int getSizeAddVocabulary() {
        return myVocabularies.get(stateAddVocabulary.getKey()).getSize();
    }

    public void delRemainingWord() {
        myVocabularies.get(stateAddVocabulary.getKey())
                .delAllStartIdx(stateAddVocabulary.getValue());
    }

    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", userName);
        jsonObject.put("id", id);
        jsonObject.put("stateLearn", stateLearn.getJson());
        jsonObject.put("myVocabularies", getJsonVocabularies());
        return jsonObject;
    }

    private JSONObject getJsonVocabularies() {
        JSONObject jsonObject = new JSONObject();
        for (String key : myVocabularies.keySet()) {
            jsonObject.put(key, myVocabularies.get(key).getJson());
        }
        return jsonObject;
    }
}
