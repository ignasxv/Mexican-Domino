import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.*;
import static org.junit.Assert.*;

public class MexicanTest {

    //////////////////////THE DOMINO CLASS/////////////////////

    /**
     * Test the toString method of the Domino class.
     */
    @Test
    public void testToString() {
        Domino domino = new Domino(3, 5);
        assertEquals("[3|5]", domino.toString());
    }

    /**
     * Test for the rotate method of the Domino class.
     */
    @Test
    public void testRotate() {
        Domino domino = new Domino(3, 5);

        // Check initial state
        assertEquals("[3|5]", domino.toString());

        // Rotate the domino
        domino.rotate();

        // Check after rotation
        assertEquals("[5|3]", domino.toString());
    }

    /**
     * Tests the equality of Domino objects using the equals method.
     */
    @Test
    public void testEquals() {
        // Create dominoes with the same values
        Domino domino1 = new Domino(4, 5);
        Domino domino2 = new Domino(5, 4);

        // Test equality
        assertEquals("Dominoes with the same values should be equal", domino1, domino2);

        // Create dominoes with the same values and rotate one of them
        domino2.rotate();

        // Test equality after rotation
        assertEquals("Rotated dominoes with the same values should still be equal", domino1, domino2);

        // Create dominoes with different values
        Domino domino3 = new Domino(4, 2);

        // Test inequality
        assertNotEquals("Dominoes with different values should not be equal", domino1, domino3);

    }


    ///////////////THE DOMINOTRAIN CLASS///////////////////

    /**
     * testing the constructor for the dominoTrain
     */
    @Test
    public void testDominoTrainConstructor() {
        // Creating a DominoTrain with a starting double domino 3
        DominoTrain<Domino> train = new DominoTrain<Domino>(3);
        assertNotNull(train);
        assertTrue(train.isEmpty());
        assertEquals(3, train.getEndValue());
    }

    @Test
    public void testAddToFrontTrain() {
        DominoTrain<Domino> train = new DominoTrain<Domino>(3);

        // Add a domino [3|5] to starting domino [3|3]
        System.out.println(train.getFirstNode().getElement());
        Domino domino1 = new Domino(5, 3);
        System.out.println(domino1.getFront());
        assertTrue(train.canAdd(domino1));
        train.addToFront(domino1);
        System.out.println(train.getFirstNode().getElement());
        assertFalse(train.isEmpty());
        assertEquals("[3|3]->[3|5]->", train.toString());

        // Add a domino [5|2]
        Domino domino2 = new Domino(2, 5);
        assertTrue(train.canAdd(domino2));
        train.addToFront(domino2);
        System.out.println(train.getFirstNode().getElement());
        System.out.println(train.toString());
        assertEquals("[3|3]->[3|5]->[5|2]->", train.toString());

        // Try to add an incompatible domino [4|8]
        Domino domino3 = new Domino(4, 8);
        train.addToFront(domino3);
        assertFalse(train.canAdd(domino3));
    }

    @Test
    public void testCanAdd() {
        DominoTrain<Domino> train = new DominoTrain<>(3);

        // Test canAdd with compatible domino [3|5]
        assertTrue(train.canAdd(new Domino(3, 5)));

        // Test canAdd with compatible domino [5|3]
        assertTrue(train.canAdd(new Domino(5, 3)));

        // Test canAdd with incompatible domino [2|8]
        assertFalse(train.canAdd(new Domino(2, 8)));
    }

    //////////HELPER METHODS////////////////////////////
    @Test
    public void testShufflingLinkedList(){
        LinkedList<Domino> brandNewDeck = Domino.generateDeck(4);
        System.out.println(brandNewDeck);
        System.out.println(brandNewDeck.shuffle());
    }
    ////////////MexicanTrainGameTest////////////////////

    @Test
    public void testGameSetup(){

        try {
            // for game with 2 players and 6 dominoes per player, you need 12 cards, but double-3 deck will only have 10 dominoes
            MexicanTrainGame.setup(6, 2, 3, true);
        } catch (IllegalArgumentException e){
            assertTrue("Can start a game with unsuitable parameters",true);
        }

        MexicanTrainGame.setup(3, 2, 3, true);
        assertEquals("The first Domino in the train should be the doublet with value provided during setup", MexicanTrainGame.mexicanTrain.firstElement(), new Domino(3, 3));
        assertEquals("after dealing a total of six dominoes to two players and the biggest doublet used in front of train we should be left with 3 dominoes in the deck", 3, MexicanTrainGame.gameDeck.length() );
        assertTrue("The first domino in each of the players train must be equal to the one in the Main mexican train", MexicanTrainGame.players[0].train.firstElement().equals(MexicanTrainGame.players[1].train.firstElement()) && MexicanTrainGame.players[0].train.firstElement().equals(MexicanTrainGame.mexicanTrain.firstElement()));
        System.out.println(MexicanTrainGame.gameDeck);
    }

    @Test
    public void testDrawFromPile(){
        //generated an unshuffled gameDeck to make easier testing
        MexicanTrainGame.setup(3, 2, 3, false);
        System.out.println(MexicanTrainGame.gameDeck);
        MexicanTrainGame.display();
        //At this point the game should look as follows
        /*
            Drawing pile: [1|3][2|2][2|3]
            Mexican Train: [3|3]

            Player 1:
            Hand: [0|2][0|1][0|0]
            Train: [3|3]

            Player 2:
            Hand: [1|2][1|1][0|3]
            Train: [3|3]
        */
        assertTrue("Drawing from the game pile [1|3][2|2][2|3]", MexicanTrainGame.players[0].drawFromPile() );
        assertEquals("Domino [1|3] has been added to player one's hand", new Domino(1, 3), MexicanTrainGame.players[0].hand.firstElement());
        assertEquals("The remaining pile is [2|2][2|3]", "[2|2][2|3]", MexicanTrainGame.gameDeck.toString() );
        MexicanTrainGame.players[0].drawFromPile();
        assertEquals("The remaining pile is [2|3]", "[2|3]", MexicanTrainGame.gameDeck.toString());
        MexicanTrainGame.players[0].drawFromPile();
        assertFalse("after drawing all dominos we should not be able to draw anything else from the pile", MexicanTrainGame.players[0].drawFromPile() );
        assertTrue("The pile is empty", MexicanTrainGame.gameDeck.isEmpty());
    }


    @Test
    public void testPlayToTrains(){
        MexicanTrainGame.setup(3, 2, 3, false);
        MexicanTrainGame.Player player1 =  MexicanTrainGame.players[0];
        MexicanTrainGame.Player player2 =  MexicanTrainGame.players[1];
        MexicanTrainGame.display();

        //the game looks as follows at this point
        /*
            Drawing pile: [1|3][2|2][2|3]
            Mexican Train: [3|3]

            Player 1:
            Hand: [0|2][0|1][0|0]
            Train: [3|3]

            Player 2:
            Hand: [1|2][1|1][0|3]
            Train: [3|3]
        */

        assertFalse("Player 1 trying to play a domino he does not have", MexicanTrainGame.Player.playPlayerToPlayer(player1, player2, new Domino(5, 3)) );
        assertFalse("player 1 tyring to play a domino he have but not playable", MexicanTrainGame.Player.playPlayerToPlayer( player1, player2, new Domino(0, 2)) );
        assertTrue("Player 2  playing a domino playable to his own train", MexicanTrainGame.Player.playPlayerToPlayer(player2, player2, new Domino(3,0)) );
        assertEquals("Player 2s train should now be [3|3][3|0]", "[3|3]->[3|0]->", player2.train.toString() );
        assertTrue("player 1 draws [1|3] a domino from the drawing pile", player1.drawFromPile());
        assertTrue("player 1 can now play into his own train",  MexicanTrainGame.Player.playPlayerToPlayer(player1, player1, new Domino(3, 1) ));
        assertFalse("player 1 should not be able to play a domino (whether playable or non-playable) on player 2s closed train", MexicanTrainGame.Player.playPlayerToPlayer(player1, player2,new Domino(3,2)) && MexicanTrainGame.Player.playPlayerToPlayer(player1, player2,new Domino(2,5)) );

        //open player 2s train
        player2.isTrainOpen = true;
        MexicanTrainGame.display();
        System.out.println(player2.isTrainOpen);
        player1.hand.addToFront(new Domino(0,5));
        player1.hand.addToFront(new Domino(1,5));

        assertTrue("Player1 should now be able to play on player2's open train", MexicanTrainGame.Player.playPlayerToPlayer(player1, player2, new Domino(0,0) ));
        MexicanTrainGame.display();
        assertTrue("Player1 should now be able to play on player2's open train", MexicanTrainGame.Player.playPlayerToPlayer(player1, player2, new Domino(5,0) ));
        MexicanTrainGame.display();
        assertTrue("Player1 should now be able to play on player2's open train", MexicanTrainGame.Player.playPlayerToPlayer(player1, player2, new Domino(1,5)));
        MexicanTrainGame.display();
        player2.isTrainOpen = false; //clasing player 2s train
        assertFalse("Player 1 should not be able to play to player 2s train since it is now closed", MexicanTrainGame.Player.playPlayerToPlayer(player1, player2, new Domino(1,0)));
        player2.isTrainOpen = true; //reopening player 2s train
        assertTrue("Player1 should now be able to play on player2's open train", MexicanTrainGame.Player.playPlayerToPlayer(player1, player2, new Domino(1,0)));
        MexicanTrainGame.display();
        assertTrue("Player 1s hand should now be empty", player1.hand.isEmpty());
//        assertFalse(true);
    }

    @Test
    public void testPlayGame(){
        MexicanTrainGame game = new MexicanTrainGame();

        game.setup(12, 4, 9, true);
        game.display();
//        game.players[0].drawFromPile();
//        game.players[0].drawFromPile();
//        game.players[0].drawFromPile();
        game.display();
//        game.mexicanTrain.remove(new Domino(6,6));
        game.mexicanTrain.remove(new Domino(6,6));
        System.out.println(game.mexicanTrain.length());
        game.display();
        System.out.println(game.mexicanTrain);

//        LinkedList<Domino> newDeck = MexicanTrainGame.generateDeck(1,false);
//        System.out.println(newDeck);
//        newDeck.remove(new Domino(0,1));
//        System.out.println(newDeck);
//        System.out.println(new Domino());
    }

    @Test
    public void testAddToMexican() {

    }


}

