package com.junt.xdialog.core;

import java.util.Stack;

public class DialogStack {
    private static DialogStack dialogStack;

    private Stack<XCoreDialog> xCoreDialogStackImpl;

    public static DialogStack getInstance() {
        if (dialogStack==null){
            dialogStack=new DialogStack();
        }
        return dialogStack;
    }

    private DialogStack() {
        xCoreDialogStackImpl =new Stack<>();
    }

    public void addDialog(XCoreDialog XCoreDialog) {
        xCoreDialogStackImpl.add(XCoreDialog);
    }

    public void removeDialog(XCoreDialog XCoreDialog) {
        xCoreDialogStackImpl.remove(XCoreDialog);
    }

    public XCoreDialog getTopDialog(){
        return xCoreDialogStackImpl.peek();
    }

    public XCoreDialog getBottomDialog(){
        return xCoreDialogStackImpl.get(0);
    }

    public Stack<XCoreDialog> getXCoreDialogStack(){
        return xCoreDialogStackImpl;
    }
}
