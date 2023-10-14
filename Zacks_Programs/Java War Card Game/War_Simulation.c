//Name: Zachary Yore
//War_Simulation
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
typedef struct card_s{
int rank;    //number
char * type; //type of card
struct card_s * nextptr;
}card_t;
//function prototypes
void printList(card_t* n);
void rules(); //display rules
int playRound(); //simulate round
card_t * openCardDeck(); //open the card deck and place into a linkedlist setup
card_t * insertBackSetup(card_t *node, char *name, int cardrank); //take card from orginial deck and place in back of linked list for setup of game
int empty(card_t * node); //check to see if linked list is empty
void cleanUp(card_t * head); //free memory to prevent memory leaks
int deckSize(card_t * head); //count number of nodes in the linked list
card_t * search(card_t * node, int spot); //search list for a specific spot in the card deck indexing is similar to array setup
card_t * copyCard(card_t * node); //make a deep copy of card
card_t * removeCard(card_t * node, int spot); //remove card from linkedlist
card_t * insertBackDeck(card_t *head, card_t *node); //place card at end of pile
int compareCard(card_t * cardp1, card_t * cardp2); //compare cards
card_t * moveCardBack(card_t *head); //place card at top of deck to the bottom of the deck
int main()
{
int seed;
printf("Enter Seed: ");
scanf("%d", &seed);
srand(seed); //seed set
rules();
int player; //1 or 2
int result;
printf("Would you like to be player 1 or 2?\n");
printf("Enter 1 or 2: ");
scanf("%d", &player); //choose player
for(int game = 1; game <= 5; ++game) //simulate games
{
printf("Alright lets play game %d.\n", game);
printf("Lets split the deck.\n");
result = playRound(); //play game
if((result == 1 && player == 1) || (result == 2 && player == 2)) 
//determine who won
printf("You won game %d.\n", game);
else
printf("I won game %d.\n", game);
}
return 0;
}
void rules()
{
printf("Welcome to the card game war!\n");
printf("Here are the rules.\n");
printf("You have a pile of cards and I have a pile of cards.\n");
printf("We will each pull the top card off of our respective deck and compare them.\n");
printf("The card with the highest number will win the round and take both cards.\n");
printf("However if there is a tie, then we have to we have to place one card faced down and the next one faced up and compare the results.\n");
printf("Winner of the tie, will grab all the cards out. However if it's a tie again, then we repeat the same action.\n");
printf("Ready? Here we go!\n");
}
card_t * openCardDeck()
{
FILE *fptr = fopen("deck.txt", "r");
char *name = (char *) malloc(sizeof(char) * 20);
if (name == NULL)
{
printf("Error in malloc...\n");
exit(1);
}
card_t * head = NULL;
int cardrank = 13;
int tracker = 1;
while(fscanf(fptr, "%s", name) == 1)
{
strcat(name, " ");
if(tracker % 5 == 1)
{
strcat(name, "of Clubs");
head = insertBackSetup(head, name, cardrank);
}
else if(tracker % 5 == 2)
{
strcat(name, "of Diamonds");
head = insertBackSetup(head, name, cardrank);
}
else if(tracker % 5 == 3)
{
strcat(name, "of Hearts");
head = insertBackSetup(head, name, cardrank);
}
else if(tracker % 5 == 4)
{
strcat(name, "of Spades");
head = insertBackSetup(head, name, cardrank);
tracker = 0;
--cardrank;
}
++tracker;
}
free(name);
fclose(fptr);
return head;
}
card_t * insertBackSetup(card_t *node, char *name, int cardrank)
{
    if(empty(node)) //check to see if list is empty
    {
node = (card_t *) malloc(sizeof(card_t));
if(empty(node)) //check to see if malloc worked
{
printf("Did not allocate head successfully...");
printf("Program Terminating...\n");
exit(1);
}
else //otherwise populate data of new head node
{
node->type = (char *) malloc(sizeof(char) * 20);
if(node->type == NULL)
{
printf("Error with malloc...\n");
exit(1);
}
strcpy(node->type, name);
node->rank = cardrank;
node->nextptr = NULL; //must make new tail nextptr NULL!!!
return node;  //terminate
}
      
    }
//here we know that the list has at least one node
card_t *head = node; //keep pointer to head since we will need to return this address
while(node->nextptr != NULL) //traverse to tail
node = node->nextptr;
node->nextptr = (card_t *) malloc(sizeof(card_t)); 
if(node->nextptr == NULL) //see if new tail was allocated successfully
{
printf("Did not allocate node successfully...");
return head; //terminate if tail didn't get created
}
//populate new node
node->nextptr->type = (char *) malloc(sizeof(char) * 20);
if(node->nextptr->type == NULL)
{
printf("Error with malloc...\n");
exit(1);
}
strcpy(node->nextptr->type, name);
node->nextptr->rank = cardrank;
node->nextptr->nextptr = NULL; //very important
return head; //return head node since we need our starting point of the linked list
}
int empty(card_t * node) // if empty
{
return (node == NULL); //return condition result
}
void cleanUp(card_t * head){ // frees the linked list
  card_t *temp;
  while (head != NULL){
    temp = head;
    head = head->nextptr;
    free(temp);
  }
}
int deckSize(card_t * head){ // recursive versioin of finding linked list size
  if(head == NULL) return 0;
  int i=deckSize(head->nextptr);
  return 1+i;
}
card_t * search(card_t * node, int spot){ // searches for node based on position
  card_t *current = node; // head node
  for(int i = 0; i < deckSize(openCardDeck()); i++){ // creates postion 0-51
    if(i == spot) // 
      return current;
  current = current->nextptr;
    }
  return NULL;
  }
card_t * removeCard(card_t * node, int spot){ // removes a card by the position its in the linked list
if (empty(node)){
      return NULL;
   }
   card_t *temp = node; // creates a temp pointer
   if(spot == 0) { // if = sot 0
      node = temp->nextptr;
      free(temp);
      return node;
   }
   for(int i = 0; temp != NULL && i < spot - 1; i++) {
      temp = temp->nextptr;
   }
   if(temp == NULL || temp->nextptr == NULL) {
      return node;
   }
   card_t *next = temp->nextptr->nextptr;
   free(temp->nextptr);
   temp->nextptr = next;
  return node;
}
card_t * insertBackDeck(card_t *head, card_t *node){ // inserts a node to the back of a linked list
  if(empty(head)){
    head = node;
    return node;
  }
  else{
  card_t *temp = head;
  while(temp->nextptr != NULL)
    temp = temp->nextptr;
  temp->nextptr = node;
  return head;
    }
}
int compareCard(card_t * cardp1, card_t * cardp2){ // compares the player 1 and player 2 rank
  if(cardp1 == NULL || cardp2 == NULL)
    return 3;
  else if(cardp1->rank > cardp2->rank)
    return 1;
  else if(cardp1->rank < cardp2->rank)
    return 2;
  else{
    return 0;
  }
}
card_t * moveCardBack(card_t *head){ // moves head to tail and makes the second node the new head
  if(empty(head))
    return NULL;
  card_t *temp1 = head;
  card_t *temp2 = head;
  while(temp1->nextptr != NULL)
   temp1 = temp1->nextptr;
   temp1->nextptr = head;
   temp2 = temp2->nextptr;
   head->nextptr = NULL;
  return temp2;
}
card_t * copyCard(card_t * node){ // creates a copy of a single node
  if(node == NULL)
    return NULL;
  else{
    card_t *cpyNode = malloc(sizeof(card_t));
    cpyNode->rank = node->rank;
    cpyNode->type = (char *) malloc(sizeof(char) * 20);
    strcpy(cpyNode->type, node->type);
    cpyNode->nextptr = NULL;
    return cpyNode;
  }
}
int playRound(){
  card_t *deck = NULL;
  deck = openCardDeck(); // unshuffled deck
  card_t *head1 = NULL; // player 1 cards
  card_t *head2 = NULL; // player 2 cards
  card_t *node1 = NULL;
  card_t *node2 = NULL;
  int size = deckSize(deck)/2;
  printf("There are %d cards in the deck.\n", deckSize(deck));
  for(int i = 0; i < size; i++){ // iterates through the deck 26 times giving each player 1 card
    int val1 = rand() % (deckSize(deck));// val1 = random number for player one deck
    node2 = copyCard(search(deck,val1)); // copies node from deck puts in node 2
    head2 = insertBackDeck(head2,node2); // inserts node2 into the back of head 2
    deck = removeCard(deck, val1); // remove from deck
    int val2 = rand() % (deckSize(deck)); // val2 = random number for player two deck
    node1 = copyCard(search(deck,val2));
    head1 = insertBackDeck(head1, node1);
    deck = removeCard(deck, val2);
  }
 
  //lets play the game!
  while(head1 != NULL || head2 != NULL){
   if(head1 == NULL || head2 == NULL)
     break;
    if(compareCard(head1, head2) == 1){
      // if player 1 wins no war
      printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
      printf("Player 1 won that round.\n");
      head1 = moveCardBack(head1);
      head1 = insertBackDeck(head1,copyCard(head2));
      head2 = removeCard(head2, 0);
      printf("Player 1 has %d cards.\n", deckSize(head1));
      printf("Player 2 has %d cards.\n", deckSize(head2));
  }
    else if(compareCard(head1, head2) == 2){
      // if player 2 wins no war
      printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
      printf("Player 2 won that round.\n");
      head2 = moveCardBack(head2);
      head2 = insertBackDeck(head2, copyCard(head1));
      head1 = removeCard(head1, 0);
      printf("Player 1 has %d cards.\n", deckSize(head1));
      printf("Player 2 has %d cards.\n", deckSize(head2));
  }
  while(compareCard(head1, head2) == 0){
    if((deckSize(head1) > 2 || deckSize(head2) > 2) && (compareCard(head1->nextptr->nextptr, head2->nextptr->nextptr) != 0)){
      if(compareCard(head1->nextptr->nextptr, head2->nextptr->nextptr) == 1){
        //if there is war and player 1 wins
        printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
        printf("Ugh oh...We have a tie! W-A-R!\n");
        head1 = moveCardBack(head1);
        head1 = moveCardBack(head1);
        head1 = insertBackDeck(head1,copyCard(head2));
        head2 = removeCard(head2, 0);
        head1 = insertBackDeck(head1,copyCard(head2));
        head2 = removeCard(head2, 0);
        printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
        head1 = moveCardBack(head1);
        head1 = insertBackDeck(head1,copyCard(head2));
        head2 = removeCard(head2, 0);
        printf("Player 1 won that W-A-R!\n");
        printf("Player 1 has %d cards.\n", deckSize(head1));
        printf("Player 2 has %d cards.\n", deckSize(head2));
      }
      else if(compareCard(head1->nextptr->nextptr, head2->nextptr->nextptr) == 2) {
        // if there is war and player 2 wins
        printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
        printf("Ugh oh...We have a tie! W-A-R!\n");
        head2 = insertBackDeck(head2,copyCard(head1));
        head1 = removeCard(head1, 0);
        head2 = insertBackDeck(head2,copyCard(head1));
        head1 = removeCard(head1, 0);
        head2 = moveCardBack(head2);
        head2 = moveCardBack(head2);
        printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
        head2 = insertBackDeck(head2,copyCard(head1));
        head1 = removeCard(head1, 0);
        head2 = moveCardBack(head2);
        printf("Player 2 won that W-A-R!\n");
        printf("Player 1 has %d cards.\n", deckSize(head1));
        printf("Player 2 has %d cards.\n", deckSize(head2));
        }
      }
    // double war
    if(compareCard(head1,head2) == 0){
      if((deckSize(head1) > 5 || deckSize(head2) > 5) && (compareCard(head1->nextptr->nextptr, head2->nextptr->nextptr) == 0)){
        if(compareCard(head1->nextptr->nextptr->nextptr->nextptr, head2->nextptr->nextptr->nextptr->nextptr) == 1){
          // if there are two consecutive wars and player 1 wins
          printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
          printf("Ugh oh...We have a tie! W-A-R!\n");
          head1 = moveCardBack(head1);
          head1 = moveCardBack(head1);
          head1 = insertBackDeck(head1,copyCard(head2));
          head2 = removeCard(head2, 0);
          head1 = insertBackDeck(head1,copyCard(head2));
          head2 = removeCard(head2, 0);
          printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
          printf("Player 1 has %d cards.\n", deckSize(head1)-4);
          printf("Player 2 has %d cards.\n", deckSize(head2));
          printf("Ugh oh...We have a tie! W-A-R!\n");
          head1 = moveCardBack(head1);
          head1 = moveCardBack(head1);
          head1 = insertBackDeck(head1,copyCard(head2));
          head2 = removeCard(head2, 0);
          head1 = insertBackDeck(head1,copyCard(head2));
          head2 = removeCard(head2, 0);
          printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
          head1 = moveCardBack(head1);
          head1 = insertBackDeck(head1,copyCard(head2));
          head2 = removeCard(head2, 0);
          printf("Player 1 won that W-A-R!\n");
          printf("Player 1 has %d cards.\n", deckSize(head1));
          printf("Player 2 has %d cards.\n", deckSize(head2));
      }
        else if(compareCard(head1->nextptr->nextptr->nextptr->nextptr, head2->nextptr->nextptr->nextptr->nextptr) == 2){
          // if player 2 wins if there are two consecutive ties
          printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
          printf("Ugh oh...We have a tie! W-A-R!\n");
          head2 = insertBackDeck(head2,copyCard(head1));
          head1 = removeCard(head1, 0);
          head2 = insertBackDeck(head2,copyCard(head1));
          head1 = removeCard(head1, 0);
          head2 = moveCardBack(head2);
          head2 = moveCardBack(head2);
          printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
          printf("Player 1 has %d cards.\n", deckSize(head1));
          printf("Player 2 has %d cards.\n", deckSize(head2)-4);
          printf("Ugh oh...We have a tie! W-A-R!\n");
          head2 = insertBackDeck(head2,copyCard(head1));
          head1 = removeCard(head1, 0);
          head2 = insertBackDeck(head2,copyCard(head1));
          head1 = removeCard(head1, 0);
          head2 = moveCardBack(head2);
          head2 = moveCardBack(head2);
          printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
          head2 = insertBackDeck(head2,copyCard(head1));
          head1 = removeCard(head1, 0);
          head2 = moveCardBack(head2);
          printf("Player 2 won that W-A-R!\n");
          printf("Player 1 has %d cards.\n", deckSize(head1));
          printf("Player 2 has %d cards.\n", deckSize(head2));
        }
      }
    }
    if(compareCard(head1,head2) == 0){
      if((deckSize(head1) > 7 || deckSize(head2) > 7) && (compareCard(head1->nextptr->nextptr, head2->nextptr->nextptr) == 0) && compareCard(head1->nextptr->nextptr->nextptr->nextptr, head2->nextptr->nextptr->nextptr->nextptr) == 0){
        if(compareCard(head1->nextptr->nextptr->nextptr->nextptr->nextptr->nextptr, head2->nextptr->nextptr->nextptr->nextptr->nextptr->nextptr) == 1){
          // if there is 3 consecutive wars and player 1 wins
          printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
          printf("Ugh oh...We have a tie! W-A-R!\n");
          head1 = moveCardBack(head1);
          head1 = moveCardBack(head1);
          head1 = insertBackDeck(head1,copyCard(head2));
          head2 = removeCard(head2, 0);
          head1 = insertBackDeck(head1,copyCard(head2));
          head2 = removeCard(head2, 0);
          printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
          printf("Player 1 has %d cards.\n", deckSize(head1)-4);
          printf("Player 2 has %d cards.\n", deckSize(head2));
          printf("Ugh oh...We have a tie! W-A-R!\n");
          head1 = moveCardBack(head1);
          head1 = moveCardBack(head1);
          head1 = insertBackDeck(head1,copyCard(head2));
          head2 = removeCard(head2, 0);
          head1 = insertBackDeck(head1,copyCard(head2));
          head2 = removeCard(head2, 0);
          printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
          printf("Player 1 has %d cards.\n", deckSize(head1)-8);
          printf("Player 2 has %d cards.\n", deckSize(head2));
          printf("Ugh oh...We have a tie! W-A-R!\n");
          head1 = moveCardBack(head1);
          head1 = moveCardBack(head1);
          head1 = insertBackDeck(head1,copyCard(head2));
          head2 = removeCard(head2, 0);
          head1 = insertBackDeck(head1,copyCard(head2));
          head2 = removeCard(head2, 0);
          printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
          head1 = moveCardBack(head1);
          head1 = insertBackDeck(head1,copyCard(head2));
          head2 = removeCard(head2, 0);
          printf("Player 1 won that W-A-R!\n");
          printf("Player 1 has %d cards.\n", deckSize(head1));
          printf("Player 2 has %d cards.\n", deckSize(head2));
        }
        if(compareCard(head1->nextptr->nextptr->nextptr->nextptr->nextptr->nextptr, head2->nextptr->nextptr->nextptr->nextptr->nextptr->nextptr) == 2){
          // if there are 3 consecutive wars and player 2 wins
        printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
        printf("Ugh oh...We have a tie! W-A-R!\n");
        head2 = insertBackDeck(head2,copyCard(head1));
        head1 = removeCard(head1, 0);
        head2 = insertBackDeck(head2,copyCard(head1));
        head1 = removeCard(head1, 0);
        head2 = moveCardBack(head2);
        head2 = moveCardBack(head2);
        printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
        printf("Player 1 has %d cards.\n", deckSize(head1));
        printf("Player 2 has %d cards.\n", deckSize(head2)-4);
        printf("Ugh oh...We have a tie! W-A-R!\n");
        head2 = insertBackDeck(head2,copyCard(head1));
        head1 = removeCard(head1, 0);
        head2 = insertBackDeck(head2,copyCard(head1));
        head1 = removeCard(head1, 0);
        head2 = moveCardBack(head2);
        head2 = moveCardBack(head2);
        printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
        printf("Player 1 has %d cards.\n", deckSize(head1));
        printf("Player 2 has %d cards.\n", deckSize(head2)-8);
        printf("Ugh oh...We have a tie! W-A-R!\n");
        head2 = insertBackDeck(head2,copyCard(head1));
        head1 = removeCard(head1, 0);
        head2 = insertBackDeck(head2,copyCard(head1));
        head1 = removeCard(head1, 0);
        head2 = moveCardBack(head2);
        head2 = moveCardBack(head2);
        printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
        head2 = insertBackDeck(head2,copyCard(head1));
        head1 = removeCard(head1, 0);
        head2 = moveCardBack(head2);
        printf("Player 2 won that W-A-R!\n");
        printf("Player 1 has %d cards.\n", deckSize(head1));
        printf("Player 2 has %d cards.\n", deckSize(head2));
          }
        }
      }
    if(compareCard(head1, head2) == 0){
     if(deckSize(head1) == 2 || deckSize(head2) == 2 ){ 
       // if there is 2 cards left and p1 wins
        if(compareCard(head1->nextptr, head2->nextptr) == 1){
         printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
         printf("Ugh oh...We have a tie! W-A-R!\n");
         head1 = moveCardBack(head1);
         head1 = insertBackDeck(head1,copyCard(head2));
         head2 = removeCard(head2, 0);
         printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
         head1 = moveCardBack(head1);
         head1 = insertBackDeck(head1,copyCard(head2));
         head2 = removeCard(head2, 0);
         printf("Player 1 won that W-A-R!\n");
         printf("Player 1 has %d cards.\n", deckSize(head1));
         printf("Player 2 has %d cards.\n", deckSize(head2));
        }
     else if(compareCard(head1->nextptr, head2->nextptr) == 2){
       // if there is 2 cards left and p2 wins
         printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
         printf("Ugh oh...We have a tie! W-A-R!\n");
         head2 = insertBackDeck(head2,copyCard(head1));
         head1 = removeCard(head1, 0);
         head2 = moveCardBack(head2);
         printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
         head2 = insertBackDeck(head2,copyCard(head1));
         head1 = removeCard(head1, 0);
         head2 = moveCardBack(head2);
         printf("Player 2 won that W-A-R!\n");
         printf("Player 1 has %d cards.\n", deckSize(head1));
         printf("Player 2 has %d cards.\n", deckSize(head2));
        }
       }
      }
    if(compareCard(head1, head2) == 0){
    if(deckSize(head1) == 1 || deckSize(head2) == 1){
      if(compareCard(head1, head2) == 1){
        // if there is 1 cards left and p1 wins
         printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
         printf("Ugh oh...We have a tie! W-A-R!\n");
         head1 = moveCardBack(head1);
         printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
         head1 = insertBackDeck(head1,copyCard(head2));
         head2 = removeCard(head2, 0);
         printf("Player 1 won that W-A-R!\n");
         printf("Player 1 has %d cards.\n", deckSize(head1));
         printf("Player 2 has %d cards.\n", deckSize(head2));
        }
     else if((deckSize(head1) >= 2 && deckSize(head2) >= 2) && compareCard(head1->nextptr, head2->nextptr) == 2){
       // if there is 1 cards left and p2 wins
         printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
         printf("Ugh oh...We have a tie! W-A-R!\n");
         head2 = insertBackDeck(head2,copyCard(head1));
         head1 = removeCard(head1, 0);
         printf("Player 1 pulled out %s. 	 Player 2 pulled out %s.\n",head1->type,head2->type);
         head2 = moveCardBack(head2);
         printf("Player 2 won that W-A-R!\n");
         printf("Player 1 has %d cards.\n", deckSize(head1));
         printf("Player 2 has %d cards.\n", deckSize(head2));   
      }
     }
    }
  }
}
  
    if(deckSize(head1) == 0){ // if head 1 is has 0 cards return 2
      cleanUp(head1); // frees head1
      cleanUp(head2); // frees head2
      return 2;
         }
    else{    // else head2 = 0 player 1 wins
      cleanUp(head1);
      cleanUp(head2);
       return 1;
      }
}
