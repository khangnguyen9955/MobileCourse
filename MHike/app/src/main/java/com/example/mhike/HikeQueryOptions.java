package com.example.mhike;

import java.util.Stack;

public class HikeQueryOptions {
    private boolean isDateAscending;
    private boolean isLengthAscending;
    private boolean isRatingAscending;
   private boolean filterByName;
    private boolean filterByLocation;

    private Stack<String> sortingFieldsStack;

    public HikeQueryOptions() {
        isDateAscending = false;
        isLengthAscending = false;
        isRatingAscending = false;
        filterByName = false;
        filterByLocation = false;
        sortingFieldsStack = new Stack<>();
    }

    // Include getters and setters for each option
    public boolean isDateAscending() {
        return isDateAscending;
    }

    public void setDateAscending(boolean dateAscending) {
        isDateAscending = dateAscending;
    }

    public boolean isLengthAscending() {
        return isLengthAscending;
    }

    public void setLengthAscending(boolean lengthAscending) {
        isLengthAscending = lengthAscending;
    }

    public boolean isRatingAscending() {
        return isRatingAscending;
    }

    public void setRatingAscending(boolean ratingAscending) {
        isRatingAscending = ratingAscending;
    }

    public boolean isFilterByName() {
        return filterByName;
    }

    public void setFilterByName(boolean filterByName) {
        this.filterByName = filterByName;
    }

    public boolean isFilterByLocation() {
        return filterByLocation;
    }

    public void setFilterByLocation(boolean filterByLocation) {
        this.filterByLocation = filterByLocation;
    }

    public void pushSortingField(String sortingField) {
        sortingFieldsStack.push(sortingField);
    }
    public Stack getStack(){
        return this.sortingFieldsStack;
    }
    public void clearStack(){
        this.sortingFieldsStack.clear();
    }


}
