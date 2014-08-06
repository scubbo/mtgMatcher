package com.scubbo.mtgmatcher.server;

public class JSONMarshallingException extends RuntimeException {
    JSONMarshallingException(String message) {super(message);}
    JSONMarshallingException(Throwable e) {super(e);}
    JSONMarshallingException(String message, Throwable e) {super(message, e);}
}
