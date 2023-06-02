package org.study.exceptions;

public class DuplicateEntryException extends Exception {
    public DuplicateEntryException() {
        super("Duplicate entry found.");
    }
}
