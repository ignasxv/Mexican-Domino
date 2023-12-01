import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;


import static org.junit.Assert.assertTrue;

public class MexicanTrainGame extends Application {

    private static int playersTillNow=0;

    //The mexican train for the game
    static DominoTrain<Domino> mexicanTrain;

    //set the maximum number of players the game can support
    static final int maxPlayers = 4;

    //defualt number of players
    private static int numOfPlayers;

    //Array of players
    static Player[] players;

    //The deck for this game
    static LinkedList<Domino> gameDeck;

    //the maxdoublet (value in front of each train)
    static int frontDoublet; //can't modify unless non private

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    static HBox mexicanTrainBox;

    static VBox mainStageVboxContainer;

    static boolean hasApplicationStarted = false;

    static Player currentPlayer = new Player();

    static Button addToMexicanButton;

    static Scene mainCommonScene;

    static ArrayList<DominoTrain<Domino>> openTrains = new ArrayList<>();


    //constructing the game parameters
    public static void setup(int cardPerPlayer, int numOfPlayers, int biggestDoubletVal, boolean shuffle){

        if( cardPerPlayer*numOfPlayers > ((biggestDoubletVal + 1)*(biggestDoubletVal + 2))/2 -1)
            throw new IllegalArgumentException("The deck created must be enough to fit the number of players and card per players");

        playersTillNow = 0;
        frontDoublet = biggestDoubletVal;
        mexicanTrain = new DominoTrain<>(frontDoublet);
        openTrains.add(mexicanTrain);
        MexicanTrainGame.numOfPlayers = numOfPlayers;

        //decide whether the gameDeck should be shuffled or unshuffled
        gameDeck = (shuffle) ? Domino.generateDeck(frontDoublet).shuffle() : Domino.generateDeck(frontDoublet);

        players = new Player[numOfPlayers];

        for(int i = 0; i < numOfPlayers; i++){
            players[i] = new Player();
        }

        deal(players, cardPerPlayer);
        MexicanTrainGame.currentPlayer = players[0];
    }

    /**
     * Starts the GUI.  This method creates the visual of a window with 4 buttons.
     * @param primaryStage the main window of the application
     */
    public void start(Stage primaryStage) throws Exception {

        hasApplicationStarted = true;
        MexicanTrainGame.setup(12,3, 9, true);
        setMainStage(primaryStage);
    }

    /**
     * The main method needed to run the application
     * @param args the command line arguments are currently ignored
     */
    public static void main(String[] args) {
        setup(12, 2, 9, true);
        launch(args);
    }


    public static void deal(Player[] players, int cardPerPlayer){
        for(Player player: players) {
            for (int i = 0; i < cardPerPlayer; i++)
                player.hand.addToFront(gameDeck.removeFromFront());
        }
    }

    public static void setMainStage(Stage primaryStage){
        setFx();
        primaryStage.setTitle("THE MEXICAN TRAIN");

        mainStageVboxContainer = new VBox(2);

        primaryStage.setY( (Player.stageHeight + 40) * numOfPlayers );
        primaryStage.setX( screenSize.getWidth()/8.5);
//        BorderPane(Node center, Node top, Node right, Node bottom, Node left
        BorderPane borderPane = new BorderPane();
        mexicanTrainBox = new HBox(2);
        mainStageVboxContainer.getChildren().add(mexicanTrainBox);

        for(Player p: players){
            mainStageVboxContainer.getChildren().add(p.trainBox);
        }

        mainCommonScene = new Scene(mainStageVboxContainer, screenSize.getWidth()/1.5, screenSize.getHeight() - (Player.stageHeight + 44) * (numOfPlayers*1.5) );
        mainCommonScene.setFill(Paint.valueOf("#fcc200"));
        addToMexicanButton = new Button("Add to MEXICAN");

        addToMexicanButton.setOnAction( Player.addToMexicanButtonOnClick() );
        mexicanTrainBox.getChildren().addAll( addToMexicanButton, new Button( mexicanTrain.firstElement().toString()));

        BackgroundImage myBI= new BackgroundImage(new Image("https://cdn.britannica.com/45/18445-050-59915B6F/Dominoes.jpg"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        mainStageVboxContainer.setBackground(new Background(myBI));

        //setting up the players
        for(Player player: players){
            player.setPlayerStage(new Stage());
            player.displayStage();
            player.refreshScenes();
        }

        primaryStage.setScene(mainCommonScene);
        primaryStage.show();
    }

    public static void setFx(){
        for(Player player: players){

            player.lockStateButton = new Button();
            player.lockStateButton.setOnAction(player.lockStateButtonOnClick());


            player.trainBox = new HBox(2);
            player.trainBox.getChildren().addAll(player.lockStateButton, new Button(mexicanTrain.firstElement().toString()) );

            player.hboxContainer = new VBox();
            player.handRow = new HBox(2);
            player.buttonRow = new HBox(3);

            player.drawButton = new Button("Draw");
            player.endMyTurnButton = new Button("Pass");


            player.buttonRow.getChildren().add(player.drawButton);
            player.drawButton.setOnAction(e -> {
               player.drawFromPile();
               if(!player.hasPlayableDomino()) {
                   player.isTrainOpen = true;
                   openTrains.add(player.train);
                   player.goToNextPlayer();
               }
               player.buttonRow.getChildren().add(player.endMyTurnButton);
               player.refreshScenes();

            });

            player.endMyTurnButton.setOnAction(e -> {
                player.buttonRow.getChildren().remove(player.endMyTurnButton);
                player.isTrainOpen = true;
                player.goToNextPlayer();

            });


        }


    }

    static void display(){
        System.out.println("Drawing pile: " + gameDeck);
        System.out.println("Mexican Train: " + mexicanTrain + "\n");
        for(Player player : players)
            System.out.println(player + "\n");
    }

    static Background background1(){
        Color backgroundColor = Color.web("#5ab897");
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, null, null);
        return new Background(backgroundFill);
    }

    static Background backgroundDefault(){
        Color backgroundColor = Color.web("#b6c1d4");
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, null, null);
        return new Background(backgroundFill);
    }


    public static class Player{
        LinkedList<Domino> hand = new LinkedList<>();

        DominoTrain<Domino> train;

        boolean isTrainOpen = false;

        String name;

        int number;

        Stage stage;

        Button drawButton;

        Button lockStateButton;

        Button passMyTurnButton;
        
        HBox handRow;

        HBox buttonRow;
        
        VBox hboxContainer;

        HBox trainBox;

        static Domino currentlySelectedDomino;

        static double stageHeight = screenSize.getHeight()/14;
        static double stageWidth = screenSize.getWidth();

        Scene playerScene;

        Button endMyTurnButton;

        Player(){
            playersTillNow++;
            number = playersTillNow;
            this.name = "Player " + (playersTillNow);
            this.train = new DominoTrain<>(frontDoublet);
        }

        @Override
        public String toString(){
//            return name + ((DominoTrain<Domino>) hand).toString(); /////////////
            return name + ": " + "\n" + "Hand: " + hand + "\n" + "Train: " + train;
        }

        public boolean drawFromPile(){
            if(gameDeck.isEmpty())
                return false;
            Domino drawedDomino = gameDeck.removeFromFront();
            this.hand.addToFront( drawedDomino );

            if(hasApplicationStarted){ //make sure the JavaFx components are only called when application has started
                drawedDomino.setButton();
                drawedDomino.getButton().setOnAction(dominoOnClick(drawedDomino));
                handRow.getChildren().add(drawedDomino.getButton());
            }

            return true;
        }

//        public boolean addToOwnTrain(Domino domino){
//            if(hand.contains(domino) ){
//                hand.remove(domino);
//                return train.addToFront(domino);
//            }
//            return false;
//        }

        public static boolean playPlayerToPlayer(Player from, Player to, Domino domino){
            if( (to.isTrainOpen || from == to) && from.hand.contains(domino) && to.train.addToFront(domino)){
                openTrains.remove(from.train);
                from.isTrainOpen = false;
                from.hand.remove(domino);
                from.goToNextPlayer();
                return true;
            }
            return false;
        }

         boolean addToMexicanTrain(Domino domino){
            if(hand.contains(domino) && mexicanTrain.addToFront(domino)){
                isTrainOpen = false;
                openTrains.remove(train);
                refreshScenes();
                hand.remove(domino);
                goToNextPlayer();
                return true;
            }
            return false;
        }


        public void setPlayerStage(Stage stage){

            this.stage = stage;
            double screenDivisions = screenSize.getHeight()/numOfPlayers;
            stage.setX(0);
            stage.setY( (stageHeight + 40) * (number-1) );
            stage.setTitle(name);
        }

        static EventHandler<ActionEvent> addToMexicanButtonOnClick(){

            return ( e ->  {
                if(currentPlayer != null){
                    if(currentPlayer.addToMexicanTrain(currentlySelectedDomino)){
                        mexicanTrainBox.getChildren().add(new Button(currentlySelectedDomino.toString()));
                        currentPlayer.handRow.getChildren().remove(currentlySelectedDomino.getButton());
                        System.out.println("Mexicana: " + MexicanTrainGame.mexicanTrain);
                    }
                }
            });
        }

        EventHandler<ActionEvent> dominoOnClick(Domino domino){

            return (e -> {
                if(currentlySelectedDomino != null){
                    currentlySelectedDomino.getButton().setStyle("");
                }
                currentlySelectedDomino = domino;
                currentlySelectedDomino.getButton().setStyle("-fx-background-color: grey;");

                System.out.println(domino);

//                    domino.getButton().setText(domino.toString());
            });
        }

        EventHandler<ActionEvent> lockStateButtonOnClick(){
            return( e -> {
                if( Player.playPlayerToPlayer( currentPlayer, this,  currentlySelectedDomino)){
                    trainBox.getChildren().add(new Button(currentlySelectedDomino.toString()));
                    handRow.getChildren().remove(currentlySelectedDomino.getButton());
                    System.out.println("Mexicana: " + MexicanTrainGame.mexicanTrain);
                }
            } );
        }

        boolean hasPlayableDomino(){
            for(Domino domino: hand){
                for(DominoTrain<Domino> train: openTrains){
                    if(train.canAdd(domino))
                        return true;
                }
            }
            return false;
        }

        public void displayStage(){

            hboxContainer.getChildren().addAll(buttonRow, handRow);

            for( Domino domino : hand){

                domino.setButton();
                handRow.getChildren().add(domino.getButton());
                domino.getButton().setOnAction(dominoOnClick(domino));
            }

            playerScene = new Scene(hboxContainer, stageWidth, stageHeight);

            stage.setScene(playerScene);
            stage.show();
//            MexicanTrainGame.display();
        }

//        public boolean havePlayableDomino(){
//            for( Domino domino: hand ){
//                if()
//            }
//        }

        void goToNextPlayer(){
            currentPlayer = players[ currentPlayer.number % numOfPlayers ];
            refreshScenes();
        }

        void refreshScenes(){
            System.out.println(MexicanTrainGame.currentPlayer.name);
            lockStateButton.setText( ( isTrainOpen ) ? "Add to: " + name + "'s" :  name + "'s Closed ");
            lockStateButton.setStyle( ( isTrainOpen ) ? "-fx-background-color: #e0967e" : "-fx-background-color: #67c268;");

            currentPlayer.lockStateButton.setText("Add to own Train");
            currentPlayer.lockStateButton.setStyle( ( currentPlayer.isTrainOpen ) ? "-fx-background-color: #deb9ad;" : "-fx-background-color: #87ab87;");

//            MexicanTrainGame.currentPlayer.trainBox.setDisable(true);

            for(Player player: MexicanTrainGame.players){
                if(player != currentPlayer){
                    player.hboxContainer.setBackground( MexicanTrainGame.backgroundDefault() );
                    player.hboxContainer.setDisable(true);
//                    player.handRow.setDisable(true);
                }
                else{
                    player.hboxContainer.setBackground( MexicanTrainGame.background1() );
                    player.hboxContainer.setDisable(false);
//                    player.handRow.setDisable(false);
                }
            }

        }





    }


}
