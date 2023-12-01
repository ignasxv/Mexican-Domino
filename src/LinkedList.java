import java.util.*;

/**
 * A class to represent a linked list of nodes.  The list is iterable (i.e. we can loop over its data).
 */
public class LinkedList<T> implements Iterable<T> {
  /** the first node of the list, or null if the list is empty */
  private LLNode<T> firstNode;
  
  /**
   * Creates an initially empty linked list
   */
  public LinkedList() {
    firstNode = null;
  }

  /**
   * Overriding the constructor to create a LinkedList given the firstNode
   * @param firstNode is the firstNode of the LinkedList
   */
  public LinkedList(LLNode<T> firstNode){
//    System.out.println("Created a LinkedList with the given firstNode");
    this.firstNode = firstNode;
  }

  public LinkedList(ArrayList<T> arrayList) {
    for(T item: arrayList)
      this.addToFront(item);
  }
  @Override
  public String toString(){
    StringBuilder string = new StringBuilder();
    for( T element : this ){
      string.append(element);
    }
    return string.toString();
  }
  /**
   * Returns the first node.
   */
  protected LLNode<T> getFirstNode() {
    return firstNode;
  }

  /**
   * Changes the front node.
   * @param node  the node that will be the first node of the new linked list
   */
  protected void setFirstNode(LLNode<T> node) {
    this.firstNode = node;
  }

  /**
   * @return the first element of the linked list
   */
  public T firstElement(){
    return getFirstNode().getElement();
  }

  /**
   * Return whether the list is empty
   * @return true if the list is empty
   */
  public boolean isEmpty() {
    return (getFirstNode() == null);
  }

  public LinkedList<T> shuffle(){
    ArrayList<T> shuffleArr = new ArrayList<>();
    for(T element: this)
      shuffleArr.add(element);
    Collections.shuffle(shuffleArr);
    return new LinkedList<>(shuffleArr);
  }
  
  /**
   * Add an element to the front of the linked list
   */
  public boolean addToFront(T element) {
//    if(!isEmpty()) System.out.println(firstElement());
    setFirstNode(new LLNode<T>(element, getFirstNode()));
    return true;
  }
  
  /**
   * Removes and returns the element at the front of the linked list
   * @return the element removed from the front of the linked list
   * @throws NoSuchElementException if the list is empty
   */
  public T removeFromFront() {
    if (isEmpty())
      throw new NoSuchElementException();
    else {
      T save = getFirstNode().getElement();
      setFirstNode(getFirstNode().getNext());
      return save;
    }
  }

  /**
   * Returns the length of the linked list
   * @return the number of nodes in the list
   */
  public int length() {
    int lengthSoFar = 0;
    LLNode<T> nodeptr = getFirstNode();
    while (nodeptr != null) {
      lengthSoFar++;
      nodeptr = nodeptr.getNext();
    }
    return lengthSoFar;
  }

  /**
   * Add an element to the end of a list.
   * @param element the element to add
   */
  public void addToEnd(T element) {
    if (isEmpty())
      addToFront(element);
    else {
      LLNode<T> nodeptr = getFirstNode();
      while (nodeptr.getNext() != null)
        nodeptr = nodeptr.getNext();
      nodeptr.setNext(new LLNode<T>(element, null));
    }
  }

  /**
   * Determines whether an element is stored in the list
   * @param element  the element to search for in the list
   * @return true if and only if the parameter element is in the list
   */
  public boolean contains(T element) {
    if(getFirstNode() == null ){
      return false;
    }
    return getFirstNode().contains( element );

  }
  T thisElement(T element){
    for(T el: this){
      if(el.equals(element))
        return el;
    }
    return  element;

  }

  /**
   * Deletes the first occurrance of an element in the list.
   * If the element is not in the list, the list is unchanged.
   * @param element  the element to remove
   */
  public void remove(T element) {
//    System.out.println(element.getClass());
//    System.out.println(getFirstNode().getElement());
    if(isEmpty()){
      System.out.println("empty list");
    }
    else if ( getFirstNode().getElement().equals(element) ) {
      setFirstNode(getFirstNode().getNext());

    }

    else{
      LLNode<T> previous = getFirstNode();
      LLNode<T> nodePointer = getFirstNode().getNext();

      while (nodePointer != null) {

        if (nodePointer.getElement().equals(element)) {
          previous.setNext(nodePointer.getNext());
        }
        previous = nodePointer;
        nodePointer = nodePointer.getNext();
      }

    }


  }
  
  /**
   * The method required by the Iterable interface returns an iterator that loops over the data in the list.
   * @return an iterator that loops over the data in the list
   */
  public Iterator<T> iterator() {
    return new LinkedListIterator<T>(getFirstNode());
  }




  /** An iterator class for our linked list.  The iterator loops over the data in the list from
   * the first node to the last.
   */
  private class LinkedListIterator<T> implements Iterator<T> {

    // keeps track of which node will store the next value of the iteration
    private LLNode<T> nodeptr;

    //Keeps track of the last value returned by next
    private LLNode<T> lastReturned;

    private  LLNode<T> previous;

//  private static T tryLastReturned; //what's wront hereeeeeeee

    /**
     * Create an iterator that loops over the data in the list starting at the given first node
     * @param firstNode the node to start this loop over the data in the list
     */
    public LinkedListIterator(LLNode<T> firstNode) {
      nodeptr = firstNode;
    }

    /**
     * Returns true if there is more data we can loop over and false if the loop reached the end of the list.
     * @return true if there is more data to loop over
     */
    public boolean hasNext() {
      return nodeptr != null;
    }


    /**
     * Returns the next value from the linked list in this iterator that loops over the list data.
     * @return the next value in this loop over the linked list data
     * @throws NoSuchElementException if next() is called after the loop reaches the end of the list
     */
    public T next() {
      if (!hasNext())
        throw new NoSuchElementException();

      previous = lastReturned;
      lastReturned = nodeptr;
      nodeptr = nodeptr.getNext();
      return lastReturned.getElement();
    }

    @Override
    public void remove(){
      if (lastReturned == null) {
        throw new IllegalStateException("next has not been called, or remove is called more than once per call to next");
      }

      if (lastReturned.equals(nodeptr) || previous == null) {
        nodeptr = nodeptr.getNext();
      } else {
        previous.setNext(lastReturned.getNext());
      }

      lastReturned = null;
    }

  }


}
