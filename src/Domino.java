import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Collections;

public class Domino {

    //value in front of the domino
    private int front;

    //value in back of the domino
    private int back;

    //button for each domino
    private Button button;

    // Constructor
    public Domino(int back, int front) {
        this.front = front;
        this.back = back;

    }

    Button getButton(){
        button.setText(toString());
        return button;
    }

    void setButton(){
        button = new Button(toString());
    }

    int getFront(){
        return front;
    }

    int getBack(){
        return back;
    }

    // Rotate method
    public void rotate() {
        int buffer = front;
        front = back;
        back = buffer;
    }

    @Override
    public boolean equals(Object o){
        return getBack() == ((Domino) o).getBack() && getFront() == ((Domino) o).getFront();
    }

    // toString method
    @Override
    public String toString() {
        return "[" + back + "|" + front+ "]";
    }

    public static LinkedList<Domino> generateDeck(int maxDoublet){
        LinkedList<Domino> deck = new LinkedList<>();
        for(int rows = 0; rows < maxDoublet; rows++){
            for(int cols = rows; cols <= maxDoublet; cols++){
                deck.addToEnd(new Domino(cols, rows));
//                System.out.println("[" + rows + "|" + cols + "]");
            }
//            System.out.println("\n");
        }
        return deck;
    }

}
