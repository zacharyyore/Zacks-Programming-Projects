//
//  main.c
//  P4
//
//  Created by Zack Yore on 10/27/21.
//

#include <stdio.h>
#include <stdlib.h>


void merge(int arr[], int l, int m, int h){
  // merge sort function
int n1 = m-l+1;
int n2 = h-m;
int left[n1];
int right[n2];
for(int i = 0; i < n1; i++){
  left[i] = arr[l+i];
}
for(int i = 0; i < n2; i++){
  right[i]= arr[m+1+i];
}
int i = 0;
int j = 0;
int k = l;
while(i < n1 && j < n2){
  if(left[i] <= right[j]){
    arr[k] = left[i];
    i++;
  }
  else{
    arr[k] = right[j];
    j++;
  }
  k++;
}
while(i < n1){
  arr[k] = left[i];
  i++;
  k++;
  }
while(j < n2){
arr[k] = right[j];
j++;
k++;
}
}

void recMerge(int arr[], int l, int h){
  // recursive merge sort function
if(l < h){
  int m = l+(h-l)/2;
  recMerge(arr,l,m);
  recMerge(arr,1+m,h);
  merge(arr,l,m,h);
  }
}

long long readMe(int arr[], long long size, long long limit){
  // read me function does the program logic for "reading the books"
  long long booksRead = 0;
  long long pagesRead = 0;
  for(int i = 0; i < size; i++){
    if(arr[i] + pagesRead > limit){
        // if number of pages + the pages "read" is greater then limit return books read
      return booksRead;
    }
    booksRead += 1;
      // adds one book
    pagesRead += arr[i];
  }
  return booksRead;
}

int main(void) {
  int testCases = 0;
  long long bookLim;
  long long pageLim;
  int* pageNums;
  scanf("%d",&testCases);
  // takes in the number of test cases
  for(int i = 0; i < testCases; i++){
    scanf("%lld", &bookLim);
    // takes in the number of books
    pageNums = malloc(sizeof(int)*bookLim);
    scanf("%lld", &pageLim);
    // takes in the number of pages
    for(int j = 0; j < bookLim; j++){
      scanf("%d", &pageNums[j]);
    }
    recMerge(pageNums, 0, bookLim - 1.0);
    printf("%lld\n", readMe(pageNums, bookLim, pageLim));
    free(pageNums);
      // frees dynamically allocated memory
  }
  return 0;
}
