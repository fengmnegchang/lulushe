package com.fgj.lulushe.activity.menu;

import com.fgj.lulushe.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 */
@SuppressLint("ValidFragment")
public class MessageFragment extends Fragment {
	private String  href = "";
	public MessageFragment() {
		super();
	}
    
    public MessageFragment(String href){
    	super();
    	this.href = href;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

}
