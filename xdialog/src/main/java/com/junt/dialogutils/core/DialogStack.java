package com.junt.dialogutils.core;

import java.util.Stack;

public class DialogStack {
    private static DialogStack dialogStack;

    private Stack<CoreDialog> coreDialogStackImpl;

    public static DialogStack getInstance() {
        if (dialogStack==null){
            dialogStack=new DialogStack();
        }
        return dialogStack;
    }

    private DialogStack() {
        coreDialogStackImpl=new Stack<>();
    }

    public void addDialog(CoreDialog coreDialog) {
        coreDialogStackImpl.add(coreDialog);
    }

    public void removeDialog(CoreDialog coreDialog) {
        coreDialogStackImpl.remove(coreDialog);
    }

    public CoreDialog getTopDialog(){
        return coreDialogStackImpl.peek();
    }

    public CoreDialog getBottomDialog(){
        return coreDialogStackImpl.get(0);
    }
}
