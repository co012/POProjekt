package simulator;


import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public final class NumberField extends TextField {
    private final static String MAX_INT_IN_STRING = String.valueOf(Integer.MAX_VALUE);

    //TODO:DO I really need to Override both
    public NumberField() {
        this("");
    }

    public NumberField(String s) {
        super(s);
        //TODO:leading zero fix
        UnaryOperator<TextFormatter.Change> changeUnaryOperator = (change) -> {
            if (change.isDeleted()) return change;
            if (change.getText().matches("^\\d+$")) {
                String newText = change.getControlNewText();
                if (isLessThanMaxInt(newText)) return change;

                return null;
            }
            return null;
        };

        this.setTextFormatter(new TextFormatter<>(changeUnaryOperator));
    }

    private boolean isLessThanMaxInt(String string){
        if("".equals(string)) return true;
        if(MAX_INT_IN_STRING.length() != string.length()) return MAX_INT_IN_STRING.length() > string.length();

        return MAX_INT_IN_STRING.compareTo(string) > 0;
    }

    public int getInt() {
        return Integer.parseInt(this.getText());
    }
}
