package com.junt.demo.dialog;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.junt.demo.R;
import com.junt.xdialog.core.XAttachDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class ListXAttachDialog extends XAttachDialog {
    private AdapterView.OnItemClickListener onItemClickListener;
    private List<String> data = new ArrayList<>();
    private ListView listView;

    public ListXAttachDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutResId() {
        return R.layout.dialog_attach_list;
    }

    @Override
    protected void initDialogContent() {
        listView = findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(getRealContext(), R.layout.item_attach_list, R.id.textView, data));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, view, position, id);
                }
                dismiss();
            }
        });
    }

    public void setData(List<String> arrayList) {
        data.clear();
        data.addAll(arrayList);
        if (listView != null) {
            ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
    }

    public void setData(List<String> arrayList, AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        data.clear();
        data.addAll(arrayList);
        if (listView != null) {
            ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
    }
}
