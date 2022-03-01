//
//  main.c
//  Smoothie
//
//  Created by Zack Yore on 9/11/21.
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct item {
 int itemID;     // item struct
 int numParts;
} item;

typedef struct recipe {
 int numItems;  // recipe struct & pointing to item struct
 item* itemList;
 int totalParts;
} recipe;


recipe** smoothieList;

double* amtOfEachItem;

char** readIngredients(int numIngredients){ // read ingredient function
    char** IngredientList;
    IngredientList = malloc((sizeof(char*))*numIngredients); // dynamic memory allocation of each ingredient
    for(int i = 0; i < numIngredients; i++){
        IngredientList[i] = malloc(sizeof(char)*20); // dynamic memory allocation of each ingredient word such as strawberry
        scanf("%s", IngredientList[i]);
    }
    return IngredientList;
}
 


recipe* readRecipe(int numItems){ // read recipe function
    item* arrItem;
    arrItem = malloc((sizeof(item))*numItems); // dynamic memory allocation of number of ingredients and ratio
    int total = 0;
    
    for(int i = 0; i < numItems; i++){
        scanf("%d", &(arrItem[i].itemID)); // scan in the ingredient #
        scanf("%d", &(arrItem[i].numParts)); // scan in the ratio
        total += arrItem[i].numParts;
     }
    recipe *specific = malloc(sizeof(recipe));
    specific->numItems = numItems;
    specific->itemList = arrItem;
    specific->totalParts = total;
    return specific;
}


recipe** readAllRecipes(int numRecipes){ // read all recipe function
    recipe** recipeBook = malloc((sizeof(recipe))*numRecipes);
    int itemsUsed = 0;
    for(int i = 0; i < numRecipes;i++){
        scanf("%d",&itemsUsed);
        recipeBook[i] = readRecipe(itemsUsed);
    }
    return recipeBook;
}
 

double* calculateOrder(int numSmoothies, recipe** recipeList, int numIngredients){ // calculate the order function
    double* order = malloc((sizeof(double))*numIngredients);
    int smootherNum;
    int weight;
    int whichIngredient;
    double ratio;
    double trueRatio;
    for(int i = 0; i < numSmoothies;i++){
        scanf("%d", &smootherNum);
        scanf("%d", &weight);
        for(int j = 0; j < recipeList[smootherNum]->numItems;j++){
            whichIngredient = recipeList[smootherNum]->itemList[j].itemID; // determines which ingredients
            ratio = (double)recipeList[smootherNum]->itemList[j].numParts / (double)recipeList[smootherNum]->totalParts; // calculates the variest weights for each ingredient for each store
            trueRatio = ratio * weight;
            order[whichIngredient] += trueRatio;
        
        }
    }
    return order;
}

void printOrder(char** ingredientNames, double* orderList, int numIngredients){ // function that prints our order
    double totalWeight;
    for(int i = 0; i < numIngredients; i++){
        totalWeight = orderList[i];
        if(totalWeight > 0.0){
            printf("%s  %f\n", ingredientNames[i],totalWeight);
       }
   }
    return;
}
void freeIngredients(char** ingredientList, int numIngredients){ // free our dynamic memory allocation for ingredients
    for(int i = 0; i < numIngredients; i++){
    free(ingredientList[i]); // frees the varies ingredients in array ingredientslist
    }
    free(ingredientList);
}

void freeRecipes(recipe** allRecipes, int numRecipes){ // free our dynamic memory allocation for recipe
    for(int i = 0; i < numRecipes; i ++){
        free(allRecipes[i]->itemList);
        free(allRecipes[i]);
    }
    free(allRecipes);
}

int main() {
    int ingredientNum = 0;

// Read Number of Ingredients and their names, not to exceed 20 chars
    scanf("%d",&ingredientNum);
    char ** list = readIngredients(ingredientNum);
    

// Read number of REecipes and their names, not to exceed 10^5
    int manySmoothies;

    int numRecipes, numStores = 0;
    scanf("%d", &numRecipes); // scans the varries smoothie recipes
    recipe** cookBook = readAllRecipes(numRecipes); // calls
    
    double* currentOrder; // calling current order
    scanf("%d", &numStores); // scans in the number of stores
    for(int i = 0; i < numStores;i++){
        scanf("%d", &manySmoothies); // scans the different smoothies in to each store
        currentOrder = calculateOrder(manySmoothies, cookBook, ingredientNum);
        printf("store #%d: \n",i);
        printOrder(list, currentOrder, ingredientNum); // calling print order
    }


   
    freeIngredients(list, ingredientNum); // free items in ingredients
    freeRecipes(cookBook, numRecipes); // free items in recipe
    return 0;
}

