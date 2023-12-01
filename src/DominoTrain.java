import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import java.util.ArrayList;
import java.util.Collections;

//public class DominoTrain<T> Extends LinkedList<T> //why this does not work
//public class DominoTrain<T> Extends LinkedList<Domino>
//public class DominoTrains<Domino> extends LinkedList<T>
//public class DominoTrain<Tsd> extends LinkedList<Domino> //why does this work
public class DominoTrain<T> extends LinkedList<Domino> {

    DominoTrain(int val) {
        super( new LLNode<>( new Domino(val, val), null) );
    }

//    @Override
//    public String toString(){
//        StringBuilder dominoTrain = new StringBuilder();
//        for( Domino domino : this )
//            dominoTrain.append( domino );
//        return dominoTrain.toString();
//    }

    @Override
    public String toString(){
        StringBuilder string = new StringBuilder();
        ArrayList<Domino> list = new ArrayList<>();
        for( Domino element : this ){
            list.add(element);
        }
//        return list.toString();
        Collections.reverse(list);
        for(Domino domino: list){
            string.append(domino).append("->");
        }
        return string.toString();
    }

    /**
     * A train is considered to be empty if it only contains the starting double domino
     * @return true if the DominoTrain contains more dominos other than the starting domino
     * Otherwise return false
     */
    @Override
    public boolean isEmpty(){
        return getFirstNode().getNext() == null;
    }

    @Override
    public boolean addToFront(Domino domino){
        if(canAdd(domino)){
            System.out.println(domino + " can be indeed added");
            System.out.println("front " + domino.getFront());
            System.out.println("Back " + domino.getBack());
            System.out.println("EndVal: " + getEndValue());

            if( domino.getFront() == getEndValue() ){
                System.out.println("need to rotate");
                domino.rotate();
                System.out.println(domino);
                super.addToFront(domino);
//                super.addToFront(domino);
            }
            else
                super.addToFront(domino);
            return true;
        }
        return false;
    }

    boolean canAdd(Domino domino){
        return ( domino.getBack() == getEndValue() || domino.getFront() == getEndValue() );
    }

    int getEndValue(){
        return firstElement().getFront();
    }



}